package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 工作阶段表
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
@Getter
@Setter
@TableName("sys_work_stage")
public class WorkStage extends BaseEntity {

    /**
     * 工作阶段名称
     */
    private String name;

    /**
     * 工作阶段描述
     */
    private String stageDesc;

    /**
     * 工作阶段状态
     */
    private String status;

    private transient List<WorkStageType> bizType;

    public static final String NAME = "name";

    public static final String STAGE_DESC = "stage_desc";

    public static final String STATUS = "status";

}
