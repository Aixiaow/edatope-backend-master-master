package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 指标分类维护指标关系表
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
@Getter
@Setter
@TableName("sys_detection_target_classify_type")
public class DetectionTargetClassifyType extends BaseEntity {

    /**
     * 检测指标分类id
     */
    private String targetClassifyId;

    /**
     * 业务类型
     */
    private String targetId;


    public static final String TARGET_CLASSIFY_ID = "target_classify_id";

    public static final String TARGET_ID = "target_id";

}
