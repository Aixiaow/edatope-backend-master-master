package com.csbaic.edatope.dict.model.command;

import com.csbaic.edatope.dict.model.dto.DictItemDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class DeleteDictCommand {


    @ApiModelProperty("字典Id")
    @NotEmpty(message = "字典id不能为空")
    private String id;

}
