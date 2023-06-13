package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 技术单位关联表
 * </p>
 *
 * @author bug
 * @since 2022-05-07
 */
@Getter
@Setter
@TableName("sys_tech_organization_relevance")
public class TechOrganizationRelevance extends BaseEntity {

    /**
     * 归属单位id
     */
    private String ownerId;

    /**
     * 技术单位id
     */
    private String orgId;

    /**
     * 服务级别
     */
    private String serviceLevel;


    public static final String OWNER_ID = "owner_id";

    public static final String ORG_ID = "org_id";

    public static final String SERVICE_LEVEL = "service_level";

}
