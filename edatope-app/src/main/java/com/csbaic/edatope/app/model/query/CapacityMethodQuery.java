package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CapacityMethodQuery extends PageQuery {

    @ApiModelProperty("指标类型")
    private String targetType;

    @ApiModelProperty("样品类型")
    private String sampleType;

}
