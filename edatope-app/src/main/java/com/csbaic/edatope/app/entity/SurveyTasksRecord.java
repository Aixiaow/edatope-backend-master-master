package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 调查任务分配操作记录
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
@Getter
@Setter
@TableName("sys_survey_tasks_record")
public class SurveyTasksRecord extends BaseEntity {

    /**
     * 工作任务阶段id
     */
    private String blockWorkStageId;

    /**
     * 操作单位
     */
    private String orgId;

    /**
     * 操作人员
     */
    private String userId;

    /**
     * 委托方式
     */
    private String entrustWay;

    /**
     * 业务单位
     */
    private String businessOrg;

    /**
     * 类型
     */
    private String type;

    private String status;

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String ORG_ID = "org_id";

    public static final String USER_ID = "user_id";

    public static final String ENTRUST_WAY = "entrust_way";

    public static final String BUSINESS_ORG = "business_org";

    public static final String TYPE = "type";

}
