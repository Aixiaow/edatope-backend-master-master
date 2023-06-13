package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UpdateTargetClassifyCmd {

    private String id;

    @ApiModelProperty("检测指标分类名称")
    private String name;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("检测指标")
    private List<String>  targetNumber;

    @ApiModelProperty("检测实验室")
    private String detectionLabOrg;

    @ApiModelProperty("是否设置质控实验室")
    private String qualityLab;

    @ApiModelProperty("质控实验室")
    private String qualityLabOrg;

    @ApiModelProperty("布点单位")
    private String stationingLabOrg;

    @ApiModelProperty("样品保存容器")
    private String container;
}
