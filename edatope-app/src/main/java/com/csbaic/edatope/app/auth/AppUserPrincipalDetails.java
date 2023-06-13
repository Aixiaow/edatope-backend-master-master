package com.csbaic.edatope.app.auth;

import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.principal.impl.SimplePrincipalDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppUserPrincipalDetails extends SimplePrincipalDetails implements UserPrincipalDetails {

    /**
     * 用户组织id
     */
    private String orgId;

    /**
     * 是否是超级管理员
     */
    private Boolean admin;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String mobile;
}
