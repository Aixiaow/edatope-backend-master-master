package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {

    @ApiModelProperty("菜单id")
    private String id;
    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String name;

    /**
     * 上级菜单id
     */
    @ApiModelProperty("上级菜单id")
    private String pid;

    /**
     * 上级菜单
     */
    @ApiModelProperty("上级菜单")
    private MenuDTO parent;

    /**
     * 系统权限id
     */
    @ApiModelProperty("系统权限id")
    private String permissionId;

    /**
     * 菜单类型：菜单项、按钮
     */
    @ApiModelProperty("菜单类型：路由，按钮")
    private String type;

    @ApiModelProperty("菜单类型描述")
    @DictProperty(type = "menu_type", value = "type")
    private String typeDesc;

    /**
     * 菜单路径
     */
    @ApiModelProperty("菜单路径")
    private String path;

    @ApiModelProperty("菜单元数据")
    private MenuMetaDTO meta;

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
    @ApiModelProperty("菜单状态：正常、隐藏、禁用")
    private String status;

    @ApiModelProperty("菜单状态描述")
    @DictProperty(type = "menu_status", value = "status")
    private String statusDesc;

    /**
     * 权限
     */
    @ApiModelProperty("菜单的权限")
    private PermissionDTO permission;


    @ApiModelProperty("子菜单")
    private List<MenuDTO> children;

    private String component;
}
