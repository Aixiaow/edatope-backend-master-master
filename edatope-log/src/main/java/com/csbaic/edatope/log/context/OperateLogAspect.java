package com.csbaic.edatope.log.context;

import com.csbaic.edatope.common.utils.EnvUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.service.OperateLogHandler;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class OperateLogAspect {


    private final OperateLogHandler handler;


    public OperateLogAspect(OperateLogHandler handler) {
        this.handler = handler;

    }

    @Pointcut("@annotation(com.csbaic.edatope.log.annotation.OperateLog)")
    public void operateLog() {
    }


    /**
     * 调用之后处理日志
     *
     * @param point
     */
    @Around("operateLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperateLog operateLog = method.getAnnotation(OperateLog.class);

        try {
            OperateContext context = OperateLogHelper.create();
            OperateLogHelper.set(context);
            //设置请求参数
            context.setLogType(operateLog.logType().name());
            context.setModule(StringUtils.isNotBlank(operateLog.module()) ? operateLog.module() : EnvUtils.resolveModuleName(point.getTarget().getClass()));
            context.setOperateType(operateLog.operateType().name());
            if (StringUtils.isNotBlank(operateLog.remark())) {
                context.setRemark(operateLog.remark());
            } else {
                context.setRemark(operateLog.operateType().getRemark());
            }
            Object returnObject = point.proceed();
            handler.handle(context);
            return returnObject;
        } finally {
            OperateLogHelper.set(null);
        }

    }
}
