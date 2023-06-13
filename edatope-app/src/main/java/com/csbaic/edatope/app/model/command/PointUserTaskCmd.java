package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author bnt
 * @Description 布点人员分配任务
 * @Date 2022/4/26 22:50
 */
@Data
public class PointUserTaskCmd {
    @NotEmpty
    @ApiModelProperty("工作任务阶段id")
    private List<String> blockWorkStageId;

    @NotBlank
    @ApiModelProperty("用户id")
    private String userId;
}
