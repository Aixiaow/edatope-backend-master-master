package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetectionMethodDTO {

    private String id;

    @ApiModelProperty("标准号")
    private String standard;

    @ApiModelProperty("检测方法名称")
    private String name;

    @ApiModelProperty("方法检出限")
    private BigDecimal restricts;

    @ApiModelProperty("计量单位")
    private String meterageUnit;

    @ApiModelProperty("计量单位描述")
    @DictProperty(type = "MeterageUnit", value = "meterageUnit")
    private String meterageUnitDesc;

    @ApiModelProperty("有效保存时间")
    private String holdTime;

    @ApiModelProperty("样品保存条件")
    private String holdCondition;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("检测指标")
    private DetectionTargetDTO targetDTO;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
