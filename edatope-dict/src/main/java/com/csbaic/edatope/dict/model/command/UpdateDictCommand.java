package com.csbaic.edatope.dict.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UpdateDictCommand {


    @ApiModelProperty("字典Id")
    @NotEmpty(message = "字典id不能为空")
    private String id;

    /**
     * 字典名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 字典编码
     */
    @ApiModelProperty("字典类型")
    private String type;


    @ApiModelProperty("上级字典id")
    private String pid;

    /**
     * 字典说明
     */
    @ApiModelProperty("字典描述")
    private String description;


    @ApiModelProperty("字典值")
    private String value;

    @ApiModelProperty("字典状态")
    private String status;

}
