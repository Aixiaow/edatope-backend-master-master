package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 各样点类型数量
 * @Date 2022/4/27 22:39
 */
@Data
public class PointCountDTO {
    @ApiModelProperty("土壤点")
    private Integer soilCount = 0;

    @ApiModelProperty("地下水")
    private Integer waterCount = 0;

    @ApiModelProperty("土水复合点")
    private Integer soleWaterCount = 0;
}
