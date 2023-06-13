package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 布点方案数据维护-待提交布点方案列表
 * @Date 2022/4/28 0:38
 */
@Data
public class SubmitPointTaskVO extends PointWorkStageDistributeVO {
    @ApiModelProperty("土壤点")
    private Integer soilCount = 0;

    @ApiModelProperty("地下水")
    private Integer waterCount = 0;

    @ApiModelProperty("土水复合点")
    private Integer soleWaterCount = 0;

    @ApiModelProperty("方案文件")
    private String fileCount = "0/3";
}
