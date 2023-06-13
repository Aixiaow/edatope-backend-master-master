package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuMetaDTO {

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String title;

    /**
     * 菜单图标
     */
    @ApiModelProperty("菜单图标")
    private String icon;


}
