package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.model.dto.TasksCmd;
import com.csbaic.edatope.app.model.dto.TasksDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SurveyTasksCmd {

    @ApiModelProperty("工作任务阶段id")
    private String blockWorkStageId;

    @ApiModelProperty("委托方式，字典值：entrust_way")
    private String entrustWay;

    @ApiModelProperty("任务信息集合")
    private List<TasksCmd> tasksList;

}
