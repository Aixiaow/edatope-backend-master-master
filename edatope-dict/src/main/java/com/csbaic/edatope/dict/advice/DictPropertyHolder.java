package com.csbaic.edatope.dict.advice;

import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.databind.introspect.Annotated;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Data
public class DictPropertyHolder {


    /**
     * 字典类型
     */
    private String type;
    /**
     * 字典的数据项的值
     */
    private Field valueField;

    /**
     * 回写数据项名称的field
     */
    private Field nameField;
}
