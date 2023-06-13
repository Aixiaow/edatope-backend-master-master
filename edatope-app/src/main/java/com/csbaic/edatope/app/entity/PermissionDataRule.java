package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数据权限规则
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Getter
@Setter
@TableName("sys_acl_permission_data_rule")
public class PermissionDataRule extends BaseEntity {

    /**
     * 权限id
     */
    private String permissionId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 字段名称
     */
    private String column;

    /**
     * 规则条件
     */
    private String conditions;

    /**
     * 规则值
     */
    private String value;

    /**
     * 权限有效状态1有0否
     */
    private String status;


    public static final String PERMISSION_ID = "permission_id";

    public static final String RULE_NAME = "rule_name";

    public static final String COLUMN = "column";

    public static final String CONDITIONS = "conditions";

    public static final String VALUE = "value";

    public static final String STATUS = "status";

}
