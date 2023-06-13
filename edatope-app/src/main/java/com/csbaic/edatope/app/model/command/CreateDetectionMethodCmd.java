package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Data
public class CreateDetectionMethodCmd {

    /**
     *
     */
    @ApiModelProperty("检测指标id")
    @NotEmpty(message = "请选择指标id")
    private String targetId;

    @ApiModelProperty("标准号")
    @NotEmpty(message = "请填写标准号")
//    @Length(max = 50, message = "请填写阶段名称，限50个字以内")
    private String standard;

    @ApiModelProperty("检测方法名称")
    @NotEmpty(message = "请填写检测方法名称")
    private String name;

    @ApiModelProperty("方法检出限")
    private BigDecimal restricts;

    @ApiModelProperty("计量单位")
    @NotEmpty(message = "请填写计量单位")
    private String meterageUnit;

    @ApiModelProperty("有效保存时间")
    @NotEmpty(message = "请填写有效保存时间")
    private String holdTime;

    @ApiModelProperty("样品保存条件")
    @NotEmpty(message = "请填写样品保存条件")
    private String holdCondition;
}
