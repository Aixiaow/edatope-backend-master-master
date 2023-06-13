package com.csbaic.edatope.app.enums;

import java.util.Objects;

/**
 * 布点方案状态
 */
public enum PointStatusEnum {

    /**
     * 未处理
     */
    untreated("D018-001"),
    /**
     * 维护中
     */
    maintenance("D018-002"),
    /**
     * 待提交
     */
    stay_submit("D018-003"),
    /**
     * 已提交
     */
    submit("D018-004"),
    /**
     * 退回维护
     */
    back_maintain("D018-005"),
    /**
     * 维护待审核
     */
    maintain_audit("D018-006"),
    /**
     * 直接通过
     */
    pass("D018-007"),
    /**
     * 退回完善
     */
    back_perfect("D018-008"),
    /**
     * 完善待复核
     */
    perfect_review("D018-009"),
    /**
     * 完善后复核通过
     */
    perfect_review_pass("D018-010"),
    /**
     * 退回重审
     */
    back_retrial("D018-011"),
    /**
     * 重审待复核
     */
    retrial_review("D018-012"),
    /**
     * 重审后复核通过
     */
    retrial_review_pass("D018-013");

    private String value;

    PointStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PointStatusEnum fromValue(String value) {
        for (PointStatusEnum typeEnum : values()) {
            if (Objects.equals(typeEnum.getValue(), value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
