package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends PageQuery {

    @ApiModelProperty("用户姓名")
    private String nickName;

    @ApiModelProperty("创建账号")
    private String username;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("是否被锁定")
    private Boolean locked;

    @ApiModelProperty("用户类型")
    private String type;

    @ApiModelProperty("用户状态（字典：user_status）")
    private String status;

    @ApiModelProperty("用户角色（角色id）")
    private List<String> roles;

    /**
     * 用户所在单位
     */
    private List<String> orgIds;

}
