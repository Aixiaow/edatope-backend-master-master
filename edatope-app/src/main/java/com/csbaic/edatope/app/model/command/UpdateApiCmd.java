package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateApiCmd {

    /**
     * 接口分组id
     */
    @ApiModelProperty("接口id")
    @NotEmpty(message = "接口id不能为空")
    private String id;

    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String name;

    /**
     * 接口分组id
     */
    @ApiModelProperty("接口分组id")
    private String gid;

    /**
     * 系统权限id
     */
    @ApiModelProperty("接口权限code")
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
    private Boolean anonymous;

    /**
     * 菜单状态：正常、隐藏、禁用
     */
    @ApiModelProperty("接口状态")
    private String status;

    /**
     * 接口排序
     */
    @ApiModelProperty("接口排序")
    private Integer sort;

    /**
     * 是否是接口组
     */
    @ApiModelProperty("是否是接口组")
    private Boolean apiGroup;
}
