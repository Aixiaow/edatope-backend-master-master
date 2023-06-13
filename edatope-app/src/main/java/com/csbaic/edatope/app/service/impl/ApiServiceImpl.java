package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.utils.TreeListUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.enums.OperateType;
import com.csbaic.edatope.app.convert.ApiConvert;
import com.csbaic.edatope.app.entity.Api;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.app.mapper.ApiMapper;
import com.csbaic.edatope.app.model.command.CreateApiCmd;
import com.csbaic.edatope.app.model.command.DeleteApiCmd;
import com.csbaic.edatope.app.model.command.UpdateApiCmd;
import com.csbaic.edatope.app.model.dto.ApiDTO;
import com.csbaic.edatope.app.service.IApiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.service.IPermissionService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.google.common.base.Strings;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统接口表 服务实现类
 * </p>
 * @author bage
 * @since 2021-12-15
 */
@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements IApiService {

    @Autowired
    private ApiConvert convert;
    @Autowired
    private IPermissionService permissionService;

    @Override
    public Api getMatchedApi(String path) {
        Api api = getOne(
                Wrappers.<Api>query().eq(Api.PATH, path)
        );
        if(api == null){
            return null;
        }

        if(api.getPermissionCode() != null){
            api.setPermission(permissionService.getByCode(api.getPermissionCode()));
        }

        return api;
    }

    @OperateLog(operateType = OperateType.CREATE)
    @Transactional
    @Override
    public void create(CreateApiCmd cmd) {
        Api entity = convert.convertToEntity(cmd);
        if (!Strings.isNullOrEmpty(cmd.getPermissionCode())) {
            Permission permission = permissionService.getByCode(cmd.getPermissionCode());
            if (permission == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到权限");
            }
        }

        if (StringUtils.isNotBlank(cmd.getGid())) {
            Api api = getById(cmd.getGid());
            if (api == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级接口");
            }
            if (!api.getApiGroup()) {
                throw BizRuntimeException.from(ResultCode.ERROR, "上级接口不是接口组，不能添加接口");
            }
            entity.setGid(api.getId());
        }
        entity.setStatus(EnableStatus.ENABLED.name());
        save(entity);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<ApiDTO> listApi() {
        List<Api> apis = list(Wrappers.<Api>query().orderByAsc(Api.SORT));
        List<Api> root = new ArrayList<>();
        TreeListUtils.tree(apis, Api::getId, Api::getGid, new BiConsumer<Api, Api>() {
            @Override
            public void accept(Api parent, Api child) {
                if (parent == null) {
                    root.add(child);
                    return;
                }

                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(child);
            }
        });
        return root.stream().map(convert::convertToDTO).collect(Collectors.toList());
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public ApiDTO getApiDetail(String id) {
        Api entity = getById(id);
        if (entity == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "接口不存在");
        }
        //获取权限
        if (StringUtils.isNotBlank(entity.getPermissionCode())) {
            entity.setPermission(permissionService.getByCode(entity.getPermissionCode()));
        }
        //获取子接口
        if (entity.getApiGroup()) {
            entity.setChildren(new ArrayList<>());
            List<Api> children = list(Wrappers.<Api>query().likeRight(Api.GID, entity.getId()));
            TreeListUtils.tree(children, Api::getId, Api::getGid, (parent, child) -> {
                if (parent == null) {
                    entity.getChildren().add(child);
                } else {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(child);
                }
            });
        }
        return convert.convertToDTO(entity);
    }

    @OperateLog(operateType = OperateType.UPDATE)
    @Transactional
    @Override
    public void update(UpdateApiCmd cmd) {
        Api api = getById(cmd.getId());
        if (!Strings.isNullOrEmpty(cmd.getPermissionCode())) {
            Permission permission = permissionService.getByCode(cmd.getPermissionCode());
            if (permission == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到权限");
            }
            api.setPermissionCode(permission.getId());
        }

        if (StringUtils.isNotBlank(cmd.getGid())) {
            Api apiGroup = getById(cmd.getGid());
            if (apiGroup == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到接口组");
            }
            if (!apiGroup.getApiGroup()) {
                throw BizRuntimeException.from(ResultCode.ERROR, "接口不是接口组，不能添加接口");
            }
            api.setGid(apiGroup.getId());
        }

        BeanCopyUtils.copyNonNullProperties(cmd, api);
        updateById(api);
    }

    @OperateLog(operateType = OperateType.DELETE)
    @Override
    public void delete(DeleteApiCmd cmd) {
        Api api = getById(cmd.getId());
        if (api == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到接口");
        }
        List<Api> children = list(Wrappers.<Api>query().eq(Api.GID, api.getId()));
        if (CollectionUtils.isNotEmpty(children)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该接口是接口组，请先删除下级接口");
        }

        removeById(cmd.getId());
    }
}
