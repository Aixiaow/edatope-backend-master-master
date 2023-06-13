package com.csbaic.edatope.app.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PointImportVO {

    @ApiModelProperty("布点区域编号*")
    @ExcelProperty({"布点区域编号*"})
    private String pointAreaNumber;

    @ApiModelProperty("筛选依据*")
    @ExcelProperty("筛选依据*")
    private String selectBasis;

    @ApiModelProperty("布点区域类型")
    @ExcelProperty("布点区域类型")
    private String pointAreaType;

    /*@ApiModelProperty("样点编码*")
    @ExcelProperty("样点编码*")
    private String pointNumber;*/

    @ApiModelProperty("位置*")
    @ExcelProperty("位置*")
    private String location;

    @ApiModelProperty("经度*")
    @ExcelProperty("经度*")
    private String longitude;

    @ApiModelProperty("纬度*")
    @ExcelProperty("纬度*")
    private String latitude;

    @ApiModelProperty("样点类型*")
    @ExcelProperty("样点类型*")
    private String pointType;

    @ApiModelProperty("计划钻探深度/m*")
    @ExcelProperty("计划钻探深度/m*")
    private String depth;

    @ApiModelProperty("表层土壤检测指标分类")
    @ExcelProperty("表层土壤检测指标分类")
    private String surfaceTargetName;

    @ApiModelProperty("深层土壤检测指标分类")
    @ExcelProperty("深层土壤检测指标分类")
    private String deepTargetName;

    @ApiModelProperty("地下水检测指标分类")
    @ExcelProperty("地下水检测指标分类")
    private String waterTargetName;

}
