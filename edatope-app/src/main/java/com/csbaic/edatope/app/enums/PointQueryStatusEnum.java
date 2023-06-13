package com.csbaic.edatope.app.enums;

/**
 * 布点任务状态查询
 */
public enum PointQueryStatusEnum {

    WAIT_DISTRIBUTE("待分配", "('NOT_ALLOT','RECALL')"),
    ALREADY_DISTRIBUTE("已分配", "('ALLOCATED')"),
    WAIT_DEAL("待处理","('D018-001','D018-002','D018-003')"),
    ALREADY_SUBMIT("已提交","('D018-004')"),
    WAIT_REFORM("布点方案数据整改-待整改","('D018-008','D018-011')"),
    WAIT_AUDIT("布点方案数据整改-待审核","('D018-009','D018-012')"),
    POINT_WAIT_REFORM("采样过程点位调整-待整改","('D018-005')"),
    POINT_WAIT_AUDIT("采样过程点位调整-待审核","('D018-006')"),

    ;
    private String desc;

    private String columnValue;

    PointQueryStatusEnum(String desc, String columnValue) {
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
