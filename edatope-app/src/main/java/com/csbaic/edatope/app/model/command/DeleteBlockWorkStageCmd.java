package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 地块
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Data
public class DeleteBlockWorkStageCmd {

    @ApiModelProperty("工作阶段id")
    @NotEmpty(message = "工作阶段id不能为空")
    private String id;


}
