package com.csbaic.edatope.file.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class CreateFileCmd {

    /**
     * 文件名称
     */
    @NotEmpty(message = "文件名称不能为空")
    @ApiModelProperty("文件名称")
    private String name;

    /**
     * 文件md5
     */
    @NotEmpty(message = "文件md5不能为空")
    @ApiModelProperty("md5")
    private String md5;

    /**
     * 文件业务类型
     */
    @ApiModelProperty("文件业务类型")
    private String bizType;

}
