package com.csbaic.edatope.log.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperateLogListItemDTO {

    /**
     * 日志id
     */
    @ApiModelProperty("日志id")
    private String id;


    /**
     * 日志类型（登陆日志，操作日志，请求日志，错误日志）
     */
    @ApiModelProperty(" 日志类型（登陆日志，操作日志，请求日志，错误日志）")
    private String logType;

    /**
     * 操作日志类型文本
     */
    @ApiModelProperty("操作日志类型文本")
    private String logTypeDesc;

    /**
     * 所属模块
     */
    @ApiModelProperty("模拟名称")
    private String module;

    /**
     * 操作人姓名
     */
    @ApiModelProperty("操作人名称")
    private String operatorName;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private String operatorId;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operateType;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operateTypeDesc;

    /**
     * 日志备注
     */
    @ApiModelProperty("日志备注")
    private String remark;


    /**
     * 关联目标id
     */
    @ApiModelProperty("关联目标id")
    private String targetId;

    /**
     * 操作对象名称
     */
    @ApiModelProperty("操作对象名称")
    private String targetName;

    /**
     * 操作对象名称
     */
    @ApiModelProperty("操作对象名称描述")
    private String targetNameDesc;


    /**
     * 操作时间
     */
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime operateTime;

}
