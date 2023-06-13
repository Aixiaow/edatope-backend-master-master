package com.csbaic.edatope.app.enums;

/**
 * 专家身份
 */
public enum SpecialistIdentityEnum {
    INTERNAL_EXPERTS("D022-001", "内部专家"),
    OUTSIDE_EXPERTS("D022-002", "外聘专家");
    private String value;

    private String desc;

    SpecialistIdentityEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    public static String getDescByValue(String val){
        SpecialistIdentityEnum[] values = SpecialistIdentityEnum.values();
        for (SpecialistIdentityEnum value : values) {
            if (value.getValue().equals(val)){
                return value.getDesc();
            }
        }
        return "";
    }
}
