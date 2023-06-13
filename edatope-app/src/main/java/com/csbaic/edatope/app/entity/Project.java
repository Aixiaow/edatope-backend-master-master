package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author bage
 * @since 2022-04-04
 */
@Getter
@Setter
@TableName("sys_project")
public class Project extends BaseEntity {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目所属省份编码
     */
    private String provinceCode;

    /**
     * 项目所属城市编码
     */
    private String cityCode;

    /**
     * 项目所属区县编码
     */
    private String districtCode;

    /**
     * 项目开始日期
     */
    private LocalDate beginDate;

    /**
     * 项目结束日期
     */
    private LocalDate endDate;

    /**
     * 项目备注
     */
    private String remark;

    private String orgId;


    public static final String NAME = "name";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_CODE = "city_code";

    public static final String DISTRICT_CODE = "district_code";

    public static final String BEGIN_DATE = "begin_date";

    public static final String END_DATE = "end_date";

    public static final String REMARK = "remark";

    public static final String ORG_ID = "org_id";

}
