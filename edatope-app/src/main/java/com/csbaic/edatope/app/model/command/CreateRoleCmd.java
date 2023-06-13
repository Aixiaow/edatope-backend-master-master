package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateRoleCmd {


    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    /**
     * 角色编码
     */
    @ApiModelProperty("角色编码")
    @NotEmpty(message = "角色编码不能为空")
    private String code;


    /**
     * 角色性质
     */
    @ApiModelProperty("角色性质，字典类型：role_property")
    @NotEmpty(message = "角色性质不能为空")
    private String property;

    /**
     * 角色类型
     */
    @ApiModelProperty("角色类型，字典类型：role_type")
    @NotEmpty(message = "角色类型不能为空")
    private String type;

    /**
     * 角色级别
     */
    @ApiModelProperty("角色级别，字典类型：role_level")
    @NotEmpty(message = "角色级别不能为空")
    private String level;

    /**
     * 关联单位id
     */
    @ApiModelProperty("关联单位")
    private String orgId;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    private String description;

    /**
     * 角色状态
     */
    @ApiModelProperty("角色状态，字典类型：role_status")
    @NotEmpty(message = "角色状态不能为空")
    private String status;

    @ApiModelProperty("角色权限id列表")
    @NotEmpty(message = "角色权限不能为空")
    private List<String> permissions;
}
