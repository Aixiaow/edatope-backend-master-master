package com.csbaic.edatope.app.enums;

public enum AuditStatusEnum {

    ToAudit("待审核"),

    AlreadyPassed("已通过"),

    NotPass("未通过");

    private String value;

    AuditStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
