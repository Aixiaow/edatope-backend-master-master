package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author bnt
 * @Description 人员选择列表
 * @Date 2022/4/27 0:40
 */
@Data
public class UserSelectResultVO {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户姓名")
    private String nickName;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("所属部门")
    private String department;

    @ApiModelProperty("用户的角色")
    private List<RoleDTO> roles;

    @ApiModelProperty("分配任务总数")
    private Integer totalCount = 0;

    @ApiModelProperty("未完成任务数")
    private Long unCompleteCount = 0L;

    @ApiModelProperty("最终任务期限")
    private String deadLine = "";

}
