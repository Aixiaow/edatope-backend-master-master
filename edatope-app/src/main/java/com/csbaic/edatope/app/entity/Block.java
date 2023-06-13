package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地块
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Getter
@Setter
@TableName("sys_block")
public class Block extends BaseEntity {

    /**
     * 地块编码
     */
    private String code;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 企业名称
     */
    private String enterpriseId;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 地区所属省份编码
     */
    private String provinceCode;

    /**
     * 地区所属城市编码
     */
    private String cityCode;

    /**
     * 地区所属区县编码
     */
    private String districtCode;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 纬度符号
     */
    private String latitudeFlag;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 经度符号
     */
    private String longitudeFlag;

    /**
     * 地块地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String contactPhone;


    private transient Project project;

    private transient Enterprise enterprise;

     private transient List<BlockWorkStage> workStageList;


   public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String ENTERPRISE_ID = "enterprise_id";

    public static final String PROJECT_ID = "project_id";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_CODE = "city_code";

    public static final String DISTRICT_CODE = "district_code";

    public static final String LATITUDE = "latitude";

    public static final String LATITUDE_FLAG = "latitude_flag";

    public static final String LONGITUDE = "longitude";

    public static final String LONGITUDE_FLAG = "longitude_flag";

    public static final String ADDRESS = "address";

    public static final String CONTACT = "contact";

    public static final String CONTACT_PHONE = "contact_phone";

}
