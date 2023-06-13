package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地块工作阶段
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@Data
public class BlockWorkStageDTO {

    /**
     * 地块工作阶段id
     */
    @ApiModelProperty("地块工作阶段id")
    private String id;

    /**
     * 地区名称
     */
    @ApiModelProperty("地块工作阶段")
    private String name;

    /**
     * 项目id
     */
    @ApiModelProperty("地块id")
    private String blockId;

    /**
     * 工作任务id
     */
    @ApiModelProperty("阶段类型")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    /**
     * 工作阶段状态
     */
    @ApiModelProperty("工作阶段状态")
    private String status;

    @ApiModelProperty("工作阶段状态文本")
    @DictProperty(value = "status", type = "block_work_state_status")
    private String statusDesc;

    @ApiModelProperty("调查任务分配状态")
    private String taskStatus;

    @ApiModelProperty("调查任务分配状态文本")
    private String taskStatusDesc;

    /**
     * 是否需要核实
     */
    @ApiModelProperty("是否需要核实")
    private Boolean verify;

    @ApiModelProperty("委托方式")
    private String entrustWay;

    @ApiModelProperty("委托方式描述")
    @DictProperty(value = "entrustWay", type = "entrust_Way")
    private String entrustWayDesc;

    /**
     * 任务期限
     */
    @ApiModelProperty("任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate deadline;

    @ApiModelProperty("任务分配单位")
    private List<TasksDTO> tasksList;

    /**
     * 记录创建时间
     */
    @ApiModelProperty("记录创建时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;

}
