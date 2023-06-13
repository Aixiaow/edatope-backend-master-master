package com.csbaic.edatope.option.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
public class CreateOptionCmd {

    /**
     * 配置名称
     */
    @ApiModelProperty("配置名称")
    @NotEmpty(message = "配置名称不能为空")
    private String name;

    /**
     * 配置键值
     */
    @ApiModelProperty("配置键")
    @NotEmpty(message = "配置键不能为空")
    private String key;

    /**
     * 配置值
     */
    @ApiModelProperty("配置键值")
    @NotEmpty(message = "配置值不能为空")
    private String value;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    private String description;



}
