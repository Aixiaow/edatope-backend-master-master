package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginCmd {

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称或者手机号")
    @NotEmpty(message = "用户名称不能为空")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    @NotEmpty(message = "用户密码不能为空")
    private String password;


}
