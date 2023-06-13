package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 布点质控专家组列表
 * @Date 2022/5/4 23:30
 */
@Data
public class SpecialistResultVO {

    @ApiModelProperty("专家组id")
    private String id;

    @ApiModelProperty("专家组名称")
    private String groupName;

    @ApiModelProperty("专家组成员名称")
    private String groupUserName;

    @ApiModelProperty("分配任务总数")
    private Long totalCount = 0L;

    @ApiModelProperty("未完成任务数")
    private Long unCompleteCount = 0L;

    @ApiModelProperty("最晚任务期限")
    private String deadLine = "";

    @ApiModelProperty("专家组状态")
    private String status = "";

    @ApiModelProperty("专家组状态")
    private String statusDesc = "";
}
