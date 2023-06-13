package com.csbaic.edatope.app.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色类型
 */
public enum RolePropertyEnum {

    /**
     * 平台管理员
     */
    PLATFORM("D011-001"),
    /**
     * 管理管理员
     */
    ORG_ADMIN("D011-002"),

    /**
     * 单位职员
     */
    EMPLOYEE("D011-003");

    private String value;

    RolePropertyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
