package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateApiCmd {

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称", required = true)
    @NotEmpty(message = "接口名称不能为空")
    private String name;

    /**
     * 接口分组id
     */
    @ApiModelProperty("上级接口")
    private String gid;

    /**
     * 系统权限id
     */
    @ApiModelProperty("接口权限id")
    private String permissionCode;

    /**
     * 接口路径
     */
    @ApiModelProperty("接口路径")
    private String path;

    /**
     * 接口是否可以匿名访问
     */
    @ApiModelProperty("是否可以匿名访问")
    private Boolean anonymous = false;

    /**
     * 菜单状态：正常、隐藏、禁用
     */
    @ApiModelProperty(value = "接口状态：启用、禁用", required = true)
    private String status;

    /**
     * 接口排序
     */
    @ApiModelProperty("接口排序")
    private Integer sort;


    @ApiModelProperty("是否是接口组")
    private Boolean apiGroup;

}
