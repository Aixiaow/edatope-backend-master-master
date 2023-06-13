package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class CreateProjectCmd {

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    @NotEmpty(message = "项目名称不能为空")
    private String name;

    /**
     * 项目所属省份编码
     */
    @ApiModelProperty("项目所属省份编码")
    @NotEmpty(message = "项目所属省份编码不能为空")
    private String provinceCode;

    /**
     * 项目所属城市编码
     */
    @ApiModelProperty("项目所属城市编码")
    @NotEmpty(message = "项目所属城市编码不能为空")
    private String cityCode;

    /**
     * 项目所属区县编码
     */
    @ApiModelProperty("项目所属区县编码")
    @NotEmpty(message = "项目所属区县编码")
    private String districtCode;

    /**
     * 项目开始日期
     */
    @ApiModelProperty("项目开始日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    @NotNull(message = "项目开始日期不能为空")
    private LocalDate beginDate;

    /**
     * 项目结束日期
     */
    @ApiModelProperty("项目结束日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    @NotNull(message = "项目结束日期不能为空")
    private LocalDate endDate;

    /**
     * 项目备注
     */
    @ApiModelProperty("项目备注")
    private String remark;


}
