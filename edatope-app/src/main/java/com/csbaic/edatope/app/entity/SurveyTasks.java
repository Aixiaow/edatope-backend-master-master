package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 调查任务分配
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
@Getter
@Setter
@TableName("sys_survey_tasks")
public class SurveyTasks extends BaseEntity {

    /**
     * 地块id
     */
    private String blockId;

    /**
     * 工作任务阶段id
     */
    private String blockWorkStageId;

    /**
     * 任务期限
     */
    private LocalDate deadline;

    /**
     * 单位id
     */
    private String orgId;

    /**
     * 负责人id
     */
    private String principal;

    /**
     * 负责人id
     */
    private String principalPhone;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态
     */
    private String status;


    public static final String BLOCK_ID = "block_id";

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String DEADLINE = "deadline";

    public static final String ORG_ID = "org_id";

    public static final String PRINCIPAL = "principal";

    public static final String PRINCIPAL_PHONE = "principal_phone";

    public static final String TYPE = "type";

    public static final String STATUS = "status";

}
