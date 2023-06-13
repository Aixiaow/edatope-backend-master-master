package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class CreateBlockWorkStageCmd {

    @ApiModelProperty("工作阶段名称")
    @NotEmpty(message = "工作阶段名称不能为空")
    private String name;

    @ApiModelProperty("地块id")
    @NotEmpty(message = "地块id不能为空")
    private String blockId;

    @ApiModelProperty("工作阶段id")
    @NotEmpty(message = "工作阶段id不能为空")
    private String workStageId;

    @ApiModelProperty("是否需要核实")
    private Boolean verify;

    @ApiModelProperty("任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate deadline;

}
