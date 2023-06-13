package com.csbaic.edatope.dict.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DictQuery extends PageQuery {

    @ApiModelProperty("字典id")
    private String id;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典值")
    private String value;

}
