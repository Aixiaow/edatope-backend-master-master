package com.csbaic.edatope.auth.accesstoken;

import lombok.Data;

/**
 * 访问令牌实体
 */
@Data
public class AccessToken {

    /**
     * 主体
     */
    private Object principal;

    /**
     * 主体类型
     */
    private String principalType;

    /**
     * 令牌
     */
    private String token;

    /**
     * 组织id
     */
    private String orgId;
}
