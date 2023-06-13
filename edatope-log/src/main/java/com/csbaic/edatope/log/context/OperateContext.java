package com.csbaic.edatope.log.context;


import lombok.Data;

@Data
public class OperateContext {

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 操作对象
     */
    private Object target;

    /**
     * 操作对象id
     */
    private String targetId;

    /**
     * 操作对象类型
     */
    private String targetName;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求参数实体
     */
    private String requestBody;

    /**
     * 请求id地址
     */
    private String requestAddr;

    /**
     * 请求路径
     */
    private String requestPath;


    /**
     * 日志备注
     */
    private String remark;


}
