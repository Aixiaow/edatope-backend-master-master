package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class AddPermissionCmd {

    @ApiModelProperty("角色id")
    @NotEmpty(message = "角色id不能为空")
    private String roleId;

    @ApiModelProperty("权限id")
    @NotEmpty(message = "权限id不能为空")
    private List<String> permissionList;

}
