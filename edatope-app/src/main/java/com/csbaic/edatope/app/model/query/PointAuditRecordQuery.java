package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author bnt
 * @Description 布点方案审核记录查询
 * @Date 2022/5/11 23:07
 */
@Data
public class PointAuditRecordQuery extends PageQuery {

    @ApiModelProperty("操作单位名称")
    private String orgName;

    @ApiModelProperty("业务类型")
    private String operateType;

    @ApiModelProperty("服务级别")
    private String serviceLevel;

    @ApiModelProperty("质控类型")
    private String auditType;

    @NotBlank(message = "地块工作阶段id不能为空")
    @ApiModelProperty("地块工作阶段id")
    private String blockWorkStageId;
}
