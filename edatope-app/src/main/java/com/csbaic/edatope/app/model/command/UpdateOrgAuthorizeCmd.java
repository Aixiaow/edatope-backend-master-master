package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.annotation.ApiPermission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UpdateOrgAuthorizeCmd {

    @ApiModelProperty("记录id")
    private String id;

    @ApiModelProperty("单位id")
    private String orgId;

    @ApiModelProperty("负责人id")
    private String userId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("分配区域code")
    private Set<String> areaCodeList;
}
