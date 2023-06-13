package com.csbaic.edatope.app.model.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class PermissionQuery {

    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String name;

    /**
     * 权限code
     */
    @ApiModelProperty("权限code")
    private String code;

    /**
     * 权限类型
     */
    @ApiModelProperty("权限类型")
    private String type;
}
