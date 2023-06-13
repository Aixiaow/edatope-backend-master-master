package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点质控任务
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Getter
@Setter
@TableName("sys_quality_control_tasks")
public class QualityControlTasks extends BaseEntity {

    /**
     * 地块id;关联地块表(sys_block)id
     */
    private String blockId;

    /**
     * 地块工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 布点任务id
     */
//    private Long pointTasksId;

    /**
     * 质控组织单位id
     */
    private String qualityOrgId;

    /**
     * 布点质控单位id
     */
    private String pointQualityOrgId;

    /**
     * 负责人id
     */
    private String principal;

    /**
     * 负责人手机号
     */
    private String principalPhone;

    /**
     * 质控专家组id
     */
    private String qualitySpecialistId;

    /**
     * 质控类型;
     */
    private String qualityType;

    /**
     * 布点质控单位任务分配状态;NOT_ALLOT 未分配 ALLOCATED 已分配 RECALL 已撤回
     */
    private String distributeStatus;

    /**
     * 方案审核状态;字典类型 D020
     */
    private String auditStatus;

    /**
     * 任务期限
     */
    private LocalDate deadline;

    /**
     * 布点质控专家组任务分配状态;NOT_ALLOT 未分配 ALLOCATED 已分配 RECALL 已撤回
     */
    private String distributeSpecialistStatus;

    /**
     * 授权调整操作人
     */
    private String authChangeUserId;

    /**
     * 授权调整操作时间
     */
    private LocalDateTime authChangeTime;

    /**
     * 布点质控意见退回时间
     */
    private LocalDateTime opinionBackTime;

    /**
     * 布点质控意见退回操作人
     */
    private String opinionBackUserId;

    public static final String BLOCK_ID = "block_id";

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String POINT_TASKS_ID = "point_tasks_id";

    public static final String QUALITY_ORG_ID = "quality_org_id";

    public static final String POINT_QUALITY_ORG_ID = "point_quality_org_id";

    public static final String QUALITY_SPECIALIST_ID = "quality_specialist_id";

    public static final String QUALITY_TYPE = "quality_type";

    public static final String DISTRIBUTE_STATUS = "distribute_status";

    public static final String AUDIT_STATUS = "audit_status";

    public static final String DEADLINE = "deadline";

    public static final String DISTRIBUTE_SPECIALIST_STATUS = "distribute_specialist_status";

    public static final String AUTH_CHANGE_USER_ID = "auth_change_user_id";

    public static final String AUTH_CHANGE_TIME = "auth_change_time";

    public static final String OPINION_BACK_TIME = "opinion_back_time";

    public static final String OPINION_BACK_USERID = "opinion_back_user_id";

}
