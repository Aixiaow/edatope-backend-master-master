package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 授权任务阶段表
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("sys_work_stage_authorize_task")
public class WorkStageAuthorizeTask extends BaseEntity {

    /**
     * 授权记录id
     */
    private String stageAuthorizeId;

    /**
     * 工作阶段id
     */
    private String stageId;

    /**
     * 任务阶段
     */
    private String bizType;


    public static final String STAGE_AUTHORIZE_ID = "stage_authorize_id";

    public static final String STAGE_ID = "stage_id";

    public static final String BIZ_TYPE = "biz_type";

}
