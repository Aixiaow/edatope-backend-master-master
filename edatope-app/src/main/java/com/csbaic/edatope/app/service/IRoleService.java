package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.app.model.command.DeleteRoleCmd;
import com.csbaic.edatope.app.model.command.CreateRoleCmd;
import com.csbaic.edatope.app.model.command.UpdateRoleCmd;
import com.csbaic.edatope.app.model.query.RoleQuery;
import com.csbaic.edatope.app.model.dto.RoleDTO;
import com.csbaic.edatope.app.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface IRoleService extends IService<Role> {

    /**
     * 创建角色
     * @param cmd
     * @return
     */
     void createRole(CreateRoleCmd cmd);

    /**
     * 获取角色详情
     * @param id
     * @return
     */
    RoleDTO getRoleDetail(String id);

    /**
     * 角色查询
     *
     * @param query
     * @return
     */
    IPage<RoleDTO> listRole(RoleQuery query);

    /**
     * 角色查询
     *
     * @return
     */
    List<RoleDTO> listAllRole();

    /**
     * 获取单位的角色
     * @param orgId
     * @return
     */
    List<RoleDTO> listRoleByOrgId(String orgId);

    /**
     * 批量更新角色
     *
     * @param cmd
     */
    void updateRole(UpdateRoleCmd cmd);

    /**
     * 批量删除
     *
     * @param cmd
     * @return
     */
    void deleteRole(DeleteRoleCmd cmd);

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUserId(String userId);

    /**
     * 获取用户的权限
     *
     * @param userId
     * @return
     */
    List<Permission> getPermissionsByUserId(String userId);


    /**
     * 获取角色的权限
     *
     * @param roleId
     * @return
     */
    List<Permission> getPermissionsByRoleId(String roleId);


    /**
     * 给角色添加权限
     *
     * @param permissionIds
     */
    void addPermissionsToRole(String roleId, List<String> permissionIds);

    /**
     * 移除角色的所有权限
     *
     * @param roleId
     */
    void removePermissionByRoleId(String roleId);


    /**
     * 移除角色的所有权限
     *
     * @param roleId
     */
    void removePermissionByRoleId(String roleId, List<String> permissionIds);


    /**
     * 为用户分配角色
     *
     * @param userId
     * @param roles
     */
    void assignRoleToUser(String userId, List<String> roles);



    /**
     * 为角色分配权限
     *
     * @param roleId
     * @param permissionIds
     */
    void assignPermissionToRole(String roleId, List<String> permissionIds);



    /**
     * 为用户分配角色
     *
     * @param userId
     * @param roles
     */
    void appendRoleToUser(String userId, List<String> roles);



    /**
     * 为用户分配角色
     *
     * @param userId
     * @param roles
     */
    void removeRoleFromUser(String userId, List<String> roles);


}
