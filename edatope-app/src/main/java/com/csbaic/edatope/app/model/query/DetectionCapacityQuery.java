package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DetectionCapacityQuery extends PageQuery {

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("指标名称")
    private String targetName;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("指标类型")
    private String targetType;

    @ApiModelProperty("标准号")
    private String standard;

    @ApiModelProperty("检测实验室")
    private String laboratory;

    @ApiModelProperty("状态")
    private String status;


}
