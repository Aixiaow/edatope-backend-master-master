package com.csbaic.edatope.app.enums;

public enum  UserType {
    /**
     * 单位管理员
     */
    ORGANIZATION_ADMIN("D011-002");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
