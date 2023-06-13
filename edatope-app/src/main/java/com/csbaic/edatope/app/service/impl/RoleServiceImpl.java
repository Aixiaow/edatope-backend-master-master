package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.CollectionStreamUtils;
import com.csbaic.edatope.app.convert.RoleConvert;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.mapper.RoleMapper;
import com.csbaic.edatope.app.model.command.CreateRoleCmd;
import com.csbaic.edatope.app.model.command.DeleteRoleCmd;
import com.csbaic.edatope.app.model.command.UpdateRoleCmd;
import com.csbaic.edatope.app.model.dto.RoleDTO;
import com.csbaic.edatope.app.model.query.RoleQuery;
import com.csbaic.edatope.app.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private RoleConvert roleConvert;

    @Override
    @Transactional
    public void createRole(CreateRoleCmd cmd) {
        Role exists = getOne(Wrappers.<Role>query().eq(Role.CODE, cmd.getCode()));
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "角色编码已存在：" + cmd.getCode());
        }

        if (StringUtils.isNotEmpty(cmd.getOrgId())) {
            Organization organization = organizationService.getById(cmd.getOrgId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位不存在");
            }
        }

        Role entity = roleConvert.convertToEntity(cmd);
        save(entity);
        assignPermissionToRole(entity.getId(), cmd.getPermissions());
    }

    @Override
    public RoleDTO getRoleDetail(String id) {
        Role role = getById(id);
        if (role == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "角色不存在：" + id);
        }
        role.setPermissions(getPermissionsByRoleId(role.getId()));
        if (StringUtils.isNotEmpty(role.getOrgId())) {
            role.setOrg(organizationService.getById(role.getOrgId()));
        }
        return roleConvert.convertToViewObject(role);
    }

    @Override
    public IPage<RoleDTO> listRole(RoleQuery query) {
        QueryWrapper<Role> queryWrapper = Wrappers.query();

        if (StringUtils.isNotBlank(query.getName())) {
            queryWrapper.like(Role.NAME, query.getName());
        }
        if (StringUtils.isNotBlank(query.getProperty())) {
            queryWrapper.like(Role.PROPERTY, query.getProperty());
        }
        if (StringUtils.isNotBlank(query.getType())) {
            queryWrapper.like(Role.TYPE, query.getType());
        }
        if (StringUtils.isNotBlank(query.getLevel())) {
            queryWrapper.like(Role.LEVEL, query.getLevel());
        }
        queryWrapper.orderByDesc(Role.CREATE_TIME);
        IPage<Role> page = page(new Page<>(query.getPageIndex(), query.getPageSize()), queryWrapper);
        page.getRecords().forEach(entity -> {
            entity.setPermissions(getPermissionsByRoleId(entity.getId()));
            if (StringUtils.isNotEmpty(entity.getOrgId())) {
                entity.setOrg(organizationService.getById(entity.getOrgId()));
            }
        });

        return page.convert(roleConvert::convertToViewObject);
    }

    @Override
    public List<RoleDTO> listAllRole() {
        return list()
                .stream()
                .map(roleConvert::convertToViewObject)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> listRoleByOrgId(String orgId) {
        QueryWrapper<Role> queryWrapper = Wrappers.<Role>query().eq(Role.ORG_ID, orgId);
        return list(queryWrapper)
                .stream()
                .map(roleConvert::convertToViewObject)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateRole(UpdateRoleCmd cmd) {
        Role role = getById(cmd.getId());
        if (role == null) {
            return;
        }

        if (StringUtils.isNotEmpty(cmd.getCode()) && !Objects.equals(cmd.getCode(), role.getCode())) {
            Role roleByCode = getOne(
                    Wrappers.<Role>query().eq(Role.CODE, cmd.getCode())
            );
            if (roleByCode != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "角色编码已存在：" + cmd.getCode());
            }
        }

        if (StringUtils.isNotEmpty(cmd.getOrgId())) {
            Organization organization = organizationService.getById(cmd.getOrgId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位不存在");
            }
        }

        BeanUtils.copyProperties(cmd, role);
        updateById(role);
        //更新权限
        if (CollectionUtils.isNotEmpty(cmd.getPermissions())) {
            assignPermissionToRole(role.getId(), cmd.getPermissions());
        }
    }

    @Transactional
    @Override
    public void deleteRole(DeleteRoleCmd cmd) {
        Role role = getById(cmd.getId());
        if (role == null) {
            return;
        }

        removeById(cmd.getId());
        removePermissionByRoleId(role.getId());

        // 刪除角色需要移除所有用户有这个角色对应的信息
        userRoleService.remove(
                Wrappers.
                        <UserRole>query().eq(UserRole.ROLE_ID, cmd.getId())
        );
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        List<UserRole> userRoles = userRoleService.list(Wrappers.<UserRole>query().eq(UserRole.USER_ID, userId));
        List<Role> roles = listByIds(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        return roles;
    }

    @Override
    public List<Permission> getPermissionsByUserId(String userId) {
        List<String> ids = getRolesByUserId(userId).stream().map(Role::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<String> permissionIds = rolePermissionService.list(
                Wrappers.<RolePermission>query().in(RolePermission.ROLE_ID, ids)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(permissionIds) ? permissionService.listByIds(permissionIds) : new ArrayList<>();
    }

    @Override
    public List<Permission> getPermissionsByRoleId(String roleId) {
        List<String> permissionIds = rolePermissionService.list(
                Wrappers.<RolePermission>query().in(RolePermission.ROLE_ID, roleId)
        ).stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(permissionIds)) {
            return new ArrayList<>();
        }
        return permissionService.listByIds(permissionIds);
    }

    @Override
    public void addPermissionsToRole(String roleId, List<String> permissionIds) {

        List<Permission> permissions = permissionService.listByIds(permissionIds);
        Map<String, Permission> permissionMap = CollectionStreamUtils.toMap(permissions, Permission::getId);
        List<RolePermission> existList = rolePermissionService.list(
                Wrappers.<RolePermission>query().eq(RolePermission.ROLE_ID, roleId)
                        .in(RolePermission.PERMISSION_ID, permissionIds)
        );
        if (CollectionUtils.isNotEmpty(existList)) {
            RolePermission first = existList.get(0);
            throw new BizRuntimeException(ResultCode.ERROR.getCode(), "权限已存在：" + permissionMap.get(first.getPermissionId()).getName());
        }

        List<RolePermission> pairList = new ArrayList<>();
        permissions.forEach(p -> {
            RolePermission entity = new RolePermission();
            entity.setPermissionId(p.getId());
            entity.setRoleId(roleId);
            pairList.add(entity);
        });

        rolePermissionService.saveBatch(pairList);
    }

    @Override
    public void removePermissionByRoleId(String roleId) {
        rolePermissionService.remove(
                Wrappers.<RolePermission>query().eq(RolePermission.ROLE_ID, roleId)
        );
    }

    @Override
    public void removePermissionByRoleId(String roleId, List<String> permissionIds) {
        rolePermissionService.remove(
                Wrappers.<RolePermission>query().eq(RolePermission.ROLE_ID, roleId)
                        .in(RolePermission.PERMISSION_ID, permissionIds)
        );
    }

    @Override
    public void assignRoleToUser(String userId, List<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        List<Role> roles = listByIds(roleIds);
        List<String> checkRoleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        checkRoleIds.forEach(s -> {
            if (!checkRoleIds.contains(s)) {
                throw BizRuntimeException.from(ResultCode.ERROR, "角色不存在");
            }
        });

        List<UserRole> assigned = userRoleService.list(
                Wrappers.<UserRole>query()
                        .eq(UserRole.USER_ID, userId)
        );

        List<String> assignedIds = assigned.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        Collection<String> roleToAdd = CollectionUtils.subtract(roleIds, assignedIds);
        Collection<String> roleToRemove = CollectionUtils.subtract(assignedIds, roleIds);

        if (CollectionUtils.isNotEmpty(roleToRemove)) {
            //移除不需要的
            userRoleService.remove(
                    Wrappers.
                            <UserRole>query().
                            eq(UserRole.USER_ID, userId).
                            in(UserRole.ROLE_ID, roleToRemove)
            );
        }

        if (CollectionUtils.isNotEmpty(roleToAdd)) {
            List<UserRole> userRoles = roleToAdd
                    .stream()
                    .distinct()
                    .map(s -> {
                        UserRole userRole = new UserRole();
                        userRole.setRoleId(s);
                        userRole.setUserId(userId);
                        return userRole;
                    }).collect(Collectors.toList());
            userRoleService.saveBatch(userRoles);
        }
    }

    @Override
    public void assignPermissionToRole(String roleId, List<String> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        List<Permission> permissions = permissionService.listByIds(permissionIds);
        List<String> existIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());
        permissionIds.forEach(s -> {
            if (!existIds.contains(s)) {
                throw BizRuntimeException.from(ResultCode.ERROR, "权限不存在，无法分配");
            }
        });

        final List<RolePermission> rolePermissions = new ArrayList<>();
        permissions.forEach(s -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(s.getId());
            rolePermissions.add(rolePermission);
        });

        //防止重新添加
        List<RolePermission> existPermissions = rolePermissionService.list(
                Wrappers.<RolePermission>query().eq(RolePermission.ROLE_ID, roleId)
        );
        //删除之前的记录
        rolePermissionService.removeByIds(existPermissions);
        rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public void appendRoleToUser(String userId, List<String> roles) {
        List<String> roleIds = getRolesByUserId(userId)
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList());

        roleIds.addAll(roles);
        assignRoleToUser(userId, roleIds);

    }

    @Override
    public void removeRoleFromUser(String userId, List<String> roles) {
        List<String> roleIds = getRolesByUserId(userId)
                .stream()
                .map(Role::getId)
                .filter(s -> !roles.contains(s))
                .collect(Collectors.toList());

        assignRoleToUser(userId, roleIds);
    }
}
