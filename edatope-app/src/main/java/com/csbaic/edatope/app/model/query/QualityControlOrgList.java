package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 质控单位列表
 * @Date 2022/5/4 17:37
 */
@Data
public class QualityControlOrgList extends PageQuery {
    @ApiModelProperty("布点质控单位")
    private String orgName;

    @ApiModelProperty("单位管理员")
    private String orgUserName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("归属单位id")
    private String ownerId;

    @ApiModelProperty(hidden = true)
    private String serviceLevel;
}
