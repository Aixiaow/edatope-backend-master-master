package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 待分配布点质控任务
 * @Date 2022/5/5 0:33
 */
@Data
public class QualityControlWorkStageVO extends PointWorkStageDistributeVO {

    @ApiModelProperty("质控类型")
    private String qualityType;

    @ApiModelProperty("质控类型")
    @DictProperty(type = "service_level", value = "qualityType")
    private String qualityTypeDesc;

    @ApiModelProperty("质控组织单位")
    private Organization qualityOrg;
}
