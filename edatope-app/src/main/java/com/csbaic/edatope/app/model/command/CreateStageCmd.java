package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
public class CreateStageCmd {

    @ApiModelProperty("工作阶段名称")
    @NotEmpty(message = "请填写阶段名称，限50个字以内")
    @Length(max = 50, message = "请填写阶段名称，限50个字以内")
    private String name;

    @ApiModelProperty("工作阶段描述")
    @NotEmpty(message = "请填写阶段描述名称，限200个字以内")
    @Length(max = 200, message = "请填写阶段描述名称，限200个字以内")
    private String stageDesc;

    @ApiModelProperty("任务阶段")
    @NotEmpty(message = "请选择当前工作阶段可执行的任务阶段")
    private List<String> bizType;
}
