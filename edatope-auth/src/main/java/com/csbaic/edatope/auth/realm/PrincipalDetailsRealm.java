package com.csbaic.edatope.auth.realm;

import com.csbaic.edatope.auth.principal.AuthConstants;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.PrincipalDetailsService;
import com.csbaic.edatope.auth.principal.PrincipalStatusChecker;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Objects;

/**
 * 处理PrincipalDetails
 */
public class PrincipalDetailsRealm extends AuthorizingRealm {

    /**
     * 主体服务
     */
    private PrincipalDetailsService detailsService;

    /**
     * 主体状态检测
     */
    private PrincipalStatusChecker statusChecker;

    public PrincipalDetailsRealm(PrincipalDetailsService detailsService, PrincipalStatusChecker statusChecker) {
        this.detailsService = detailsService;
        this.statusChecker = statusChecker;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (Objects.equals(principals.getPrimaryPrincipal(), AuthConstants.PRINCIPAL_ANONYMOUS)) {
            //匿名用户直接返回
            return new SimpleAuthorizationInfo();
        }

        PrincipalDetails principalDetails = (PrincipalDetails) principals.getPrimaryPrincipal();
        //检查状态
        statusChecker.check(principalDetails);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //添加权限
        if (CollectionUtils.isNotEmpty(principalDetails.getStringPermissions())) {
            principalDetails.getStringPermissions().forEach(authorizationInfo::addStringPermission);
        }
        //添加角色
        if (CollectionUtils.isNotEmpty(principalDetails.getRoles())) {
            principalDetails.getRoles().forEach(authorizationInfo::addRole);
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (!(token instanceof BearerToken)) {
            throw new AuthenticationException("not support token");
        }
        BearerToken bearerToken = (BearerToken) token;
        String principal = (String) bearerToken.getPrincipal();
        //为空的使用，返回匿名登陆
        if (Objects.isNull(principal) || Strings.isNullOrEmpty(principal)) {
            return new SimpleAuthenticationInfo(AuthConstants.PRINCIPAL_ANONYMOUS , "", getName());
        }

        PrincipalDetails details = detailsService.getPrincipalDetails(principal);
        if(details == null){
            return new SimpleAuthenticationInfo(AuthConstants.PRINCIPAL_ANONYMOUS , "", getName());
        }

        //检查状态
        if(statusChecker != null){
            statusChecker.check(details);
        }

        return new SimpleAuthenticationInfo(details, "", getName());
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        return;
    }
}
