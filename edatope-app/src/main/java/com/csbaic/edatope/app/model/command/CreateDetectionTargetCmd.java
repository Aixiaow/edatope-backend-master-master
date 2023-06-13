package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.enums.SampleTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateDetectionTargetCmd {

    @ApiModelProperty("指标编号")
    @NotEmpty(message = "请填写编号")
//    @Length(max = 50, message = "请填写阶段名称，限50个字以内")
    private String targetNumber;

    @ApiModelProperty("指标名称")
    @NotEmpty(message = "请填写指标名称，限50个字以内")
    @Length(max = 50, message = "请填写阶段名称，限50个字以内")
    private String name;

    @ApiModelProperty("指标类型")
    @NotEmpty(message = "请填写指标类型")
    private String targetType;

    @ApiModelProperty("样品类型")
    @NotEmpty(message = "请填写样品类型")
    private String sampleType;

    @ApiModelProperty("是否挥发")
    @NotEmpty(message = "请选择是否挥发")
    private String volatilize;
}
