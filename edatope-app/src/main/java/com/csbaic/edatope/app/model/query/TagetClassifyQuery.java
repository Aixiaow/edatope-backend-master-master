package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TagetClassifyQuery extends PageQuery {

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("指标分类名称")
    private String name;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("检测实验室")
    private String detectionLabOrg;

    @ApiModelProperty("质控实验室")
    private String qualityLabOrg;

    @ApiModelProperty("阶段状态")
    private String status;

}
