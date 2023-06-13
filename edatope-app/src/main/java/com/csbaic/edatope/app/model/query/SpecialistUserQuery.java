package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 布点质控专家
 * @Date 2022/5/6 7:52
 */
@Data
public class SpecialistUserQuery extends PageQuery {

    @ApiModelProperty("专家姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("工作单位")
    private String orgName;

    @ApiModelProperty("专家性质")
    private String nature;
}
