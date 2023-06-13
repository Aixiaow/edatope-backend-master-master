package com.csbaic.edatope.app.enums;

/**
 * 专家性质
 */
public enum SpecialistNatureEnum {
    GROUP_LEADER("D021-001", "组长"),
    GROUP_MEMBER("D021-002", "组员");
    private String value;

    private String desc;

    SpecialistNatureEnum(String value, String desc) {
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
        SpecialistNatureEnum[] values = SpecialistNatureEnum.values();
        for (SpecialistNatureEnum value : values) {
            if (value.getValue().equals(val)){
                return value.getDesc();
            }
        }
        return "";
    }
}
