package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 专家组分页列表
 * @Date 2022/5/5 15:21
 */
@Data
public class SpecialistPageResultVO {
    @ApiModelProperty("专家组id")
    private String id;

    @ApiModelProperty("专家组名称")
    private String groupName;

    @ApiModelProperty(hidden = true)
    private String groupLeaderUserId;

    @ApiModelProperty("专家组组长")
    private UserDTO groupLeaderName;

    @ApiModelProperty("专家组成员名称")
    private String groupUserName;

    @ApiModelProperty("外聘专家数")
    private Long outsideCount = 0L;

    @ApiModelProperty("分配任务总数")
    private Long totalCount = 0L;

    @ApiModelProperty("未完成任务数")
    private Long unCompleteCount = 0L;

    @ApiModelProperty("任务完成率")
    private String CompleteRate = "0%";

    @ApiModelProperty("最晚任务期限")
    private String deadLine = "";

    @ApiModelProperty("专家组状态")
    private String status = "";

    @ApiModelProperty("专家组状态")
    private String statusDesc = "";

    @ApiModelProperty("是否可删除 1:是 2:否")
    private Integer delete = 1;

    @ApiModelProperty("是否可维护 1:是 2:否")
    private Integer defend = 1;

    @ApiModelProperty("是否可停用 1:是 2:否")
    private Integer stop = 1;
}
