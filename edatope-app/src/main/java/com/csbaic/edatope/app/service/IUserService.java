package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.model.command.CreateUserCmd;
import com.csbaic.edatope.app.model.command.DeleteUserCmd;
import com.csbaic.edatope.app.model.command.ResetPasswordCmd;
import com.csbaic.edatope.app.model.command.UpdateUserCmd;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.UserQuery;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
public interface IUserService extends IService<User> {

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    Set<String> getStringRoleForUser(String userId);

    /**
     * 获取用户的所有权限
     *
     * @return
     */
    Set<String> getStringPermissionForUser(String userId);


    /**
     * 根据登陆用户名或者手机号获取用户
     *
     * @param text
     * @return
     */
    User getByUsernameOrMobile(String text);


    /**
     * 创建用户
     *
     * @param cmd
     */
    void create(CreateUserCmd cmd);

    /**
     * 更新用户
     *
     * @param cmd
     */
    void update(UpdateUserCmd cmd);

    /**
     * 删除用户
     *
     * @param cmd
     */
    void delete(DeleteUserCmd cmd);

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    UserDTO getUserDetail(String userId);

    /**
     * 获取用户详情
     *
     * @param userId
     * @return
     */
    UserDTO getSimpleUser(String userId);

    /**
     * 用户列表
     *
     * @param query
     * @return
     */
    IPage<UserDTO> listUserPage(UserQuery query);

    /**
     * 获取单位的用户
     * @param orgId
     * @return
     */
    List<UserDTO> listAllUserByOrgId(String orgId);

    List<UserDTO> listAllUserByOrgIdLikeName(String orgId, String name);
    /**
     * 重置密码
     *
     * @param cmd
     */
    void resetPassword(ResetPasswordCmd cmd);

    /**
     * 单位管理员，防止多个所以查list
     * @param orgId
     * @return
     */
    List<UserDTO> adminUserList(String orgId, String type);
    /**
     * 获取单位管理员
     * @param orgId
     * @return
     */
    UserDTO getOrganizationAdmin(String orgId);

    UserDTO convertUserDTO(User user);
}
