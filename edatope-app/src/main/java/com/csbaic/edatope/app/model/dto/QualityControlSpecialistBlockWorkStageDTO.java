package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.Specialist;
import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author bnt
 * @Description 质控专家组工作阶段
 * @Date 2022/5/4 13:27
 */
@Data
@ApiModel
public class QualityControlSpecialistBlockWorkStageDTO {

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

    @ApiModelProperty("调查任务任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate taskDeadline;

    @ApiModelProperty("任务分配单位")
    private List<TasksDTO> tasksList;

    @ApiModelProperty("土壤点")
    private Integer soilCount = 0;

    @ApiModelProperty("地下水")
    private Integer waterCount = 0;

    @ApiModelProperty("土水复合点")
    private Integer soleWaterCount = 0;

    @ApiModelProperty("方案文件")
    private String fileCount = "0/3";

    @ApiModelProperty("布点方案状态")
    private String deployPointStatus;

    @ApiModelProperty("布点方案状态")
    @DictProperty(type = "deploy_point_status", value = "deployPointStatus")
    private String deployPointStatusDesc;

    @ApiModelProperty("布点人员")
    private String userName;

    @ApiModelProperty("质控类型")
    private String qualityType;

    @ApiModelProperty("质控类型")
    @DictProperty(type = "service_level", value = "qualityType")
    private String qualityTypeDesc;

    @ApiModelProperty("质控组织单位")
    private Organization qualityOrg;

    @ApiModelProperty("布点质控单位")
    private Organization pointQualityOrg;

    @ApiModelProperty("质控专家组")
    private Specialist specialist;

    @ApiModelProperty("分配状态")
    private String status;

    @ApiModelProperty("分配状态文本")
    @DictProperty(type = "distribute", value = "status")
    private String statusDesc;

    @ApiModelProperty("方案审核状态")
    private String auditStatus;

    @ApiModelProperty("方案审核状态文本")
    @DictProperty(type = "SchemeAuditStatus", value = "auditStatus")
    private String auditStatusDesc;

    @ApiModelProperty("审核进度")
    private String auditPlan;

    @ApiModelProperty("布点质控意见退回时间")
    private LocalDateTime opinionBackTime;

    @ApiModelProperty("任务分配进度颜色")
    private String auditPlanColor;

    @ApiModelProperty("意见反馈意见查看互斥字段0：意见反馈，1：意见查看")
    private Integer feedback = 0;

    @ApiModelProperty("汇总查看和汇总提交互斥字段0：汇总提交，1：汇总查看")
    private Integer collect = 0;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime updateTime;

    @ApiModelProperty("授权调整时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime authChangeTime;

    @ApiModelProperty("是否可意见退回 0：不可，1：可")
    private Integer sendBack = 0;

    @ApiModelProperty("布点方案数据整改是否可选 0：不可，1：可")
    private Integer checkBox = 0;

    @ApiModelProperty("布点方案数据整改按钮 1：可下载方案 2：可查看结果 3：可重传方案 4：可重导点位 5: 可查看审核记录")
    private List<Integer> pointReformButton = new ArrayList<>();
}
