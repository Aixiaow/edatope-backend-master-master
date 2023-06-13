package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author bnt
 * @Description 采样过程点位调整
 * @Date 2022/5/7 18:19
 */
@Data
public class QualityControlBackCmd {
    @NotEmpty(message = "布点质控任务id不能为空")
    @ApiModelProperty("布点质控任务id")
    private String qualityControlTasksId;

    @ApiModelProperty("质控专家组id")
    private String qualitySpecialistId;
}
