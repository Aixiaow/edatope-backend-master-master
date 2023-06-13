package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateTechOrganizationCmd {


    @NotNull(message = "技术单位信息不能为空")
    @ApiModelProperty("技术单位")
    @Valid
    private CreateOrganizationCmd organization;

    @NotNull(message = "单位管理员不能为空")
    @ApiModelProperty("管理员")
    @Valid
    private CreateTechOrganizationAdminCmd admin;

}
