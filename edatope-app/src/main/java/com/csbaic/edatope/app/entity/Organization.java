package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统组织机构表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Getter
@Setter
@TableName("sys_organization")
public class Organization extends BaseEntity {

    /**
     * 组织机构名称
     */
    private String name;

    /**
     * 组织机构编码
     */
    private String code;

    /**
     * 上级组织机构id
     */
    private String pid;

    /**
     * 组织机构性质
     */
    private String category;


    /**
     * 组织机构所在省
     */
    private String provinceCode;

    /**
     * 组织机构所在市
     */
    private String cityCode;

    /**
     * 组织机构所在区县
     */
    private String districtCode;

    /**
     * 组织机构地址详情
     */
    private String address;

    /**
     * 组织机构法人名称
     */
    private String legalPerson;

    /**
     * 组织机构联系电话
     */
    private String phone;

    /**
     * 服务级别
     */
    private String serviceLevel;

    /**
     * 组织机构成立日期
     */
    private LocalDate establishmentDate;

    /**
     * 组织机构状态
     */
    private String status;


    /**
     * 组织机构业务类型
     */
    private transient List<OrganizationBizType> bizType;

    /**
     * 单位管理员
     */
    private transient User admin;

    public static final String NAME = "name";

    public static final String CODE = "code";

    public static final String PID = "pid";

    public static final String CATEGORY = "category";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_CODE = "city_code";

    public static final String DISTRICT_CODE = "district_code";

    public static final String ADDRESS = "address";

    public static final String LEGAL_PERSON = "legal_person";

    public static final String PHONE = "phone";


    public static final String SERVICE_LEVEL = "service_Level";

    public static final String ESTABLISHMENT_DATE = "establishment_date";

    public static final String STATUS = "status";

}
