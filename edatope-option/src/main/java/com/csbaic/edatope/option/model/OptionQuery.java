package com.csbaic.edatope.option.model;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OptionQuery extends PageQuery {

    @ApiModelProperty("配置id")
    private String id;
    @ApiModelProperty("配置名称")
    private String name;
    @ApiModelProperty("配置键")
    private String key;
    @ApiModelProperty("配置值")
    private String value;
}
