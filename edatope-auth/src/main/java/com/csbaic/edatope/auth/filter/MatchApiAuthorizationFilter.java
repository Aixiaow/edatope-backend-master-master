package com.csbaic.edatope.auth.filter;

import com.csbaic.edatope.auth.api.ApiPrincipalDetails;
import com.csbaic.edatope.auth.api.ApiPrincipalDetailsService;
import com.csbaic.edatope.auth.principal.AuthConstants;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.result.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class MatchApiAuthorizationFilter extends AccessControlFilter implements InitializingBean {

    /**
     * 鉴权消息
     */
    private static final String ATTR_AUTHORIZING_MESSAGE = "attr_authorizing_message";
    /**
     * 查询接口的权限信息
     */
    private final ApiPrincipalDetailsService apiPrincipalDetailsService;


    private final ObjectMapper mapper = new ObjectMapper();

    public MatchApiAuthorizationFilter(ApiPrincipalDetailsService apiPrincipalDetailsService) {
        this.apiPrincipalDetailsService = apiPrincipalDetailsService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        String path = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
        ApiPrincipalDetails details = apiPrincipalDetailsService.getPrincipalDetails(path);
        //没有获取到接口的配置
        if (details == null) {
            request.setAttribute(ATTR_AUTHORIZING_MESSAGE, "没有匹配到请求的接口，请配置接口信息");
            return false;
        }

        if (Objects.equals(details.getStatus(), EnableStatus.DISABLED.name())) {
            request.setAttribute(ATTR_AUTHORIZING_MESSAGE, "接口已被禁用");
            return false;
        }

        //支持匿名的接口，直接返回
        if (details.getAnonymous()) {
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        //未认证不请允许访问
        if (subject == null || !subject.isAuthenticated() || Objects.equals(subject.getPrincipal(), AuthConstants.PRINCIPAL_ANONYMOUS)) {
            request.setAttribute(ATTR_AUTHORIZING_MESSAGE, "未认证，不允许访问");
            return false;
        }

        /*
            超级管理员，可以访问任何内容
         */
        if(subject.getPrincipal() instanceof UserPrincipalDetails){
            if(((UserPrincipalDetails) subject.getPrincipal()).getAdmin()){
                return true;
            }
        }

        if(CollectionUtils.isNotEmpty(details.getStringPermissions())){
            for(String perm : details.getStringPermissions()){
                if (!subject.isPermitted(perm)) {
                    request.setAttribute(ATTR_AUTHORIZING_MESSAGE, "没有 [" + perm + "] 权限，请联系管理员");
                    return false;
                }
            }
        }

        if (CollectionUtils.isNotEmpty(details.getRoles())) {
            for(String role: details.getRoles()){
                if (!subject.hasRole(role)) {
                    request.setAttribute(ATTR_AUTHORIZING_MESSAGE, "没有 [" + role + "] 角色，请联系管理员");
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        Object message = request.getAttribute(ATTR_AUTHORIZING_MESSAGE);
        Result result = Result.error(ResultCode.ERROR.getCode(), message != null ? String.valueOf(message) : "");
        Subject subject = getSubject(request, response);
        //认证成功的返回403（拒绝访问），未认证的返回401（未认证）
        int status = subject == null
                || !subject.isAuthenticated()
                || AuthConstants.PRINCIPAL_ANONYMOUS.equals(subject.getPrincipal()) ? 401 : 403;
        String responseBody = mapper.writeValueAsString(result);
        httpServletResponse.setStatus(status);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpServletResponse.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(responseBody);
        writer.close();
        return false;
    }


}
