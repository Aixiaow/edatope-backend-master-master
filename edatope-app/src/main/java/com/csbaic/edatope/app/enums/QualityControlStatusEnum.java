package com.csbaic.edatope.app.enums;

/**
 * @Author bnt
 * @Description 布点质控任务状态
 * @Date 2022/5/5 12:28
 */
public enum QualityControlStatusEnum {
    WAIT_DISTRIBUTE("待分配", "('NOT_ALLOT','RECALL')"),
    ALREADY_DISTRIBUTE("已分配", "('ALLOCATED')"),
    WAIT_DEAL("待处理", "('D020-001','D020-002','D020-003','D020-006','D020-009')"),
    ALREADY_BACK("已退回", "('D020-005','D020-008','D020-011')"),
    PASS("已通过", "('D020-004','D020-007','D020-010')"),

    ;
    private String desc;

    private String columnValue;

    QualityControlStatusEnum(String desc, String columnValue) {
        this.desc = desc;
        this.columnValue = columnValue;
    }

    public String getDesc() {
        return desc;
    }

    public String getColumnValue() {
        return columnValue;
    }
}
