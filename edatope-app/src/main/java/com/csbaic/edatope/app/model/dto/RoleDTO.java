package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {


    @ApiModelProperty("角色id")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    private String code;


    /**
     * 角色性质
     */
    @ApiModelProperty("角色性质，字典类型：role_property")
    private String property;


    /**
     * 角色性质
     */
    @ApiModelProperty("角色性质描述")
    @DictProperty(type = "role_property", value = "property")
    private String propertyDesc;

    /**
     * 角色类型
     */
    @ApiModelProperty("角色类型，字典类型：role_type")
    private String type;

    @ApiModelProperty("角色类型描述")
    @DictProperty(type = "role_type", value = "type")
    private String typeDesc;

    /**
     * 角色级别
     */
    @ApiModelProperty("角色级别，字典类型：role_level")
    private String level;


    /**
     * 角色级别
     */
    @ApiModelProperty("角色级别描述")
    @DictProperty(type = "role_level", value = "level")
    private String levelDesc;


    /**
     * 关联单位id
     */
    @ApiModelProperty("关联单位")
    private String orgId;

    /**
     * 关联单位信息
     */
    @ApiModelProperty("关联单位基本信息")
    private OrganizationDTO org;


    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 角色状态
     */
    @ApiModelProperty("角色状态，字典类型：role_status")
    private String status;

    /**
     * 角色状态
     */
    @ApiModelProperty("角色状态描述")
    @DictProperty(type = "role_status", value = "status")
    private String statusDesc;

    @ApiModelProperty("角色权限id列表")
    private List<PermissionDTO> permissions;

}
