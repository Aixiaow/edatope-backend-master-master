package com.csbaic.edatope.app.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AuditOpinionVO {

    @ApiModelProperty("审核意见")
    private String auditOpinion;

    @ApiModelProperty("审核意见描述")
    private String auditOpinionDesc;

    @ApiModelProperty("意见说明")
    private String opinionDesc;

    @ApiModelProperty("意见附件")
    private String opinionFile;

}
