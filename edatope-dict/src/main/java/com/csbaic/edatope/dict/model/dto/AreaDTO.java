package com.csbaic.edatope.dict.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AreaDTO {

    @ApiModelProperty("省份名称")
    @DictProperty(value = "provinceCode", type = "area")
    private String provinceName;
    @ApiModelProperty("省份code")
    private String provinceCode;
    @ApiModelProperty("城市code")
    private String cityCode;
    @ApiModelProperty("城市名称")
    @DictProperty(value = "cityCode", type = "area")
    private String cityName;
    @ApiModelProperty("地区名称")
    private String districtCode;
    @ApiModelProperty("地区名称")
    @DictProperty(value = "districtCode", type = "area")
    private String districtName;

}
