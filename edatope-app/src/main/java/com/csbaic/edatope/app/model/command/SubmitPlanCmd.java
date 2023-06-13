package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.model.dto.TasksCmd;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitPlanCmd {

    @ApiModelProperty("任务阶段ID集合")
    private List<String> blockWorkStageId;

}
