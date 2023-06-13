package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 布点质控分页 工作阶段
 * @Date 2022/5/3 15:12
 */
@Data
public class QualityControlBlockWorkStageDTO {
    @ApiModelProperty("布点质控任务id")
    private String qualityControlTasksId;

    @ApiModelProperty("地块工作阶段id")
    private String id;

    @ApiModelProperty("地块工作阶段名称")
    private String name;

    @ApiModelProperty("阶段类型")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    @ApiModelProperty("任务分配单位")
    private List<TasksDTO> tasksList;

    @ApiModelProperty("土壤点")
    private Integer soilCount = 0;

    @ApiModelProperty("地下水")
    private Integer waterCount = 0;

    @ApiModelProperty("土水复合点")
    private Integer soleWaterCount = 0;

    @ApiModelProperty("分配状态")
    private String status;

    @ApiModelProperty("分配状态文本")
    @DictProperty(type = "distribute", value = "status")
    private String statusDesc;

    @ApiModelProperty("质控信息")
    private List<QualityControlDTO> qualityControlDTOList;
}
