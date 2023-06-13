package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateAuthorizeStatusCmd {

    @ApiModelProperty("授权记录id")
    private String id;

    @ApiModelProperty("是否启用")
    private Boolean enabled;
}
