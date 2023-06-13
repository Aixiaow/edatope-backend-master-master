package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TasksCmd {
    private String id;
    
    @ApiModelProperty("任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate deadline;

    @ApiModelProperty("单位id")
    private String orgId;

    @ApiModelProperty("负责人id")
    private String principal;

    @ApiModelProperty("负责人手机")
    private String principalPhone;

    @ApiModelProperty("类型")
    private String type;
}
