package com.csbaic.edatope.app.utils;

import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;

public final class UserUtils {

    /**
     * 获取当前用户
     * @return
     */
    public static UserPrincipalDetails getOrThrow(){
        return (UserPrincipalDetails) PrincipalUtils.getOrThrow();
    }

    /**
     * 获取当前用户的单位
     * @return
     */
    public static String getUserOrgId(){
        UserPrincipalDetails details = getOrThrow();
        if(details != null){
            return details.getOrgId();
        }
        return "";
    }

}
