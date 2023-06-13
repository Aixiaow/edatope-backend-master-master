package com.csbaic.edatope.app.enums;

public enum SurveyTaskStatusEnum {

    NOT_ALLOT("未分配"),

    ALLOCATED("已分配"),

    RECALL("已撤回");

    private String value;

    SurveyTaskStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据name查询中文value
     *
     * @param name
     * @return
     */
    public static String getValueByName(String name) {
        SurveyTaskStatusEnum[] values = SurveyTaskStatusEnum.values();
        for (SurveyTaskStatusEnum surveyTaskStatusEnum : values) {
            if (surveyTaskStatusEnum.name().equals(name)) {
                return surveyTaskStatusEnum.getValue();
            }
        }
        return "";
    }
}
