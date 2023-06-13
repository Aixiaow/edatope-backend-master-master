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
public class ProjectInfoDTO {

    @ApiModelProperty("项目id")
    private String id;
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String name;

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


    @ApiModelProperty("阶段数量")
    private Integer workStageCount = 0;

    @ApiModelProperty("企业数量")
    private Integer enterpriseCount = 0;

    @ApiModelProperty("地块数量")
    private Integer blockCount = 0;
}
