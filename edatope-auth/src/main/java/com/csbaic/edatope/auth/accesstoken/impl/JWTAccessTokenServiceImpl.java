package com.csbaic.edatope.auth.accesstoken.impl;

import com.csbaic.edatope.auth.accesstoken.AccessToken;
import com.csbaic.edatope.auth.accesstoken.AccessTokenException;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.accesstoken.TokenRequest;
import com.google.common.base.Strings;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class JWTAccessTokenServiceImpl implements AccessTokenService {

    /**
     * jwt claim key
     */
    private static final String CLAIM_PRINCIPAL = "principal";


    /**
     * jwt claim key
     */
    private static final String CLAIM_PRINCIPAL_TYPE = "principal_type";

    /**
     * jwt claim key
     */
    private static final String CLAIM_PRINCIPAL_ORG_ID = "principal_org_id";

    /**
     * 签名密钥
     */
    private final String keyValue;

    /**
     * 生成算法
     */
    private final String algorithm;

    /**
     * 令牌过期时间（秒）
     */
    private final Integer expireIn;

    public JWTAccessTokenServiceImpl(String keyValue, String algorithm, Integer expireIn) {
        this.keyValue = keyValue;
        this.algorithm = algorithm;
        this.expireIn = expireIn;
    }

    @Override
    public String create(TokenRequest request) throws AccessTokenException   {

        try {
            JWSSigner signer = new MACSigner(keyValue);

            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject("csbaic")
                    .issuer("https://www.csbaic.com/")
                    .expirationTime(expireIn != null ? new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireIn)) : null);

            //写入主体
            claimsSetBuilder.claim(CLAIM_PRINCIPAL, request.getPrincipal());
            claimsSetBuilder.claim(CLAIM_PRINCIPAL_TYPE, request.getPrincipalType());
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.parse(algorithm)), claimsSetBuilder.build());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AccessTokenException(e);
        }
    }

    @Override
    public void verify(String token) throws AccessTokenException {

    }

    @Override
    public AccessToken resolve(String token) throws AccessTokenException {
        if(Strings.isNullOrEmpty(token)){
            throw new AccessTokenException("token == null");
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(keyValue);
            signedJWT.verify(verifier);

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            // TODO: 2020/2/8 检查是超时
            Map<String, Object> claims =  claimsSet.getClaims();
            AccessToken accessToken = new AccessToken();
            accessToken.setPrincipal(claims.get(CLAIM_PRINCIPAL));
            accessToken.setPrincipalType(claims.containsKey(CLAIM_PRINCIPAL_TYPE) ? claims.get(CLAIM_PRINCIPAL_TYPE).toString() : null );
            return accessToken;
        } catch (JOSEException | ParseException e) {
            throw new AccessTokenException(e);
        }
    }


}
