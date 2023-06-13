package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteRoleCmd {

    @ApiModelProperty("角色id")
    @NotEmpty(message = "角色id不能为空")
    private String id;

}
