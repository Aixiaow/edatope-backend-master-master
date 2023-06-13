package com.csbaic.edatope.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadFileVO {

    @ApiModelProperty("文件id")
    private String id;

    @ApiModelProperty("文件下载路径")
    private String url;
}
