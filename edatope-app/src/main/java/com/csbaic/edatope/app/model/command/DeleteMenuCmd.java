package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteMenuCmd {

    @ApiModelProperty("菜单id")
    @NotEmpty(message = "菜单id不能为空")
    private String id;

}
