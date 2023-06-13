package com.csbaic.edatope.app.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class TasksRecordDto {

    @ApiModelProperty("操作单位")
    private OrganizationDTO org;

    @ApiModelProperty("操作人员")
    private UserDTO user;

    @ApiModelProperty("委托方式")
    private String entrustWay;

    @ApiModelProperty("委托方式描述")
    @DictProperty(value = "entrustWay", type = "entrust_way")
    private String entrustWayDes;

    @ApiModelProperty("业务单位")
    private OrganizationDTO businessOrg;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("类型描述")
    @DictProperty(value = "type", type = "organization_type")
    private String typeDesc;

    private String status;

    private String statusDesc;

    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDateTime createTime;
}
