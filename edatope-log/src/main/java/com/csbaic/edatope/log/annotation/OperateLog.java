package com.csbaic.edatope.log.annotation;

import com.csbaic.edatope.log.constants.LogConstants;
import com.csbaic.edatope.log.enums.LogType;
import com.csbaic.edatope.log.enums.OperateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    /**
     * 业务模块
     *
     * @return
     */
    String module() default "";


    /**
     * 日志类型
     *
     * @return
     */
    LogType logType() default LogType.OPERATE;

    /**
     * 操作类型
     *
     * @return
     */
    OperateType operateType() default OperateType.NONE;


    /**
     * 备注
     *
     * @return
     */
    String remark() default "";


}
