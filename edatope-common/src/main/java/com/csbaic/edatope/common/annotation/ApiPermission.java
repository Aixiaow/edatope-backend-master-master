package com.csbaic.edatope.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口权限说明
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiPermission {

    /**
     * 权限编码
     * @return
     */
    String value() default "";
    /**
     * 权限编码
     * @return
     */
    String code() default "";


    /**
     * 接口是否能够匿名访问
     * @return
     */
    boolean anonymous() default false;

}
