package com.csbaic.edatope.auth.api;

/**
 * 获取接口的权限信息s
 */
public interface ApiPrincipalDetailsService    {

    /**
     * 获取接口的详细信息
     * @param principal 标识接口的参数
     * @return
     */
    ApiPrincipalDetails getPrincipalDetails(Object principal);

}
