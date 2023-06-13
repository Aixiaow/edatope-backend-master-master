package com.csbaic.edatope.auth.accesstoken;

import java.util.Map;

/**
 * Created by yjwfn on 2020/2/13.
 */
public interface AccessTokenService {

    /**
     * 生成accessToken
     * @param request
     * @return
     */
    String create(TokenRequest request)  throws AccessTokenException;


    /**
     * token校验
     * @param token
     * @throws AccessTokenException
     */
    void verify(String token) throws AccessTokenException ;

    /**
     * 解析AccessToken
     * @param token
     * @return
     */
    AccessToken resolve(String token) throws AccessTokenException;

}
