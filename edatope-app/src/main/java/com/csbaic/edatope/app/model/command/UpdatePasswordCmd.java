package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdatePasswordCmd {

    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String password;

    @NotEmpty(message = "确认密码不能为空")
    @ApiModelProperty("确认密码")
    private String confirmPassword;

}
