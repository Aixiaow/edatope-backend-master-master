package com.csbaic.edatope.app.enums;

/**
 * @Author bnt
 * @Description 操作事项
 * @Date 2022/4/28 22:07
 */
public enum OperateItemsEnum {
    DISTRIBUTE_TASK("分配布点单位任务"),
    RECALL_TASK("撤回布点单位任务"),
    DISTRIBUTE_POINT_USER_TASK("分配布点人员任务"),
    RECALL_POINT_USER_TASK("撤回布点人员任务"),
    SUBMIT_TASK("提交布点方案"),
    REFORM_SUBMIT_TASK("整改后提交布点方案"),
    DEFEND_SUBMIT_TASK("维护后提交布点方案"),
    RETURN_DEFEND_TASK("退回维护布点方案"),
    AUDIT_TASK("审核布点方案"),

    DISTRIBUTE_CONTROL_TASK("分配布点质控单位任务"),
    RECALL_CONTROL_TASK("撤回布点质控单位任务"),
    RETURN_OPINION_TASK("退回布点质控意见"),
    DISTRIBUTE_CONTROL_EXPERTS_TASK("分配布点质控专家组任务"),
    RECALL_CONTROL_EXPERTS_TASK("撤回布点质控专家组任务"),
    ;

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    OperateItemsEnum(String desc) {
        this.desc = desc;
    }
}
