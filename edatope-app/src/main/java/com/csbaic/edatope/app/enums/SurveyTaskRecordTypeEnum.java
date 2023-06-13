package com.csbaic.edatope.app.enums;

public enum SurveyTaskRecordTypeEnum {

    ALLOT("分配"),

    RECALL("撤回");

    private String value;

    SurveyTaskRecordTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
