package com.csbaic.edatope.app.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointImportHeadVO {

    @ApiModelProperty("取值")
    @ExcelProperty(index = 1)
    private String value;

//    @ApiModelProperty("阶段名称")
//    @ExcelProperty(index = 1)
//    private String stageName;

}
