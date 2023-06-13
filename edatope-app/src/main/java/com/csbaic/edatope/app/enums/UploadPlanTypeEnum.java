package com.csbaic.edatope.app.enums;

public enum UploadPlanTypeEnum {

    plan("点位结构化数据"),

    planText("方案文本"),

    planAttach("方案附件"),

    selfOpinion("自审意见及整改说明"),
    ;

    private String value;

    UploadPlanTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
