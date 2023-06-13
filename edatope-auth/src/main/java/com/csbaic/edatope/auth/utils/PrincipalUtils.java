package com.csbaic.edatope.auth.utils;

import com.csbaic.edatope.auth.principal.AuthConstants;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;


import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class PrincipalUtils {

    /**
     * 获取当前主体详情
     * @return
     */
    public static Optional<PrincipalDetails> get() {
        Subject subject = ThreadContext.getSubject();
        if (subject == null || isAnonymous()) {
            return Optional.empty();
        }

        return Optional.ofNullable((PrincipalDetails) subject.getPrincipal());
    }

    /**
     * 获取当前主体详情
     *
     * @return
     */
    public static PrincipalDetails getOrThrow() {
        return get().orElseThrow(() -> BizRuntimeException.from(ResultCode.ERROR, "当前没有用户"));
    }


    /**
     * 判断是否是匿名用户
     *
     * @return
     */
    public static boolean isAnonymous() {
        Subject subject = ThreadContext.getSubject();
        return subject == null || Objects.equals(subject.getPrincipal(), AuthConstants.PRINCIPAL_ANONYMOUS);
    }

}
