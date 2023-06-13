package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class CreatePermissionCmd {

    @ApiModelProperty("权限名称")
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @ApiModelProperty("权限编码")
    @NotBlank(message = "权限编码不能为空")
    private String code;

    @ApiModelProperty("上级权限id")
    private String pid;

    @ApiModelProperty("权限排序")
    private Integer sort;

}
