package com.csbaic.edatope.app.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 角色类型
 */
public enum OrganizationBizTypeEnum {

    /**
     * 行政管理单位
     */
    GOVERNMENT("D001-001"),
    /**
     * 技术管理单位
     */
    TECHNOLOGY_MANAGEMENT("D001-002"),
    /**
     * 牵头单位
     */
    LEAD("D001-003"),
    /**
     * 布点单位
     */
    LAYOUT("D001-004"),
    /**
     * 布点质控
     */
    LAYOUT_QUALITY("D001-007"),
    /**
     * 采样
     */
    SAMPLE("D001-005"),
    /**
     * 采样质控
     */
    SAMPLE_QUALITY("D001-008"),
    /**
     * 质控实验室
     */
    QUALITY_LAB("D001-009"),
    /**
     * 检测实验室
     */
    CHECK_LAB("D001-006");

    private String value;

    OrganizationBizTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrganizationBizTypeEnum fromValue(String value) {
        for (OrganizationBizTypeEnum typeEnum : values()) {
            if (Objects.equals(typeEnum.getValue(), value)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有的技术单位（不包含行政单位）
     *
     * @return
     */
    public static List<OrganizationBizTypeEnum> getTechOrganizationList() {
        List<OrganizationBizTypeEnum> techList = new ArrayList<>();
        for (OrganizationBizTypeEnum bizType : values()) {
            if (bizType != GOVERNMENT) {
                techList.add(bizType);
            }
        }
        return techList;
    }
}
