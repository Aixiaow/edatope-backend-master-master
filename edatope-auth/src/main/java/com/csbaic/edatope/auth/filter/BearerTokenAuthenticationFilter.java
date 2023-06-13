package com.csbaic.edatope.auth.filter;

import com.csbaic.edatope.auth.principal.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Objects;

@Slf4j
public class BearerTokenAuthenticationFilter extends AuthenticatingFilter {

    /**
     * 默认token
     */
    private static final BearerToken DEFAULT_BEARER_TOKEN = new BearerToken("");

    private BearerTokenResolver resolver = new BearerTokenResolver();

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        BearerToken token = resolver.resolve(request);
        return token != null ? token : DEFAULT_BEARER_TOKEN;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return super.isAccessAllowed(request, response, mappedValue) && !Objects.equals(AuthConstants.PRINCIPAL_ANONYMOUS, SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        /*
            发起登陆
         */
        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //不管是认证成功不审认证失败，都继续处理
        log.error("onLoginFailure：", e);
        return true;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        //不管是认证成功不审认证失败，都继续处理
        return true;
    }
}
