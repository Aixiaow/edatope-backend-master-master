package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *
 * </p>
 *
 * @author bage
 * @since 2022-03-19
 */
@Data
public class DeleteWorkStageCmd {

    @ApiModelProperty("工作阶段id")
    @NotEmpty(message = "工作阶段id不能为空")
    private String id;


}
