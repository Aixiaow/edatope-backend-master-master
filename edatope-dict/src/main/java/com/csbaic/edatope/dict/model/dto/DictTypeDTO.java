package com.csbaic.edatope.dict.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictTypeDTO {

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典类型描述")
    @DictProperty(type = "dict_type_mate", value = "type")
    private String typeDesc;
}
