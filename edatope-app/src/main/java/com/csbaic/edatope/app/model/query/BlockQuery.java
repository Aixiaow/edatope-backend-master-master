package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
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
public class BlockQuery extends PageQuery {


    @ApiModelProperty("地块编码")
    private String code;


    @ApiModelProperty("地块名称")
    private String name;


    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("企业类型")
    private String enterpriseType;

    @ApiModelProperty("行业分类")
    private List<String> enterpriseCategory;

    @ApiModelProperty("地区所属省份编码")
    private String provinceCode;

    @ApiModelProperty("地区所属城市编码")
    private String cityCode;

    @ApiModelProperty("地区所属区县编码")
    private String districtCode;


    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("阶段名称")
    private String phaseName;

    @ApiModelProperty("阶段类型")
    private String phaseType;

    @ApiModelProperty("地块管理单位（仅查某单位下的地块用到）")
    private String orgId;

}
