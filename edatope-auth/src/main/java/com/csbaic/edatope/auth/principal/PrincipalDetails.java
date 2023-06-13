package com.csbaic.edatope.auth.principal;

import java.util.Set;

public interface PrincipalDetails     {

    /**
     * 用户id
     * @return
     */
    String getId();


    /**
     * 主体的权限
     * @return
     */
    Set<String> getStringPermissions();

    /**
     * 主体的角色
     * @return
     */
    Set<String> getRoles();

    /**
     * 主体的状态
     * @return
     */
    String getStatus();

}
