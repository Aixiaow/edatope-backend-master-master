package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.app.enums.SampleTypeEnums;
import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class DetectionTargetQuery extends PageQuery {

    @ApiModelProperty("指标编号")
    private String targetNumber;

    @ApiModelProperty("指标名称")
    private String name;

    @ApiModelProperty("指标类型")
    private String targetType;

    @ApiModelProperty("样品类型")
    private String sampleType;

    @ApiModelProperty("是否挥发")
    private String volatilize;

    @ApiModelProperty("阶段状态")
    private String status;


}
