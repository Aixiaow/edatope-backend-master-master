package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统角色与用户关联表
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Getter
@Setter
@TableName("sys_acl_user_role")
public class UserRole extends BaseEntity {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 用户id
     */
    private String userId;


    public static final String ROLE_ID = "role_id";

    public static final String USER_ID = "user_id";

}
