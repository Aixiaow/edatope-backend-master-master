package com.csbaic.edatope.app.enums;

/**
 * @Author bnt
 * @Description 方案审核状态
 * @Date 2022/5/1 23:46
 */
public enum PlanAuditStatusEnum {

    CROSS("D020-001", "-"),
    STAY_AUDIT("D020-002", "待审核"),
    STAY_COLLECT("D020-003", "待汇总"),
    PASS("D020-004", "直接通过"),
    BACK_PERFECT("D020-005", "退回完善"),
    PERFECT_REVIEW("D020-006", "完善待复核"),
    PERFECT_REVIEW_PASS("D020-007", "完善后复核通过"),
    BACK_RETRIAL("D020-008", "退回重审"),
    RETRIAL_REVIEW("D020-009", "重审待复核"),
    RETRIAL_REVIEW_PASS("D020-010", "重审后复核通过"),
    BACK_MAINTAIN("D020-011", "退回维护"),
    ;

    private String value;

    private String desc;

    PlanAuditStatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByValue(String val){
        PlanAuditStatusEnum[] values = PlanAuditStatusEnum.values();
        for (PlanAuditStatusEnum statusEnum : values) {
            if (statusEnum.getValue().equals(val)){
                return statusEnum.getDesc();
            }
        }
        return "";
    }
}
