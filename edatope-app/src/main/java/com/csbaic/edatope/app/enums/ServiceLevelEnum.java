package com.csbaic.edatope.app.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务级别
 */
public enum ServiceLevelEnum {

    /**
     * 国家级
     */
    COUNTRY_LEVEL("国家级"),
    /**
     * 省级
     */
    PROVINCE_LEVEL("省级"),
    /**
     * 市级
     */
    CITY_LEVEL("市级"),
    /**
     * 县级
     */
    DISTRICT_LEVEL("县级");

    private String value;

    private String dictValue;

    ServiceLevelEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValueByName(String name){
        ServiceLevelEnum[] values = ServiceLevelEnum.values();
        for (ServiceLevelEnum serviceLevelEnum : values) {
            if (serviceLevelEnum.name().equals(name)){
                return serviceLevelEnum.getValue();
            }
        }
        return "";
    }
}
