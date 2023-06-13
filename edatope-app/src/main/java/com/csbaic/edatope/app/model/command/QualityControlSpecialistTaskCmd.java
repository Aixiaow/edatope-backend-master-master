package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author bnt
 * @Description 质控专家组任务分配
 * @Date 2022/5/4 10:26
 */
@Data
public class QualityControlSpecialistTaskCmd {

    @NotEmpty(message = "布点质控任务id不能为空")
    @ApiModelProperty("布点质控任务id集合")
    private List<String> qualityControlTasksIds;

    @NotBlank(message = "质控专家组id不能为空")
    @ApiModelProperty("质控专家组id")
    private String qualitySpecialistId;
}
