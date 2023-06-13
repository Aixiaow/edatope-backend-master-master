package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.enums.YesOrNoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class CreateDetectionCapacityCmd {

    @ApiModelProperty("样品类型")
    @NotEmpty(message = "请选择样品类型")
    private String sampleType;

    @ApiModelProperty("指标类型")
    @NotEmpty(message = "请选择指标类型")
    private String targetType;

    @ApiModelProperty("指标名称")
    @NotEmpty(message = "请填写指标名称")
    @Length(max = 50, message = "请填写指标名称，限50个字以内")
    private String targetName;

    @ApiModelProperty("检测方法名称")
    @NotEmpty(message = "请填写检测方法名称")
    private String detectionMethodName;

    @ApiModelProperty("标准号")
    @NotEmpty(message = "请填写标准号")
    private String standardNumber;

    @ApiModelProperty("检测实验室")
    @NotEmpty(message = "请填写检测实验室")
    private String laboratory;

    @ApiModelProperty("行政管理单位")
    @NotEmpty(message = "请选择行政管理单位")
    private String orgId;

    @ApiModelProperty("方法验证材料")
    private String materials;

    @ApiModelProperty("实验室检出限")
    private BigDecimal restricts;

    @ApiModelProperty("计量单位")
    @NotEmpty(message = "请选择计量单位")
    private String meterageUnit;


    @ApiModelProperty("计量单位（选择其他计量单位时填写）")
    @NotEmpty(message = "请填写计量单位")
    private String meterageUnitDesc;

    @ApiModelProperty("是否挥发")
    private YesOrNoEnum volatilize;
}
