package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 工作阶段任务类型表
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
@Getter
@Setter
@TableName("sys_work_stage_type")
public class WorkStageType extends BaseEntity {

    /**
     * 工作阶段id
     */
    private String stageId;

    /**
     * 业务类型
     */
    private String bizType;


    public static final String STAGE_ID = "stage_id";

    public static final String BIZ_TYPE = "biz_type";

}
