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
public class UpdateOptionCmd {

    @NotEmpty(message = "配置id不能为空")
    @ApiModelProperty("配置id")
    private String id;
    /**
     * 配置名称
     */
    @ApiModelProperty("配置名称")
    private String name;

    /**
     * 配置键值
     */
    @ApiModelProperty("配置键")
    private String key;

    /**
     * 配置值
     */
    @ApiModelProperty("配置键值")
    private String value;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    private String description;



}
