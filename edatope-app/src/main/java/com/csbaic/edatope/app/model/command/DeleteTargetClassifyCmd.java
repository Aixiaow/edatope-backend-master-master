package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteTargetClassifyCmd {

    @ApiModelProperty("检测指标分类id")
    @NotEmpty(message = "检测指标分类id不能为空")
    private String id;

}
