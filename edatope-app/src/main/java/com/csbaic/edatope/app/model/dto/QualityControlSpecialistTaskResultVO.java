package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 质控专家组任务列表
 * @Date 2022/5/4 13:27
 */
@Data
public class QualityControlSpecialistTaskResultVO extends QualityControlResultVO {

    @ApiModelProperty("工作阶段")
    private List<QualityControlSpecialistBlockWorkStageDTO> workStageList;
}
