package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateMenuCmd {


    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    @NotEmpty(message = "菜单名称不能为空")
    private String name;
//
//    /**
//     * 菜单标题
//     */
//    @ApiModelProperty("菜单标题")
//    @NotEmpty(message = "菜单标题不能为空")
//    private String title;


    /**
     * 上级菜单id
     */
    @ApiModelProperty("上级菜单id")
    private String pid;

    /**
     * 系统权限id
     */
    @ApiModelProperty("菜单权限不能为空")
    private String permissionId;

    /**
     * 菜单类型：菜单项、按钮
     */
    @ApiModelProperty("菜单类型：路由，按钮")
    @NotEmpty(message = "菜单类型不能为空")
    private String type;

    /**
     * 菜单路径
     */
    @NotEmpty(message = "菜单路径不能为空")
    @ApiModelProperty("菜单路径")
    private String path;

    /**
     * 菜单组件
     */
    @ApiModelProperty("菜单组件")
    private String component;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;

    /**
     * 菜单排序
     */
    @ApiModelProperty("菜单排序")
    private String sort;

    /**
     * 是否为外部链接
     */
    @ApiModelProperty("是否为外部链接")
    private Boolean external;

    /**
     * 菜单状态：正常、隐藏、禁用
     */
    @ApiModelProperty("菜单状态：启用（ENABLED）、隐藏（HIDDEN）、禁用（DISABLED）")
    @NotEmpty(message = "菜单状态不能为空")
    private String status;

}
