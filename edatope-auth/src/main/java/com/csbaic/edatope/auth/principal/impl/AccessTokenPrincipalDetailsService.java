package com.csbaic.edatope.auth.principal.impl;

import com.csbaic.edatope.auth.accesstoken.AccessToken;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.PrincipalDetailsService;
import com.google.common.base.Strings;

public abstract class AccessTokenPrincipalDetailsService implements PrincipalDetailsService {

    /**
     * 令牌服务
     */
    private final AccessTokenService accessTokenService;


    public AccessTokenPrincipalDetailsService(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public PrincipalDetails getPrincipalDetails(Object principal) {
        if (principal == null || Strings.isNullOrEmpty(principal.toString())) {
            return null;
        }

        String token = principal.toString();
        AccessToken accessToken = accessTokenService.resolve(token);
        return doGetPrincipalDetails(accessToken);
    }

    /**
     * 根据AccessToken获取主体的详细信息
     * @param accessToken
     * @return
     */
    protected abstract PrincipalDetails doGetPrincipalDetails(AccessToken accessToken);
}
