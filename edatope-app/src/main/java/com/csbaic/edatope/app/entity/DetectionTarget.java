package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检测指标表
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("sys_detection_target")
public class DetectionTarget extends BaseEntity {

    /**
     * 指标id
     */
    private String targetNumber;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标类型
     */
    private String targetType;

    /**
     * 样品类型
     */
    private String sampleType;

    /**
     * 是否挥发
     */
    private String volatilize;

    /**
     * 状态
     */
    private String status;


    public static final String TARGET_NUMBER = "target_number";

    public static final String NAME = "name";

    public static final String TARGET_TYPE = "target_type";

    public static final String SAMPLE_TYPE = "sample_type";

    public static final String STATUS = "status";

}
