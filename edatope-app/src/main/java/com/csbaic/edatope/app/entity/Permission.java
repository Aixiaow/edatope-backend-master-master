package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author bage
 * @since 2021-12-16
 */
@Getter
@Setter
@TableName("sys_acl_permission")
public class Permission extends BaseEntity {

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限编码
     */
    private String code;

    /**
     * 上组权限id
     */
    private String pid;

    /**
     * 权限状态
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 权限类型
     */
    private String type;

    public static final String NAME = "name";

    public static final String CODE = "code";

    public static final String PID = "pid";

    public static final String STATUS = "status";

    public static final String TYPE = "type";

    public static final String SORT = "sort";
}
