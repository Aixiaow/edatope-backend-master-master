package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteOrgAuthorizeCmd {

    @ApiModelProperty("授权记录id")
    private String id;

}
