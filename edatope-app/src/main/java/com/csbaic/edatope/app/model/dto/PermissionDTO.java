package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PermissionDTO {


    @ApiModelProperty("权限id")
    private String id;

    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 权限编码
     */
    @ApiModelProperty("权限编码")
    private String code;

    /**
     * 上级权限id
     */
    @ApiModelProperty("上级权限id")
    private String pid;

    /**
     * 上组权限
     */
    @ApiModelProperty("上级权限")
    private PermissionDTO parent;

    /**
     * 权限状态
     */
    @ApiModelProperty("权限状态")
    private String status;

    /**
     * 权限状态
     */
    @ApiModelProperty("权限状态")
    @DictProperty(type = "enable_status", value = "status")
    private String statusDesc;


    /**
     * 记录更新时间
     */
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 记录创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 子权限
     */
    @ApiModelProperty("子权限")
    private List<PermissionDTO> children;
}
