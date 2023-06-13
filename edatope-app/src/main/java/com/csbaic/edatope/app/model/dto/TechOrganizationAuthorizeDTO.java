package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.model.dto.AreaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class TechOrganizationAuthorizeDTO {

    /**
     * 授权记录id
     */
    @ApiModelProperty("授权记录id")
    private String id;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("授权区域")
    private Set<String> aresCodeList;

    @ApiModelProperty("授权区域")
    private List<AreaDTO> areaList;

    @ApiModelProperty("单位")
    private OrganizationDTO organization;

    @ApiModelProperty("分配角色")
    private RoleDTO roleDTO;

    @ApiModelProperty("负责人")
    private UserDTO user;
}
