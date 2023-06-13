package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.entity.Organization;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 专家组成员
 * @Date 2022/5/5 20:59
 */
@Data
public class SpecialistUserVO {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("专家性质")
    private String nature;

    @ApiModelProperty("专家性质")
    private String natureDesc;

    @ApiModelProperty("工作单位")
    private Organization orgId;

    @ApiModelProperty("专家信息")
    private UserDTO userInfo;

    @ApiModelProperty("专家身份")
    private String specialistIdentity;

    @ApiModelProperty("专家身份")
    private String specialistIdentityDesc;

    @ApiModelProperty("分配任务总数")
    private Integer totalCount = 0;

    @ApiModelProperty("未完成任务数")
    private Long unCompleteCount = 0L;

    @ApiModelProperty("最终任务期限")
    private String deadLine = "";

    @ApiModelProperty("任务完成率")
    private String completeRate = "0%";

    @ApiModelProperty("担任组长数")
    private Long leaderCount = 0L;

    @ApiModelProperty("所属专家组数")
    private Integer specialistCount = 0;
}
