package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiDTO {

    @ApiModelProperty("接口id")
    private String id;
    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String name;

    /**
     * 接口分组id
     */
    @ApiModelProperty("接口组id")
    private String gid;

    /**
     * 是否是接口组
     */
    @ApiModelProperty("是否是接口组")
    private Boolean apiGroup;

    /**
     * 系统权限id
     */
    @ApiModelProperty("接口权限")
    private PermissionDTO permission;

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
     * 菜单状态：正常、隐藏、禁用
     */
    @ApiModelProperty("接口状态")
    @DictProperty(type = "enable_status", value = "status")
    private String statusDesc;

    /**
     * 接口排序
     */
    @ApiModelProperty("接口排序")
    private Integer sort;

    /**
     * 上级接口
     */
    @ApiModelProperty("上级接口")
    private ApiDTO parent;
    /**
     * 子接口
     */
    @ApiModelProperty("接口列表")
    private List<ApiDTO> children;

}
