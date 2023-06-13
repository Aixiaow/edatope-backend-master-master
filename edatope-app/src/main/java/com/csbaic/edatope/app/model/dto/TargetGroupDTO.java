package com.csbaic.edatope.app.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class TargetGroupDTO {

    @ApiModelProperty("分组名称")
    private String name;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("样品类型")
    @DictProperty(type = "SampleType", value = "sampleType")
    private String sampleTypeDec;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("是否挥发")
    private String volatilizeDes;

    @ApiModelProperty("检测指标id")
    private List<DetectionTargetDTO> targetList;

    @ApiModelProperty("状态")
    private String status;

}
