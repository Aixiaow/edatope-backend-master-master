package com.csbaic.edatope.auth.accesstoken;

/**
 * token存储
 */
public interface AccessTokenReposition {


    /**
     * 保存token
     * @param accessToken
     */
    void save(AccessToken accessToken);

    /**
     * 获取Token
     * @param token
     * @return
     */
    AccessToken getByToken(String token);

    /**
     * 使用主体获取token
     * @param principal
     * @return
     */
    AccessToken getByPrincipal(Object principal);
}

