package com.csbaic.edatope.log.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogContextFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpServletRequest wrapperReq = request instanceof ContentCachingRequestWrapper ? request : new ContentCachingRequestWrapper(request);
            //保存请求
            OperateLogHelper.saveRequest(wrapperReq);
            filterChain.doFilter(wrapperReq, response);
        } finally {
            OperateLogHelper.removeRequest();
        }
    }


}
