package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 布点质控列表
 * @Date 2022/5/3 15:11
 */
@Data
public class QualityControlTaskResultVO extends QualityControlResultVO{

    @ApiModelProperty("工作阶段")
    private List<QualityControlBlockWorkStageDTO> workStageList;
}
