package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CreateOrgAuthorizeCmd {

    @ApiModelProperty("单位id")
    private String orgId;

    @ApiModelProperty("负责人id")
    private String userId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("分配区域code")
    private Set<String> areaCodeList;
}
