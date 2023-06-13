package com.csbaic.edatope.dict.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupDict {

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("字典列表")
    private List<DictDTO> dictList;

}
