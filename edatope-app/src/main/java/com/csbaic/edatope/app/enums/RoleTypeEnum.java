package com.csbaic.edatope.app.enums;

import com.csbaic.edatope.app.entity.OrganizationBizType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 角色类型
 */
public enum RoleTypeEnum {

    /**
     * 平台角色
     */
    PLATFORM("D012-001"),
    /**
     * 行政管理单位
     */
    GOVERNMENT("D012-002"),

    /**
     * 布点单位
     */
    LAYOUT("D012-003"),
    /**
     * 布点质控
     */
    LAYOUT_QUALITY("D012-006"),
    /**
     * 采样
     */
    SAMPLE("D012-004"),
    /**
     * 采样质控
     */
    SAMPLE_QUALITY("D012-007"),
    /**
     * 质控实验室
     */
    QUALITY_LAB("D012-008"),
    /**
     * 检测实验室
     */
    CHECK_LAB("D012-005"),
    /**
     * 牵头单位
     */
    LEAD("D012-009");

    private String value;

    RoleTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static RoleTypeEnum fromValue(String value) {
        for (RoleTypeEnum roleTypeEnum : values()) {
            if (Objects.equals(roleTypeEnum.getValue(), value)) {
                return roleTypeEnum;
            }
        }
        return null;
    }

    /**
     * 获取所有的技术单位（不包含行政单位）
     *
     * @return
     */
    public static List<RoleTypeEnum> getRoleTypeByBizType(List<String> bizTypes) {
        if (bizTypes == null || bizTypes.isEmpty()) {
            return new ArrayList<>();
        }
        List<RoleTypeEnum> roles = new ArrayList<>();
        List<OrganizationBizTypeEnum> organizationBizTypeList = bizTypes.stream().map(OrganizationBizTypeEnum::fromValue).collect(Collectors.toList());
        for (OrganizationBizTypeEnum bizTypeEnum : organizationBizTypeList) {
            switch (bizTypeEnum) {

                case GOVERNMENT:
                    roles.add(RoleTypeEnum.GOVERNMENT);
                    break;
                case TECHNOLOGY_MANAGEMENT:
                    break;
                case LEAD:
                    roles.add(RoleTypeEnum.LEAD);
                    break;
                case LAYOUT:
                    roles.add(RoleTypeEnum.LAYOUT);
                    break;
                case LAYOUT_QUALITY:
                    roles.add(RoleTypeEnum.LAYOUT_QUALITY);
                    break;
                case SAMPLE:
                    roles.add(RoleTypeEnum.SAMPLE);
                    break;
                case SAMPLE_QUALITY:
                    roles.add(RoleTypeEnum.SAMPLE_QUALITY);
                    break;
                case QUALITY_LAB:
                    roles.add(RoleTypeEnum.QUALITY_LAB);
                    break;
                case CHECK_LAB:
                    roles.add(RoleTypeEnum.CHECK_LAB);
                    break;
            }
        }
        return roles;
    }
}
