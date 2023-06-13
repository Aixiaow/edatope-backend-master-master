package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateTargetClassifyCmd {

    @ApiModelProperty("检测指标分类名称")
    @NotEmpty(message = "请填写检测指标分类名称，限50个字以内")
    @Length(max = 50, message = "请填写检测指标分类名称，限50个字以内")
    private String name;

    @ApiModelProperty("样品类型，字典值：SampleType")
    @NotEmpty(message = "请选择样品类型")
    private String sampleType;

    @ApiModelProperty("是否挥发")
    @NotEmpty(message = "请确定当前检测指标分类中的检测指标是否为挥发性污染物")
    private String volatilize;

    @ApiModelProperty("检测指标")
    @NotEmpty(message = "请选检测指标")
    private List<String>  targetNumber;

    @ApiModelProperty("检测实验室")
    @NotEmpty(message = "请选择对应的检测实验室")
    private String detectionLabOrg;

    @ApiModelProperty("是否设置质控实验室")
    @NotEmpty(message = "请选择是否设置质控实验室")
    private String qualityLab;

    @ApiModelProperty("质控实验室")
    private String qualityLabOrg;

    @ApiModelProperty("布点单位")
    private String stationingLabOrg;

    @ApiModelProperty("样品保存容器，字典值：container")
    @NotEmpty(message = "请选择样品保存容器")
    private String container;
}
