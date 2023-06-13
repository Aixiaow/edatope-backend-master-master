package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 技术单位授权城市表
 * </p>
 *
 * @author bage
 * @since 2022-04-17
 */
@Getter
@Setter
@TableName("sys_tech_organization_authorize_city")
public class TechOrganizationAuthorizeCity extends BaseEntity {

    /**
     * 技术单位授权id
     */
    private String authorizeId;

    /**
     * 授权所在省
     */
    private String provinceCode;

    /**
     * 授权所在市
     */
    private String cityCode;

    /**
     * 授权所在区县
     */
    private String districtCode;


    public static final String AUTHORIZE_ID = "authorize_id";

    public static final String PROVINCE_CODE = "province_code";

    public static final String CITY_CODE = "city_code";

    public static final String DISTRICT_CODE = "district_code";

}
