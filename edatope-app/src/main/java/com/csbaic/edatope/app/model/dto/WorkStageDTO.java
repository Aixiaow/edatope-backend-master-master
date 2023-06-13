package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkStageDTO {

    private String id;

    @ApiModelProperty("工作阶段名称")
    private String name;

    @ApiModelProperty("工作阶段描述")
    private String stageDesc;

    @ApiModelProperty("工作阶段描述") 
    private String status;

    @ApiModelProperty("任务阶段")
    private List<String> bizType;

    @ApiModelProperty("任务阶段描述")
    private List<String> bizTypeDesc;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
