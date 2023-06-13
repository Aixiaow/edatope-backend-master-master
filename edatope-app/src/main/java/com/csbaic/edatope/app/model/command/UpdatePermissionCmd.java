package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel
@Data
public class UpdatePermissionCmd {

    @ApiModelProperty("权限id")
    @NotBlank(message = "权限id不能为空")
    private String id;

    @ApiModelProperty("权限名称")
    private String name;

    @ApiModelProperty("权限编码")
    private String code;

    @ApiModelProperty("上级权限id")
    private String pid;

    @ApiModelProperty("权限类型")
    private String type;

    @ApiModelProperty("权限状态")
    private String status;

    @ApiModelProperty("权限排序")
    private Integer sort;


}
