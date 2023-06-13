package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleQuery extends PageQuery {

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色性质")
    private String property;

    @ApiModelProperty("角色类型")
    private String type;

    @ApiModelProperty("角色级别")
    private String level;
}
