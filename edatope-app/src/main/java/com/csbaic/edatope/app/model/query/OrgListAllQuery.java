package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrgListAllQuery {

    /**
     * 单位类型
     */
    @ApiModelProperty("单位类型，没有下拉选择目前写死字典值，检测实验室：D001-006， 质控实验室：D001-009")
    private String bizType;

}
