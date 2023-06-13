package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RectifySubmitPlanCmd {

    @ApiModelProperty("布点质控任务ID集合")
    private List<String> qualityControlTasksId;

}
