package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TargetClassifyDTO {
    private String id;

    @ApiModelProperty("检测指标分类名称")
    private String name;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("样品类型描述")
    @DictProperty(value = "sampleType", type = "SampleType")
    private String sampleTypeDes;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("是否挥发描述")
    private String volatilizeDes;

    @ApiModelProperty("检测实验室")
    private OrganizationDTO detectionLabOrg;

    @ApiModelProperty("是否设置质控实验室")
    private String qualityLab;

    @ApiModelProperty("是否设置质控实验室描述")
    private String qualityLabDes;

    @ApiModelProperty("质控实验室")
    private OrganizationDTO qualityLabOrg;

    @ApiModelProperty("布点单位")
    private OrganizationDTO stationingLabOrg;

    @ApiModelProperty("样品保存容器")
    private String container;

    @ApiModelProperty("样品保存容器描述")
    @DictProperty(value = "container", type = "container")
    private String containerDes;

    @ApiModelProperty("任务阶段")
    private List<String> targetNumber;

    @ApiModelProperty("任务阶段描述")
    private List<String> targetNumberDes;

    @ApiModelProperty("样品数")
    private Integer sampleNum = 0;

    @ApiModelProperty("样点数")
    private Integer samplePointNum = 0;

    @ApiModelProperty("指标数")
    private Integer targetNum = 0;

    @ApiModelProperty("审核记录信息")
    private List<TargetClassifyCheckDTO> checkList;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
