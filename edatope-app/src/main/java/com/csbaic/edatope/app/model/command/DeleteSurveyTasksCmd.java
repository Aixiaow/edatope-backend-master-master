package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteSurveyTasksCmd {

    @ApiModelProperty("任务分配id")
    @NotEmpty(message = "任务分配id不能为空")
    private String id;

}
