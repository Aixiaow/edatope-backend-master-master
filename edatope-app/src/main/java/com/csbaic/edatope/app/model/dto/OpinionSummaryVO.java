package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author bnt
 * @Description 质控专家组任务列表
 * @Date 2022/5/4 13:27
 */
@Data
public class OpinionSummaryVO {

    @ApiModelProperty("专家组名称")
    private String specialistName;

    @ApiModelProperty("审核信息")
    private List<SpecialistUserAuditInfo> auditInfos;

    @ApiModelProperty("用户审核资料")
    private AuditOpinionVO auditOpinionVO;

    @Data
    public static class SpecialistUserAuditInfo {

        @ApiModelProperty("专家性质")
        private String nature;

        @ApiModelProperty("专家性质描述")
        @DictProperty(type = "ExpertProperty", value = "nature")
        private String natureDesc;

        @ApiModelProperty("工作单位")
        private OrganizationDTO org;

        @ApiModelProperty("专家用户")
        private UserDTO user;

        @ApiModelProperty("专家身份")
        private String specialistIdentity;

        @ApiModelProperty("专家身份描述")
        @DictProperty(type = "ExpertIdentity", value = "specialistIdentity")
        private String specialistIdentityDesc;

        @ApiModelProperty("审核意见")
        private String auditOpinion;

        @ApiModelProperty("审核意见描述")
        private String auditOpinionDesc;

        @ApiModelProperty("意见说明")
        private String opinionDesc;

        @ApiModelProperty("意见附件")
        private String opinionFile;

        @ApiModelProperty("审核时间")
        private LocalDateTime createTime;
    }
}
