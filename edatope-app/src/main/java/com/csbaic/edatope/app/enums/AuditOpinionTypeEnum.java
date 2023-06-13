package com.csbaic.edatope.app.enums;

public enum AuditOpinionTypeEnum {

    crew("组员提交"),
    collect("汇总提交(组长)"),
    ;

    private String value;

    AuditOpinionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
