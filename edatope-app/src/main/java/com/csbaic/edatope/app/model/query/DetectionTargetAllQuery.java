package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DetectionTargetAllQuery {

    @ApiModelProperty("指标名称, 查询指标分组信息不需要传此字段")
    private List<String> nameList;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("样品类型")
    private String sampleType;

}
