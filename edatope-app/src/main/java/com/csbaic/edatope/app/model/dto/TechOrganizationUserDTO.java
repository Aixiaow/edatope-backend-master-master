package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class TechOrganizationUserDTO {

    @ApiModelProperty("技术单位")
    private OrganizationDTO organization;

    @ApiModelProperty("技术单位管理员")
    private List<UserDTO> admin;
}
