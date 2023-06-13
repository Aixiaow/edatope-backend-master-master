package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检测能力表
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@Getter
@Setter
@TableName("sys_detection_capacity")
public class DetectionCapacity extends BaseEntity {

    /**
     * 样品类型
     */
    private String sampleType;

    /**
     * 指标类型
     */
    private String targetType;

    /**
     * 指标名称
     */
    private String targetName;

    /**
     * 检测方法名称
     */
    private String detectionMethodName;

    /**
     * 标准号
     */
    private String standardNumber;

    /**
     * 检测实验室
     */
    private String laboratory;

    /**
     * 行政管理单位
     */
    private String orgId;

    /**
     * 方法验证材料
     */
    private String materials;

    /**
     * 方法检出限
     */
    private BigDecimal restricts;

    /**
     * 计量单位
     */
    private String meterageUnit;

    /**
     * 是否挥发
     */
    private String volatilize;

    /**
     * 状态
     */
    private String status;


    public static final String SAMPLE_TYPE = "sample_type";

    public static final String TARGET_TYPE = "target_type";

    public static final String INDEX_NAME = "index_name";

    public static final String DETECTION_METHOD_NAME = "detection_method_name";

    public static final String STANDARD_NUMBER = "standard_number";

    public static final String LABORATORY = "laboratory";

    public static final String ORG_ID = "org_id";

    public static final String MATERIALS = "materials";

    public static final String RESTRICTS = "restricts";

    public static final String METERAGE_UNIT = "meterage_unit";

    public static final String VOLATILIZE = "volatilize";

    public static final String STATUS = "status";

}
