package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DetectionCapacityDTO {

    private String id;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("样品类型描述")
    @DictProperty(value = "sampleType", type = "SampleType")
    private String sampleTypeDesc;

    @ApiModelProperty("指标类型")
    private String targetType;

    @ApiModelProperty("指标类型描述")
    @DictProperty(value = "targetType", type = "TestIndexType")
    private String targetTypeDesc;

    @ApiModelProperty("指标名称")
    private String targetName;

    @ApiModelProperty("检测方法名称")
    private String detectionMethodName;

    @ApiModelProperty("标准号")
    private String standardNumber;

    @ApiModelProperty("检测实验室")
    private String laboratory;

    @ApiModelProperty("检测实验室描述")
    private OrganizationDTO laboratoryOrg;

    @ApiModelProperty("行政管理单位")
    private String orgId;

    @ApiModelProperty("方法验证材料")
    private String materials;

    @ApiModelProperty("实验室检出限")
    private BigDecimal restricts;

    @ApiModelProperty("计量单位")
    private String meterageUnit;

    @ApiModelProperty("计量单位值")
    @DictProperty(value = "meterageUnit", type = "MeterageUnit")
    private String meterageUnitValue;

    @ApiModelProperty("计量单位（选择其他计量单位时填写）")
    private String meterageUnitDesc;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("是否挥发描述")
    private String volatilizeDesc;

    @ApiModelProperty("行政管理单位")
    private OrganizationDTO organizationDTO;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态描述")
    private String statusDes;

    @ApiModelProperty("审核信息")
    private List<DetectionCapacityCheckDTO> checkList;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
