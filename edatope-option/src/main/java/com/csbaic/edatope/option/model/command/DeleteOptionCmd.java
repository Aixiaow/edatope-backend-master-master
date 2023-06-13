package com.csbaic.edatope.option.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author bage
 * @since 2022-01-05
 */
@Data
public class DeleteOptionCmd {

    @NotEmpty(message = "配置id不能为空")
    @ApiModelProperty("配置id")
    private String id;


}
