package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.enums.OrganizationStatus;
import com.csbaic.edatope.app.enums.StatusEnums;
import com.csbaic.edatope.app.enums.UserType;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.convert.RoleConvert;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.app.entity.Role;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.enums.UserStatus;
import com.csbaic.edatope.app.enums.UserType;
import com.csbaic.edatope.app.mapper.UserMapper;
import com.csbaic.edatope.app.model.command.CreateUserCmd;
import com.csbaic.edatope.app.model.command.DeleteUserCmd;
import com.csbaic.edatope.app.model.command.ResetPasswordCmd;
import com.csbaic.edatope.app.model.command.UpdateUserCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleConvert roleConvert;

    @Override
    public Set<String> getStringRoleForUser(String userId) {
        User user = getById(userId);
        if (user.getAdmin()) {
            List<Role> all = roleService.list(
                    Wrappers.<Role>query().eq(Role.STATUS, EnableStatus.ENABLED.name())
            );

            return all.stream()
                    .map(Role::getCode)
                    .collect(Collectors.toSet());
        }

        return roleService.getRolesByUserId(userId)
                .stream()
                .filter(role -> Objects.equals(role.getStatus(), EnableStatus.ENABLED.name()))
                .map(Role::getCode)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getStringPermissionForUser(String userId) {
        User user = getById(userId);
        if (user.getAdmin()) {
            List<Permission> all = permissionService.list(
                    Wrappers.<Permission>query().eq(Permission.STATUS, EnableStatus.ENABLED.name())
            );

            return all.stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toSet());
        }

        return roleService
                .getPermissionsByUserId(userId)
                .stream()
                .filter(role -> Objects.equals(role.getStatus(), EnableStatus.ENABLED.name()))
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

    @Override
    public User getByUsernameOrMobile(String text) {
        return getBaseMapper().getByUsernameOrMobile(text);
    }

    @OperateLog(remark = "#{operatorName} 添加用户成功 ")
    @Transactional
    @Override
    public void create(CreateUserCmd cmd) {
        User exists = getBaseMapper().getByUsernameOrMobile(cmd.getUsername());
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该用户名已经被使用");
        }
        exists = getBaseMapper().getByUsernameOrMobile(cmd.getMobile());
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该手机号已经被使用");
        }

        Organization organization = organizationService.getById(cmd.getOrgId());
        if (organization == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户所在单位不存在");
        }

        User user = new User();
        BeanCopyUtils.copyNonNullProperties(cmd, user);
        String salt = UUID.randomUUID().toString();
        String encoded = passwordEncoder.encode(cmd.getMobile().substring(3) + salt);
        user.setPasswordSalt(salt);
        user.setPassword(encoded);
        user.setLocked(Boolean.FALSE);
        user.setStatus(UserStatus.NORMAL.name());
        save(user);
        roleService.assignRoleToUser(user.getId(), cmd.getRoles());
    }

    @OperateLog(remark = "#{operatorName} 更新用户成功 ")
    @Override
    public void update(UpdateUserCmd cmd) {
        User user = getById(cmd.getId());
        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户不存在");
        }

        if (user.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "不能修改超级管理员");
        }

        if (StringUtils.isNotEmpty(cmd.getUsername()) && !Objects.equals(cmd.getUsername(), user.getUsername())) {
            User exists = getBaseMapper().getByUsernameOrMobile(cmd.getUsername());
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该用户名已经被使用");
            }
        }

        if (StringUtils.isNotEmpty(cmd.getMobile()) && !Objects.equals(cmd.getMobile(), user.getMobile())) {
            User exists = getBaseMapper().getByUsernameOrMobile(cmd.getMobile());
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该手机号已经被使用");
            }
        }
        BeanCopyUtils.copyNonNullProperties(cmd, user);
        if (CollectionUtils.isNotEmpty(cmd.getRoles())) {
            roleService.assignRoleToUser(user.getId(), cmd.getRoles());
        }
        updateById(user);
    }

    @Override
    public void delete(DeleteUserCmd cmd) {
        User user = getById(cmd.getId());
        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户不存在");
        }

        if (user.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "系统超级管理员无法删除");
        }
        //清空用户的角色
        roleService.assignRoleToUser(user.getId(), new ArrayList<>());
        removeById(user);
    }

    @Override
    public UserDTO getUserDetail(String userId) {
        User user = null;
        user = getBaseMapper().getUserDetail(userId);
        if (user == null) {
            log.info("用户不存在,用户id:" + userId);
            return null;
//                throw BizRuntimeException.from(ResultCode.ERROR, "用户不存在,用户id:" + userId);
        }
        return convertUserDTO(user);
    }

    @Override
    public UserDTO getSimpleUser(String userId) {
        User user = getBaseMapper().getUserDetail(userId);
        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户不存在");
        }

        UserDTO userDTO = convertUserDTO(user);
        userDTO.setOrg(null);
        userDTO.setRoles(null);
        return userDTO;
    }

    @Override
    public IPage<UserDTO> listUserPage(UserQuery query) {
        IPage<User> page = getBaseMapper().listUser(new Page<>(query.getPageIndex(), query.getPageSize()), query);
        return page.convert(this::convertUserDTO);
    }

    @Override
    public List<UserDTO> listAllUserByOrgId(String orgId) {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query().eq(User.ORG_ID, orgId);
        return list(queryWrapper)
                .stream()
                .map(this::convertUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> listAllUserByOrgIdLikeName(String orgId, String name) {
        QueryWrapper<User> queryWrapper = Wrappers.<User>query().eq(User.ORG_ID, orgId);
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(User.USERNAME, name);
        }

        return list(queryWrapper)
                .stream()
                .map(this::convertUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void resetPassword(ResetPasswordCmd cmd) {
        User user = getById(cmd.getId());
        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户不存在");
        }

        String salt = UUID.randomUUID().toString();
        String encoded = passwordEncoder.encode(user.getMobile().substring(3) + salt);
        user.setNeedSetPassword(true);
        user.setPasswordSalt(salt);
        user.setPassword(encoded);
        updateById(user);
    }

    public List<UserDTO> adminUserList(String orgId, String type) {
        if (StringUtils.isNotEmpty(type)) {
            return list(
                    Wrappers.<User>query().eq(User.ORG_ID, orgId).eq(User.TYPE, type)
            ).stream().map(this::convertUserDTO).collect(Collectors.toList());
        } else {
            return list(
                    Wrappers.<User>query().eq(User.ORG_ID, orgId)
            ).stream().map(this::convertUserDTO).collect(Collectors.toList());
        }
    }

    @Override
    public UserDTO getOrganizationAdmin(String orgId) {
        List<User> userList = list(
                Wrappers.<User>query().eq(User.ORG_ID, orgId)
                        .eq(User.TYPE, UserType.ORGANIZATION_ADMIN.getValue())
        );

        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(userList)) {
            User firstUser = userList.get(0);
            userList.get(0).setRoles(roleService.getRolesByUserId(firstUser.getId()));
            return convertUserDTO(userList.get(0));
        }
        return null;
    }

    /**
     * 转换成用户
     *
     * @param user
     */
    @Override
    public UserDTO convertUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanCopyUtils.copyNonNullProperties(user, userDTO);
        if (user.getOrg() != null) {
//            OrganizationDTO organizationDTO = new OrganizationDTO();
//            BeanCopyUtils.copyNonNullProperties(user.getOrg(), organizationDTO);
            userDTO.setOrg(organizationService.getDetail(user.getOrgId()));
        }
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            userDTO.setRoles(
                    user.getRoles()
                            .stream()
                            .map(roleConvert::convertToViewObject)
                            .collect(Collectors.toList())
            );
        }
        return userDTO;
    }
}
