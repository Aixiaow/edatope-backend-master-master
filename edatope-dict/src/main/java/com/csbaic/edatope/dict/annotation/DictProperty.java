package com.csbaic.edatope.dict.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典数据项
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DictProperty {

    /**
     * 字典类型
     * @return
     */
    String type();

    /**
     * 字典数据值属性名称
     * @return
     */
    String value();
}
