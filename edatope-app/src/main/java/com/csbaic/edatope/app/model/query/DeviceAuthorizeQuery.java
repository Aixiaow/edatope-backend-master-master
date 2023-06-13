package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceAuthorizeQuery extends PageQuery {

    @ApiModelProperty("单位名称")
    private String organizationName;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("设置标识符")
    private String identifier;
}
