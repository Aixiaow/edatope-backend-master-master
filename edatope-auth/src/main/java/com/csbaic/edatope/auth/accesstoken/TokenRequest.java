package com.csbaic.edatope.auth.accesstoken;

import lombok.Data;

@Data
public class TokenRequest {

    /**
     * 主体
     */
    private Object principal;

    /**
     * 主体类型
     */
    private String principalType;

    /**
     * 主体组织id
     */
    private String orgId;

}
