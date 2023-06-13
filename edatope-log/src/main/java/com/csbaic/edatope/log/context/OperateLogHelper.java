package com.csbaic.edatope.log.context;

import com.alibaba.fastjson.JSON;
import com.csbaic.edatope.auth.principal.AuthConstants;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.utils.IPUtils;
import com.csbaic.edatope.log.context.OperateContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class OperateLogHelper {

    public static final String COMMA = ",";

    /**
     * 保存当前请求
     */
    private static ThreadLocal<HttpServletRequest> requestCache = new ThreadLocal<>();

    /**
     * 持有当前线程的LogContext对象
     */
    public static ThreadLocal<OperateContext> contextHolder = new ThreadLocal<>();


    /**
     * 请求路径帮忙类
     */
    private static UrlPathHelper pathHelper = new UrlPathHelper();


    /**
     * 创建日志对象
     *
     * @return
     */
    public static OperateContext create(Object target) {
        OperateContext context = new OperateContext();
        //如果是匿名使用匿名名称
        if (PrincipalUtils.isAnonymous()) {
            context.setOperatorName(AuthConstants.PRINCIPAL_ANONYMOUS);
        } else {
            PrincipalUtils.get().ifPresent(details -> {
                context.setOperatorId(details.getId());
                if (details instanceof UserPrincipalDetails) {
                    context.setOperatorName(((UserPrincipalDetails) details).getUsername());
                }
            });
        }
        context.setTarget(target);
        //设置请求信息
        HttpServletRequest request = requestCache.get();
        if (request != null) {
            if (MapUtils.isNotEmpty(request.getParameterMap())) {
                Map<String, Object> objectMap = new HashMap<>();
                request.getParameterMap().forEach((s, strings) -> objectMap.put(s, strings.length == 1 ? strings[0] : strings));
                context.setRequestParam(JSON.toJSONString(objectMap));
            }
            context.setRequestAddr(IPUtils.getIpAddr(request));
            context.setRequestPath(pathHelper.getLookupPathForRequest(request));
            ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                try {
                    context.setRequestBody(new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding()));
                } catch (UnsupportedEncodingException ignore) {

                }
            }
        }

        return context;
    }

    /**
     * 创建日志对象
     *
     * @return
     */
    public static OperateContext create() {
        return create(null);
    }


    /**
     * 获取当前线程的日志上下文
     *
     * @return
     */
    public static Optional<OperateContext> get() {
        return Optional.ofNullable(contextHolder.get());
    }

    /**
     * 设置日志操作对象
     *
     * @param target
     * @return
     */
    public static void setOperateTarget(Object target) {
        get().ifPresent(context -> context.setTarget(target));
    }

    /**
     * 设置日志上下文
     *
     * @param context
     */
    static void set(OperateContext context) {
        contextHolder.set(context);
    }

    /**
     * 保存请求
     *
     * @param request
     */
    static void saveRequest(HttpServletRequest request) {
        requestCache.set(request);
    }

    /**
     * 移除请求
     */
    static void removeRequest() {
        requestCache.set(null);
    }

}
