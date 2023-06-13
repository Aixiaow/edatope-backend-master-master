package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统组织机构业务类型表
 * </p>
 *
 * @author bage
 * @since 2022-03-07
 */
@Getter
@Setter
@TableName("sys_organization_biz_type")
public class OrganizationBizType extends BaseEntity {

    /**
     * 组织机构id
     */
    private String orgId;

    /**
     * 业务类型
     */
    private String bizType;


    public static final String ORG_ID = "org_id";

    public static final String BIZ_TYPE = "biz_type";

}
