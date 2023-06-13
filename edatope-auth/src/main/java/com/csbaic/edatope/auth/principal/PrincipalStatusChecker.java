package com.csbaic.edatope.auth.principal;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 检查主体的状态
 */
public interface PrincipalStatusChecker {

    /**
     * 检查principal状态
     * @param principal
     * @throws AuthenticationException
     */
    void check(PrincipalDetails principal) throws AuthenticationException;
}
