package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 创建单位
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class DeleteOrganizationCmd {

    /**
     * 单位id
     */
    @ApiModelProperty("单位id（保存的时候可选")
    @NotEmpty(message = "单位id不能为空")
    private String id;


}
