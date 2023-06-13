package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点人员任务分配
 * </p>
 *
 * @author bnt
 * @since 2022-04-26
 */
@Getter
@Setter
@TableName("sys_point_user_tasks")
public class PointUserTasks extends BaseEntity {

    /**
     * 地块id;关联地块表(sys_block)id
     */
    private String blockId;

    /**
     * 工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 布点人员id;关联系统用户表(sys_user)id
     */
    private String userId;

    /**
     * 分配状态;NOT_ALLOT:未分配;ALLOCATED:已分配;RECALL:已撤回
     */
    private String status;

    /**
     * 布点方案状态;字典类型 D018
     */
    private String deployPointStatus;

    /**
     * 任务期限
     */
    private LocalDate deadline;

    public static final String BLOCK_ID = "block_id";

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String USER_ID = "user_id";

    public static final String STATUS = "status";

    public static final String DEPLOY_POINT_STATUS = "deploy_point_status";

    public static final String DEADLINE = "deadline";

}
