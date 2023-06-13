package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author bnt
 * @Description 布点人员工作阶段信息
 * @Date 2022/4/26 20:17
 */
@Data
public class PointUserWorkStageDTO {

    @ApiModelProperty("地块工作阶段id")
    private String id;

    @ApiModelProperty("地块工作阶段名称")
    private String name;

    @ApiModelProperty("阶段类型")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    @ApiModelProperty("委托方式")
    private String entrustWay;

    @ApiModelProperty("委托方式描述")
    @DictProperty(value = "entrustWay", type = "entrust_Way")
    private String entrustWayDesc;

    @ApiModelProperty("任务分配单位")
    private List<TasksDTO> tasksList;

    //--

    @ApiModelProperty("调查任务任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate taskDeadline;

    @ApiModelProperty("布点人员id")
    private String userId = "";

    @ApiModelProperty("布点人员名称")
    private String userName = "";

    @ApiModelProperty("分配状态")
    private String status;

    @ApiModelProperty("分配状态文本")
    private String statusDesc;
}
