package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author bage
 * @since 2022-02-13
 */
@Getter
@Setter
@TableName("sys_acl_role")
public class Role extends BaseEntity {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色说明
     */
    private String description;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色类型
     */
    private String type;

    /**
     * 关联单位
     */
    private String orgId;

    /**
     * 角色性质
     */
    private String property;

    /**
     * 角色级别
     */
    private String level;

    /**
     * 角色状态
     */
    private String status;

    /**
     * 关联单位
     */
    private transient Organization org;
    /**
     * 角色的权限
     */
    private transient List<Permission> permissions;

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String CODE = "code";

    public static final String TYPE = "type";

    public static final String ORG_ID = "org_id";

    public static final String PROPERTY = "property";

    public static final String LEVEL = "level";

    public static final String STATUS = "status";

}
