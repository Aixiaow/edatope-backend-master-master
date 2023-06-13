package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DeleteApiGroupCmd {

    @ApiModelProperty("接口分组id")
    @NotEmpty(message = "接口分组不能为空")
    private String id;

}
