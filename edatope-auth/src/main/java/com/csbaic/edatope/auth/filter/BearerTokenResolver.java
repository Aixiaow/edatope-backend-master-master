package com.csbaic.edatope.auth.filter;

import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class BearerTokenResolver {

    /**
     * Header name
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Bearer
     */
    private static final String BEARER = "Bearer";



    /**
     * 提取token
     * @param request
     * @return
     */
    public BearerToken resolve(ServletRequest request){
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String value  =  httpRequest.getHeader(AUTHORIZATION_HEADER);
        if(value == null || Objects.equals("", value.trim())){
            return null;
        }

        String[] split = value.split(" ");
        if(split.length != 2){
            return null;
        }

        if(!BEARER.equalsIgnoreCase(split[0])){
            return null;
        }

        return new BearerToken(split[1], request.getRemoteHost());
    }
}
