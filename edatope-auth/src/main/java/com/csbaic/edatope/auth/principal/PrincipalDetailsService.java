package com.csbaic.edatope.auth.principal;

/**
 * 主体详情服务
 */
public interface PrincipalDetailsService {

    /**
     * 获取主体的详细信息
     * @param principal 标识主体的参数
     * @return
     */
    PrincipalDetails getPrincipalDetails(Object principal);

}
