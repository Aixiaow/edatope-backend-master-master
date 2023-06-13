package com.csbaic.edatope.file.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileQuery extends PageQuery {

    @ApiModelProperty("文件id")
    private String id;

    @ApiModelProperty("文件名称")
    private String name;

}
