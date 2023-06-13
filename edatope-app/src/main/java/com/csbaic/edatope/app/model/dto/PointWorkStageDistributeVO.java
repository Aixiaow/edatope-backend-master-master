package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author bnt
 * @Description 待分配和已分配任务列表
 * @Date 2022/4/27 16:45
 */
@Data
public class PointWorkStageDistributeVO {

    @ApiModelProperty("地块编码")
    private String code;

    @ApiModelProperty("地块名称")
    private String blockName;

    @ApiModelProperty("地块管理单位")
    private OrganizationDTO organization;

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterprise;

    @ApiModelProperty("地块工作阶段id")
    private String blockWorkStageId;

    @ApiModelProperty("地块工作阶段名称")
    private String name;

    @ApiModelProperty("阶段类型id")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    @ApiModelProperty("调查任务任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate taskDeadline;
}
