package com.csbaic.edatope.log.enums;

/**
 * 操作类型
 */
public enum OperateType {
    /**
     * 没有操作操作类型
     */
    NONE(""),
    /**
     * 创建操作
     */
    CREATE("创建"),
    /**
     * 删除操作
     */
    DELETE("删除"),
    /**
     * 更新操作
     */
    UPDATE("更新"),
    /**
     * 查询操作
     */
    QUERY("查询"),
    /**
     * 登录操作
     */
    LOGIN("用户登录");


    OperateType(String remark) {
        this.remark = remark;
    }

    private final String remark;

    /**
     * 默认备注
     *
     * @return
     */
    public String getRemark() {
        return remark;
    }
}
