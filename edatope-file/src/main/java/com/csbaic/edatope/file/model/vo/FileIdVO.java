package com.csbaic.edatope.file.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileIdVO {

    @ApiModelProperty("文件id")
    private String id;
}
