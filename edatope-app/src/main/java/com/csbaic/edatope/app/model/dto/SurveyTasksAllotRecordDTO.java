package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 地块
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Data
public class SurveyTasksAllotRecordDTO {

    /**
     * 地块id
     */
    @ApiModelProperty("地块id")
    private String blockId;

    @ApiModelProperty("地快编号")
    private String code;

    /**
     * 地区名称
     */
    @ApiModelProperty("地块名称")
    private String name;

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterprise;

    @ApiModelProperty("项目信息")
    private ProjectDTO project;

    @ApiModelProperty("地块工作阶段")
    private String blockStageName;

    @ApiModelProperty("阶段类型")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    @ApiModelProperty("任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate deadline;

    @ApiModelProperty("任务记录")
    private List<TasksDTO> tasksList;

    @ApiModelProperty("操作记录")
    private List<TasksRecordDto> recordList;

}
