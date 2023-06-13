package com.csbaic.edatope.app.enums;

public enum FeedbackStatusEnum {

    pass("通过"),

    back_perfect("退回完善"),

    back_retrial("退回重审"),
    ;

    private String value;

    FeedbackStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
