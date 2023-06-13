package com.csbaic.edatope.app.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BlockImportVO {

    @ApiModelProperty("序号")
    @ExcelProperty({"序号"})
    private String index;


    @ApiModelProperty("地块名称")
    @ExcelProperty("地块名称")
    private String name;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    @ExcelProperty("经度")
    private String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    @ExcelProperty("纬度")
    private String latitude;

    /**
     * 行业分类
     */
    @ApiModelProperty("行业分类")
    @ExcelProperty("行业分类")
    private String category;

    @ApiModelProperty("所属地区-省")
    @ExcelProperty("所属地区-省")
    private String province;

    @ApiModelProperty("所属地区-市")
    @ExcelProperty("所属地区-市")
    private String city;

    @ApiModelProperty("所属地区-县")
    @ExcelProperty("所属地区-县")
    private String district;

    @ExcelProperty("联系人")
    private String contact;

    @ExcelProperty("联系电话")
    private String contactPhone;

    @ExcelProperty("企业名称")
    private String enterpriseName;

    @ExcelProperty("统一社会信用代码")
    private String enterpriseCode;

    @ExcelProperty("企业类型")
    private String enterpriseType;

    @ExcelProperty("项目名称")
    private String projectName;

}
