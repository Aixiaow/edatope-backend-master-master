package com.csbaic.edatope.dict.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateDictCommand {

    /**
     * 字典名称
     */
    @ApiModelProperty("名称")
    @NotEmpty(message = "字典名称不能为空")
    private String name;

    @ApiModelProperty("上级字典id")
    private String pid;

    /**
     * 字典说明
     */
    @ApiModelProperty("字典描述")
    private String description;


    @ApiModelProperty("字典值")
    private String value;

    /**
     * 字典编码
     */
    @ApiModelProperty("字典类型")
    @NotEmpty(message = "字典类型不能为空")
    private String type;
    /**
     * 字典状态
     */
    @ApiModelProperty("字典状态")
    @NotEmpty(message = "字典状态不能为空")
    private String status;

    @ApiModelProperty("字典排序")
    private Integer sort;
}
