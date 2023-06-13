package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检测方法表
 * </p>
 *
 * @author bage
 * @since 2022-04-02
 */
@Getter
@Setter
@TableName("sys_detection_method")
public class DetectionMethod extends BaseEntity {

    /**
     * 指标id
     */
    private String targetId;

    /**
     * 标准号
     */
    private String standard;

    /**
     * 检测方法名称
     */
    private String name;

    /**
     * 方法检出限
     */
    private BigDecimal restricts;

    /**
     * 计量单位
     */
    private String meterageUnit;

    /**
     * 有效保存时间
     */
    private String holdTime;

    /**
     * 样品保存条件
     */
    private String holdCondition;

    /**
     * 指标方法状态
     */
    private String status;


    public static final String TARGET_ID = "target_id";

    public static final String STANDARD = "standard";

    public static final String NAME = "name";

    public static final String RESTRICT = "restrict";

    public static final String METERAGE_UNIT = "meterage_unit";

    public static final String HOLD_TIME = "hold_time";

    public static final String HOLD_CONDITION = "hold_condition";

    public static final String STATUS = "status";

}
