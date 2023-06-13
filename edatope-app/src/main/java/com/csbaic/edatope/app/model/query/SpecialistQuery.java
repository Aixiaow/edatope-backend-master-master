package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 专家组分页参数封装
 * @Date 2022/5/1 23:08
 */
@Data
public class SpecialistQuery extends PageQuery {
    @ApiModelProperty("专家组名称")
    private String groupName;

    @ApiModelProperty("专家组组长")
    private String groupLeaderName;

    @ApiModelProperty("专家组状态;DISABLE=停用 NORMAL=正常")
    private String status;

    @ApiModelProperty("专家组成员")
    private String groupUserName;
}
