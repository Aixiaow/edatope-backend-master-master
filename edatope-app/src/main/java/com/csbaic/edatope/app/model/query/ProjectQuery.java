package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectQuery extends PageQuery {


    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String name;

    /**
     * 项目所属省份编码
     */
    @ApiModelProperty("项目所属省份编码")
    private String provinceCode;

    /**
     * 项目所属城市编码
     */
    @ApiModelProperty("项目所属城市编码")
    private String cityCode;

    /**
     * 项目所属区县编码
     */
    @ApiModelProperty("项目所属区县编码")
    private String districtCode;

    /**
     * 项目开始日期
     */
    @ApiModelProperty("项目开始日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate beginDate;

    /**
     * 项目结束日期
     */
    @ApiModelProperty("项目结束日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate endDate;

    @ApiModelProperty("排序字段：beginDate或endDate")
    private String orderBy;

    @ApiModelProperty("'排序类型：desc（降序）， asc（升序）")
    private String orderType;

}
