package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检测指标分组表
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Getter
@Setter
@TableName("sys_detection_target_group")
public class DetectionTargetGroup extends BaseEntity {

    /**
     * 指标分类名称
     */
    private String name;

    /**
     * 样品类型
     */
    private String sampleType;

    /**
     * 是否挥发
     */
    private String volatilize;

    /**
     * 检测指标id
     */
    private String targetId;

    /**
     * 状态
     */
    private String status;


    public static final String NAME = "name";

    public static final String SAMPLE_TYPE = "sample_type";

    public static final String VOLATILIZE = "volatilize";

    public static final String TARGET_ID = "target_id";

    public static final String STATUS = "status";

}
