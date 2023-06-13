package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 技术单位授权表
 * </p>
 *
 * @author bage
 * @since 2022-03-05
 */
@Getter
@Setter
@TableName("sys_tech_organization_authorize")
public class TechOrganizationAuthorize extends BaseEntity {

    /**
     * 归属单位id
     */
    private String ownerId;

    /**
     * 技术单位的单位id
     */
    private String orgId;

    /**
     * 技术负责人id
     */
    private String userId;

    /**
     * 技术单位角色id
     */
    private String roleId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 地址列表
     */
    private String areaCode;

    private transient List<TechOrganizationAuthorizeCity> authorizeCityList;

    public static final String OWNER_ID = "owner_id";

    public static final String ORG_ID = "org_id";

    public static final String USER_ID = "user_id";

    public static final String ROLE_ID = "role_id";

    public static final String ENABLED = "enabled";

    public static final String AREA_CODE = "area_code";
}
