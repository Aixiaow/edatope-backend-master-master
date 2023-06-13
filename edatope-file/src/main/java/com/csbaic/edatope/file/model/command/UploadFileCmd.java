package com.csbaic.edatope.file.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UploadFileCmd {

    @ApiModelProperty("文件业务类型")
    private String bizType;

    @ApiModelProperty("上传的文件")
    @NotNull
    private MultipartFile file;

    @ApiModelProperty("文件名称")
    private String fileName;
}
