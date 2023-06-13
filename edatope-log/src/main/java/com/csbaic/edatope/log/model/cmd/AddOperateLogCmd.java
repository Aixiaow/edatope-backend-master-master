package com.csbaic.edatope.log.model.cmd;

import lombok.Data;

/**
 * 添加操作日志
 */
@Data
public class AddOperateLogCmd {
    /**
     * 日志类型（登陆日志，操作日志，请求日志，错误日志）
     */
    private String logType;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 日志备注
     */
    private String remark;

    /**
     * 操作对象内容快照
     */
    private String targetSnapshot;

    /**
     * 关联目标id
     */
    private String targetId;

    /**
     * 操作对象
     */
    private String targetContent;

    /**
     * 操作对象名称
     */
    private String targetName;

    /**
     * 操作人ip地址
     */
    private String ip;

}
