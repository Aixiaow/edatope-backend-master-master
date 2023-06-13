package com.csbaic.edatope.log.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperateLogQuery extends PageQuery {

    /**
     * 日志类型
     */
    @ApiModelProperty("日志类型")
    private String logType;

    /**
     * 模块
     */
    @ApiModelProperty("模块")
    private String module;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operateType;

    /**
     * 操作对象id
     */
    @ApiModelProperty("操作对象id")
    private String targetId;

    /**
     * 开始操作时间
     */
    @ApiModelProperty("开始操作时间")
    private String beginOperateTime;

    /**
     * 结束操作时间
     */
    @ApiModelProperty("结束操作时间")
    private String endOperateTime;
}
