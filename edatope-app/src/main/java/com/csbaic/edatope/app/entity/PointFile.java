package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点方案数据维护
 * </p>
 *
 * @author bug
 * @since 2022-04-26
 */
@Getter
@Setter
@TableName("sys_point_file")
public class PointFile extends BaseEntity {

    /**
     * 地块工作阶段id关联block_work_stage表
     */
    private String blockWorkStageId;

    /**
     * 方案文本;关联系统文件表(sys_app_file)id
     */
    private String fileId;

    /**
     * 类型;1：方案文本；2：方案附件；3：自审意见及整改说明；4：点位结构化数据
     */
    private String type;


    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String FILE_Id = "file_id";

    public static final String TYPE = "type";

}
