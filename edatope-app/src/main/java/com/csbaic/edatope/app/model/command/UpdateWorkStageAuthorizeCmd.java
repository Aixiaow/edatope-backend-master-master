package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UpdateWorkStageAuthorizeCmd {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("到期时间")
    private String expireTime;

    @ApiModelProperty("工作阶段状态。ALREADY 已授权，STAY 待授权")
    private String status;

    @ApiModelProperty("工作阶段及任务")
    private List<WorkStageList> stageList;

    @Data
    public static class WorkStageList {

        @ApiModelProperty("工作阶段id")
        private String workStageId;

        @ApiModelProperty("任务阶段")
        private Set<String> bizType;
    }
}
