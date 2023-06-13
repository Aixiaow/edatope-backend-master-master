package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 地块
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Data
public class EnterpriseQuery {


    @ApiModelProperty("企业名称")
    private String name;

}
