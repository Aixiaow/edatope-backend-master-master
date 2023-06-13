package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.enums.OperateType;
import com.csbaic.edatope.app.convert.PermissionConvert;
import com.csbaic.edatope.app.entity.Api;
import com.csbaic.edatope.app.entity.Menu;
import com.csbaic.edatope.app.model.command.DeletePermissionCmd;
import com.csbaic.edatope.app.model.command.CreatePermissionCmd;
import com.csbaic.edatope.app.model.command.UpdatePermissionCmd;
import com.csbaic.edatope.app.model.query.PermissionQuery;
import com.csbaic.edatope.app.model.dto.PermissionDTO;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.app.mapper.PermissionMapper;
import com.csbaic.edatope.app.service.IApiService;
import com.csbaic.edatope.app.service.IMenuService;
import com.csbaic.edatope.app.service.IPermissionService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    private PermissionConvert permissionConvert = new PermissionConvert();

    @Lazy
    @Autowired
    private IApiService apiService;

    @Lazy
    @Autowired
    private IMenuService menuService;


    @Override
    public Permission getByCode(String code) {
        return getOne(Wrappers.<Permission>query().eq(Permission.CODE, code));
    }

    @OperateLog(operateType = OperateType.CREATE)
    @Override
    @Transactional
    public void create(CreatePermissionCmd cmd) {
        Permission exists = getOne(Wrappers.<Permission>query().eq(Permission.CODE, cmd.getCode()));
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "权限编码：" + cmd.getCode() + " 已存在");
        }

        if (!Strings.isNullOrEmpty(cmd.getPid())) {
            Permission parent = getById(cmd.getPid());
            if (parent == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "上级权限不存在：" + cmd.getPid());
            }
        }

        Permission entity = permissionConvert.convertToEntity(cmd);
        save(entity);
    }

    @OperateLog(operateType = OperateType.UPDATE)
    @Transactional
    @Override
    public void update(UpdatePermissionCmd cmd) {
        Permission permission = getById(cmd.getId());
        if (permission == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "权限不存在");
        }
        BeanCopyUtils.copyNonNullProperties(cmd, permission);
        updateById(permission);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<PermissionDTO> list(PermissionQuery query) {

        QueryWrapper<Permission> queryWrapper = Wrappers.<Permission>query().orderByAsc(Permission.SORT);

        if (!Strings.isNullOrEmpty(query.getType())) {
            queryWrapper.eq(Permission.TYPE, query.getType());
        }
        if (!Strings.isNullOrEmpty(query.getName())) {
            queryWrapper.like(Permission.NAME, query.getName());
        }
        if (!Strings.isNullOrEmpty(query.getCode())) {
            queryWrapper.like(Permission.CODE, query.getCode());
        }

        List<Permission> list  = list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        Set<String> ids = new HashSet<>();
        list.forEach(permission -> {
            ids.add(permission.getId());
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(permission.getPid())) {
                ids.add(permission.getPid());
            }
        });

        List<PermissionDTO> all = listByIds(ids)
                .stream()
                .map(permissionConvert::convertToViewObject)
                .collect(Collectors.toList());

        Map<String, PermissionDTO> detailVoMap = all
                .stream().collect(Collectors.toMap(PermissionDTO::getId, java.util.function.Function.identity()));

        List<PermissionDTO> root = new ArrayList<>();
        all.forEach(permissionVo -> {
            if (StringUtils.isBlank(permissionVo.getPid())) {
                root.add(permissionVo);
            } else {
                PermissionDTO parent = detailVoMap.get(permissionVo.getPid());
                if(parent != null){
                    parent.getChildren().add(permissionVo);
                }
            }
        });

        return root;
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public PermissionDTO getPermissionDetail(String permissionId) {
        Permission permission = getById(permissionId);
        if(permission == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到权限：" + permissionId);
        }
        PermissionDTO detailVo = permissionConvert.convertToViewObject(permission);
        if (StringUtils.isNotBlank(permission.getPid())) {
            Permission parent = getById(permission.getPid());
            if (parent == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级权限：" + permission.getPid());
            }

            detailVo.setParent(permissionConvert.convertToViewObject(parent));
        }
        detailVo.setChildren(
                list(Wrappers.<Permission>query().eq(Permission.PID, detailVo.getId()))
                        .stream().map(permissionConvert::convertToViewObject).collect(Collectors.toList())
        );
        return detailVo;
    }

    @OperateLog(operateType = OperateType.DELETE)
    @Transactional
    @Override
    public void delete(DeletePermissionCmd cmd) {
        Permission permission = getById(cmd.getId());
        List<Api> apis = apiService.list(Wrappers.<Api>query().eq(Api.PERMISSION_CODE, permission.getCode()));
        if (CollectionUtils.isNotEmpty(apis)) {
            String names = apis.stream().map(Api::getName).collect(Collectors.joining(","));
            throw BizRuntimeException.from(ResultCode.ERROR, "该权限正在被接口 [" + names + "] 关联，无法删除");
        }
        List<Menu> menus = menuService.list(Wrappers.<Menu>query().eq(Menu.PERMISSION_ID, cmd.getId()));
        if (CollectionUtils.isNotEmpty(menus)) {
            String names = menus.stream().map(Menu::getName).collect(Collectors.joining(","));
            throw BizRuntimeException.from(ResultCode.ERROR, "该权限正在被菜单 [" + names + "] 关联，无法删除");
        }
        List<Permission> children = list(
                Wrappers.<Permission>query().eq(Permission.PID, permission.getId())
        );

        if (CollectionUtils.isNotEmpty(children)) {
            String names = children.stream().map(Permission::getName).collect(Collectors.joining(","));
            throw BizRuntimeException.from(ResultCode.ERROR, "该权限有子权限 [" + names + "] ，无法删除");
        }

        removeById(permission);
    }

}
