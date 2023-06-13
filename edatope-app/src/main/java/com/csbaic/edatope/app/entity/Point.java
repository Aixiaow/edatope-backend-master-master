package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 点位结构化数据详情
 * </p>
 *
 * @author bage
 * @since 2022-04-25
 */
@Getter
@Setter
@TableName("sys_point")
public class Point extends BaseEntity {

    /**
     * 布点方案数据维护id;关联布点方案数据维护(sys_point_file)表id
     */
    private Long pointFileId;

    /**
     * 工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 布点区域编号
     */
    private String pointAreaNumber;

    /**
     * 筛选依据
     */
    private String selectBasis;

    /**
     * 布点区域类型
     */
    private String pointAreaType;

    /**
     * 样点编码
     */
    private String pointNumber;

    /**
     * 位置
     */
    private String location;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 样点类型;字典类型 D006
     */
    private String pointType;

    /**
     * 计划钻探深度/m
     */
    private String depth;

    /**
     * 表层土壤检测指标分类;关联检测指标分类表(sys_detection_target_classify)id
     */
    private String surfaceTarget;

    /**
     * 深层土壤检测指标分类;关联检测指标分类表(sys_detection_target_classify)id
     */
    private String deepTarget;

    /**
     * 地下水检测指标分类;关联检测指标分类表(sys_detection_target_classify)id
     */
    private String waterTarget;

    /**
     * 表层土壤检测指标分类名称;每个监测指标分类以中文“、”隔开
     */
    private String surfaceTargetName;

    /**
     * 深层土壤检测指标分类名称;每个监测指标分类以中文“、”隔开
     */
    private String deepTargetName;

    /**
     * 地下水检测指标分类名称;每个监测指标分类以中文“、”隔开
     */
    private String waterTargetName;

    /**
     * 行数
     */
    private Integer row;


    public static final String POINT_FILE_ID = "point_file_id";

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String POINT_AREA_NUMBER = "point_area_number";

    public static final String SELECT_BASIS = "select_basis";

    public static final String POINT_AREA_TYPE = "point_area_type";

    public static final String POINT_NUMBER = "point_number";

    public static final String LOCATION = "location";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String POINT_TYPE = "point_type";

    public static final String DEPTH = "depth";

    public static final String SURFACE_TARGET = "surface_target";

    public static final String DEEP_TARGET = "deep_target";

    public static final String WATER_TARGET = "water_target";

    public static final String SURFACE_TARGET_NAME = "surface_target_name";

    public static final String DEEP_TARGET_NAME = "deep_target_name";

    public static final String WATER_TARGET_NAME = "water_target_name";

    public static final String ROW = "row";

}
