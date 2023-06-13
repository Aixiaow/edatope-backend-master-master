package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DetectionTargetDTO {

    private String id;

    @ApiModelProperty("指标编号")
    private String targetNumber;

    @ApiModelProperty("指标名称")
    private String name;

    @ApiModelProperty("指标类型")
    private String targetType;

    @ApiModelProperty("指标类型")
    @DictProperty(type = "IndexType", value = "targetType")
    private String targetTypeDes;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("样品类型")
    @DictProperty(type = "SampleType", value = "sampleType")
    private String sampleTypeDec;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("是否挥发")
    private String volatilizeDes;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
