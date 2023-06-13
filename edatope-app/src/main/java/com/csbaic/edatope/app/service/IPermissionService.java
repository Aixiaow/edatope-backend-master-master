package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.model.command.DeletePermissionCmd;
import com.csbaic.edatope.app.model.command.CreatePermissionCmd;
import com.csbaic.edatope.app.model.query.PermissionQuery;
import com.csbaic.edatope.app.model.dto.PermissionDTO;
import com.csbaic.edatope.app.model.command.UpdatePermissionCmd;
import com.csbaic.edatope.app.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface IPermissionService extends IService<Permission> {

    Permission getByCode(String code);

    /**
     * 创建权限
     *
     * @param cmd
     * @return
     */
    void create(CreatePermissionCmd cmd);

    /**
     * 更新权限
     *
     * @param cmds
     * @return
     */
    void update(UpdatePermissionCmd cmds);

    /**
     * 查询权限
     *
     * @param
     * @return
     */
    List<PermissionDTO> list(PermissionQuery query);

    /**
     * 获取权限详情
     * @param permissionId
     * @return
     */
    PermissionDTO getPermissionDetail(String permissionId);


    /**
     * 批量删除权限
     *
     * @param cmd
     * @return
     */
    void delete(DeletePermissionCmd cmd);
}
