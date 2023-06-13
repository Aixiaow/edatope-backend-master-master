package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class CreateWorkStageAuthorizeCmd {

    @ApiModelProperty("单位id")
    private List<String> orgId;

    @ApiModelProperty("到期时间")
    private String expireTime;

    @ApiModelProperty("工作阶段及任务")
    private List<StageList> stageList;

    @Data
    public static class StageList {
        @ApiModelProperty("工作阶段id")
        private String workStageId;

        @ApiModelProperty("任务阶段")
        private Set<String> bizType;
    }
}
