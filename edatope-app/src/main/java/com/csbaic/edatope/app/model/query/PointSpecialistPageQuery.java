package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 布点质控专家组列表
 * @Date 2022/5/5 0:05
 */
@Data
public class PointSpecialistPageQuery extends PageQuery {
    @ApiModelProperty("专家组名称")
    private String groupName;

    @ApiModelProperty("专家组成员")
    private String groupUserName;

    @ApiModelProperty("专家组成员id")
    private String specialistUserId;
}
