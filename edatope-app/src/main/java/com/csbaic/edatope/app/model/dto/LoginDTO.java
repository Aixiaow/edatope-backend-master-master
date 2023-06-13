package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class LoginDTO {

    @ApiModelProperty("用户id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("用户的权限")
    private Set<String> permissions;

    @ApiModelProperty("用户的访问令牌")
    private String token;

    @ApiModelProperty("单位名称")
    private String orgName;

    @ApiModelProperty("单位id")
    private String orgId;

    @ApiModelProperty("用户状态")
    private String status;

    @ApiModelProperty("用户状态描述")
    @DictProperty(type = "user_status", value = "status")
    private String statusDesc;

    @ApiModelProperty("是否需要重新设置密码")
    private Boolean needSetPassword;

    @ApiModelProperty("单位业务类型")
    private List<String> bizType;

    @ApiModelProperty("单位业务类型描述")
    private List<String> bizTypeDesc;
}
