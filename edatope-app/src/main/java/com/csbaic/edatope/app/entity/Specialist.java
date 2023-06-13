package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 专家组表
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Getter
@Setter
@TableName("sys_specialist")
public class Specialist extends BaseEntity {

    /**
     * 专家组名称
     */
    private String groupName;

    /**
     * 专家组状态;DISABLE=停用 NORMAL=正常
     */
    private String status;

    /**
     * 组员md5
     */
    private String groupUser;

    /**
     * 专家组组员名称
     */
    private String groupUserName;

    /**
     * 所属单位id
     */
    private String orgId;


    public static final String GROUP_NAME = "group_name";

    public static final String STATUS = "status";

    public static final String GROUP_USER_NAME = "group_user_name";

    public static final String ORG_ID = "org_id";

}
