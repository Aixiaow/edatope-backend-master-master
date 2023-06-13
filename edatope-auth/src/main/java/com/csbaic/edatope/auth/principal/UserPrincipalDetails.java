package com.csbaic.edatope.auth.principal;

/**
 * 用户主体
 */
public interface UserPrincipalDetails extends PrincipalDetails {


    /**
     * 用户名称
     *
     * @return
     */
    String getUsername();

    /**
     * 用户名称
     *
     * @return
     */
    String getNickName();


    /**
     * 是否是管理员
     *
     * @return
     */
    Boolean getAdmin();


    /**
     * 获取用户的单位id
     *
     * @return
     */
    String getOrgId();
}
