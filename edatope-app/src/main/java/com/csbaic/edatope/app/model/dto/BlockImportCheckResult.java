package com.csbaic.edatope.app.model.dto;

import com.alibaba.excel.metadata.Cell;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlockImportCheckResult {

    @ApiModelProperty("表格行")
    private Integer  row;

    @ApiModelProperty("表格列")
    private Integer column;

    @ApiModelProperty("表格内容")
    private String content;

    @ApiModelProperty("错误提示")
    private String msg;


    public BlockImportCheckResult(Integer row, Integer column, String content, String msg) {
         this.row = row;
        this.column = column;
        this.content = content;
        this.msg = msg;
    }
}
