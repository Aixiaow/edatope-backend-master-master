package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 检测指标分类表
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
@Getter
@Setter
@TableName("sys_detection_target_classify")
public class DetectionTargetClassify extends BaseEntity {

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
     * 检测实验室
     */
    private String detectionLabOrg;

    /**
     * 是否设置质控实验室
     */
    private String qualityLab;

    /**
     * 质控实验室
     */
    private String qualityLabOrg;

    /**
     * 布点单位
     */
    private String stationingLabOrg;

    /**
     * 样品保存容器
     */
    private String container;

    /**
     * 是否授权
     */
    private String authorization;

    /**
     * 状态
     */
    private String status;

    /**
     * 检测指标
     */
    private transient List<DetectionTargetClassifyType> target;

    public static final String NAME = "name";

    public static final String SAMPLE_TYPE = "sample_type";

    public static final String VOLATILIZE = "volatilize";

    public static final String TARGET_NUMBER = "target_number";

    public static final String DETECTION_LAB_ORG = "detection_lab_org";

    public static final String QUALITY_LAB = "quality_lab";

    public static final String QUALITY_LAB_ORG = "quality_lab_org";

    public static final String STATIONING_LAB_ORG = "stationing_lab_org";

    public static final String CONTAINER = "container";

    public static final String STATUS = "status";

}
