package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author bage
 * @since 2022-03-19
 */
@Data
public class ProjectDTO {

    @ApiModelProperty("项目id")
    private String id;
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
     * 单位所在省
     */
    @ApiModelProperty("单位所在省名称")
    @DictProperty(type = "area", value = "provinceCode")
    private String provinceName;

    /**
     * 项目所属城市编码
     */
    @ApiModelProperty("项目所属城市编码")
    private String cityCode;


    /**
     * 单位所在市名称
     */
    @ApiModelProperty("单位所在市名称")
    @DictProperty(type = "area", value = "cityCode")
    private String cityName;

    /**
     * 项目所属区县编码
     */
    @ApiModelProperty("项目所属区县编码")
    private String districtCode;


    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县名称")
    @DictProperty(type = "area", value = "districtCode")
    private String districtName;


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

    /**
     * 项目备注
     */
    @ApiModelProperty("项目备注")
    private String remark;


    @ApiModelProperty("单位id")
    private String orgId;

}
