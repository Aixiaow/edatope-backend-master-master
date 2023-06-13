package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点质控审核意见
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Getter
@Setter
@TableName("sys_audit_opinion")
public class AuditOpinion extends BaseEntity {

    /**
     * 布点质控任务id
     */
    private String qualityControlTasksId;

    /**
     * 布点任务id
     */
    private String pointTasksId;

    /**
     * 地块工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 质控专家组id;关联专家组表(sys_specialist)id
     */
    private String qualitySpecialistId;

    /**
     * 专家组人员id;关联专家组组员表(sys_specialist_user)id
     */
    private String specialistUser;

    /**
     * 审核意见;pass=通过;back_perfect=退回完善;back_retrial=退回重审
     */
    private String auditOpinion;

    /**
     * 意见说明
     */
    private String opinionDesc;

    /**
     * 意见附件id;关联系统文件表(sys_app_file)id
     */
    private String opinionFileId;

    /**
     * 审核类型crew：组员提交，collect：汇总提交
     */
    private String auditType;


    public static final String QUALITY_CONTROL_TASKS_ID = "quality_control_tasks_id";

    public static final String POINT_TASKS_ID = "point_tasks_id";

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String QUALITY_SPECIALIST_ID = "quality_specialist_id";

    public static final String SPECIALIST_USER = "specialist_user";

    public static final String AUDIT_OPINION = "audit_opinion";

    public static final String OPINION_DESC = "opinion_desc";

    public static final String OPINION_FILE_ID = "opinion_file_id";

    public static final String AUDIT_TYPE = "audit_type";

}
