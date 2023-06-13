package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DetectionCapacityAuditBody extends PageQuery {

    @ApiModelProperty("指标名称（通过审核只传id）")
    private String id;

    @ApiModelProperty("退回原因")
    private String backCause;

    @ApiModelProperty("附件地址")
    private String accessoryPath;

    @ApiModelProperty("通过/退回")
    private Integer type;
}
