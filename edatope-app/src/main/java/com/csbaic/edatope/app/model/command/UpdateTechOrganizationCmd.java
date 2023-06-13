package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.UserDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTechOrganizationCmd {


    @NotNull(message = "技术单位信息不能为空")
    @ApiModelProperty("技术单位")
    @Valid
    private OrganizationDTO organization;

    @NotNull(message = "单位管理员不能为空")
    @ApiModelProperty("管理员")
    @Valid
    private UpdateTechOrganizationAdminCmd admin;

}
