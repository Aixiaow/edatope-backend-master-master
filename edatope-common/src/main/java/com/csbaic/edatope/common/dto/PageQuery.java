package com.csbaic.edatope.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class PageQuery {

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "分页参数不正确")
    private int pageIndex;

    @ApiModelProperty( value = "分页大小", example = "20")
    @Min(value = 1, message = "分页参数不正确")
    @Max(value = 500, message = "分页大小不能超过500")
    private int pageSize;


}
