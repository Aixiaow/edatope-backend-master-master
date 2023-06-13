package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description TODO
 * @Date 2022/4/27 6:41
 */
@Data
public class UserListQuery extends PageQuery {
    @ApiModelProperty("用户姓名")
    private String nickName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("单位id")
    private String orgId;
}
