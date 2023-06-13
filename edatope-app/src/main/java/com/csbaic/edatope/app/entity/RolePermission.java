package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统角色与权限关联表
 * </p>
 *
 * @author bage
 * @since 2021-12-16
 */
@Getter
@Setter
@TableName("sys_acl_role_permission")
public class RolePermission extends BaseEntity {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String permissionId;


    public static final String ROLE_ID = "role_id";

    public static final String PERMISSION_ID = "permission_id";

}
