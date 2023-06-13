package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地块工作阶段
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@Getter
@Setter
@TableName("sys_block_work_stage")
public class BlockWorkStage extends BaseEntity {

    /**
     * 地区名称
     */
    private String name;

    /**
     * 项目id
     */
    private String blockId;

    /**
     * 工作任务id
     */
    private String workStageId;

    /**
     * 任务期限
     */
    private LocalDate deadline;

    /**
     * 是否需要核实
     */
    private Boolean verify;

    private String orgId;

    // 委托方式
    private String entrustWay;
    /**
     * 工作阶段状态
     */
    private String status;


    public static final String NAME = "name";

    public static final String BLOCK_ID = "block_id";

    public static final String WORK_STAGE_ID = "work_stage_id";

    public static final String DEADLINE = "deadline";

    public static final String VERIFY = "verify";

    public static final String ORG_ID = "org_id";

    public static final String STATUS = "status";

    public static final String ENTRUST_WAY = "entrust_way";

}
