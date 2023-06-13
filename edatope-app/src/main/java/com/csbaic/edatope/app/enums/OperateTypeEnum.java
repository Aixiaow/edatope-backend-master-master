package com.csbaic.edatope.app.enums;

/**
 * @Author bnt
 * @Description 业务类型
 * @Date 2022/4/28 23:10
 */
public enum OperateTypeEnum {
    SURVEY_TASK("调查任务"),
    POINT_TASK("布点"),
    POINT_CONTROL_TASK("布点质控");

    private String desc;

    public String getDesc() {
        return desc;
    }

    OperateTypeEnum(String desc) {
        this.desc = desc;
    }
}
