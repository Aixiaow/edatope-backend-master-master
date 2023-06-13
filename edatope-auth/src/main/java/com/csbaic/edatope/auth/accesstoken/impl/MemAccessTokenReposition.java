package com.csbaic.edatope.auth.accesstoken.impl;

import com.csbaic.edatope.auth.accesstoken.AccessToken;
import com.csbaic.edatope.auth.accesstoken.AccessTokenReposition;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MemAccessTokenReposition implements AccessTokenReposition {

    /**
     * 保存token
     */
    private Map<Object, AccessToken> map = new ConcurrentHashMap<>();


    @Override
    public void save(AccessToken accessToken) {
        String fullKey = accessToken.getPrincipal() + ":" + accessToken.getPrincipalType() + accessToken.getToken();
        String tokenKey = accessToken.getToken();
        String principalKey = accessToken.getPrincipal() + "";
        map.put(fullKey,accessToken);
        map.put(tokenKey, accessToken);
        map.put(principalKey, accessToken);
    }

    @Override
    public AccessToken getByToken(String token) {
        return map.get(token);
    }

    @Override
    public AccessToken getByPrincipal(Object principal) {
        return map.get(principal);
    }
}
