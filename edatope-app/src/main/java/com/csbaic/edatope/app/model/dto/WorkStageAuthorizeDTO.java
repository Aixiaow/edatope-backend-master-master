package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.model.command.CreateWorkStageAuthorizeCmd;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class WorkStageAuthorizeDTO {

    /**
     * 授权记录id
     */
    @ApiModelProperty("授权记录id")
    private String id;

    @ApiModelProperty("是否启用")
    private String status;

    @ApiModelProperty("授权到期时间")
    private Date expireTime;

    @ApiModelProperty("单位")
    private OrganizationDTO organization;

    @ApiModelProperty("单位管理员")
    private UserDTO admin;

    @ApiModelProperty("剩余天数")
    private Integer residueDay;

    @ApiModelProperty("工作阶段及任务")
    private List<WorkStageAuthorizeDTO.StageList> stageList;

    @Data
    public static class StageList {
        @ApiModelProperty("工作阶段id")
        private String workStageId;

        @ApiModelProperty("工作阶段名称")
        private String workStageName;

        @ApiModelProperty("任务阶段")
        private List<String> bizType;

        @ApiModelProperty("任务阶段描述")
        private List<String> bizTypeDesc;
    }
}
