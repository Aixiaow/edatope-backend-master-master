package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.enums.OrganizationCategory;
import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 布点质控
 * @Date 2022/5/3 15:24
 */
@Data
public class QualityControlDTO {

    @ApiModelProperty("质控组织单位")
    private Organization qualityOrg;

    @ApiModelProperty("质控类型")
    private String qualityType;

    @ApiModelProperty("质控类型")
    @DictProperty(type = "service_level", value = "qualityType")
    private String qualityTypeDesc;

    @ApiModelProperty("方案审核状态")
    private String auditStatus;

    @ApiModelProperty("方案审核状态")
    @DictProperty(type = "SchemeAuditStatus", value = "auditStatus")
    private String auditStatusDesc;
}
