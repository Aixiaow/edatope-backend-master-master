package com.csbaic.edatope.auth.api;

import com.csbaic.edatope.auth.principal.PrincipalDetails;

public interface ApiPrincipalDetails extends PrincipalDetails {

    /**
     * 判断接口是否支持匿名访问
     * @return
     */
    Boolean getAnonymous();

}
