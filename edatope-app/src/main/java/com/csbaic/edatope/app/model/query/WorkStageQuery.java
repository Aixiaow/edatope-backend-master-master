package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class WorkStageQuery extends PageQuery {

    @ApiModelProperty("阶段名称")
    private String name;

    @ApiModelProperty("阶段状态")
    private String status;


}
