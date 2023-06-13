package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.enums.FeedbackStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FeedbackCmd {

    @NotBlank
    @ApiModelProperty("审核意见")
    private String auditOpinion;

//    @NotBlank
    @ApiModelProperty("意见说明")
    private String opinionDesc;

    @ApiModelProperty("布点质控任务id")
    private String qualityControlTasksId;

    private String fileId;

}
