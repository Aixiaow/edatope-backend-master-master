package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DetectionCapacityCheckDTO {

    private String id;

    @ApiModelProperty("审核单位")
    private OrganizationDTO checkOrg;

    @ApiModelProperty("审核人员")
    private UserDTO userDTO;

    @ApiModelProperty("审核节点")
    private String checkNode;

    @ApiModelProperty("审核备注")
    private String checkRemark;

    @ApiModelProperty("附件地址")
    private String accessoryPath;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("状态描述")
    private String statusDes;

    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
