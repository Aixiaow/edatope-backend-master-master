package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiGroupDTO {

    @ApiModelProperty("接口分组id")
    private String id;
    @ApiModelProperty("接口分组名称")
    private String name;
    @ApiModelProperty("上组分组id")
    private String pid;
    @ApiModelProperty("上组分组")
    private ApiGroupDTO parent;
    @ApiModelProperty("接口列表")
    private List<ApiDTO> apis;
    @ApiModelProperty("接口分组列表")
    private List<ApiGroupDTO> children;

}
