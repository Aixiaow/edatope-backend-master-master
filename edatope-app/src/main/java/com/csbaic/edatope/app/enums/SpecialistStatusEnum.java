package com.csbaic.edatope.app.enums;

/**
 * 专家组状态
 */
public enum SpecialistStatusEnum {
    DISABLE("停用"),
    NORMAL("正常");

    private String desc;

    SpecialistStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByName(String name){
        SpecialistStatusEnum[] values = SpecialistStatusEnum.values();
        for (SpecialistStatusEnum value : values) {
            if (value.name().equals(name)){
                return value.getDesc();
            }
        }
        return "";
    }
}
