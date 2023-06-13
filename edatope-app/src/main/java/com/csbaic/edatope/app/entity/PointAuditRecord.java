package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点方案审核记录
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
@Getter
@Setter
@TableName("sys_point_audit_record")
public class PointAuditRecord extends BaseEntity {

    /**
     * 工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 操作单位;关联系统组织机构表(sys_organization)id
     */
    private String orgId;

    /**
     * 操作人员;关联系统用户表(sys_user)id
     */
    private String userId;

    /**
     * 操作单位名称
     */
    private String orgName;

    /**
     * 操作人员名称
     */
    private String userName;

    /**
     * 业务类型;布点 布点质控
     */
    private String operateType;

    /**
     * 服务级别;市级/省级/国家级
     */
    private String serviceLevel;

    /**
     * 服务单位id
     */
    private String serviceOrgId;

    /**
     * 服务单位名称
     */
    private String serviceOrgName;

    /**
     * 质控类型;市级质控/省级质控/国家级质控
     */
    private String auditType;

    /**
     * 操作事项;提交布点方案/整改后提交布点方案/维护后提交布点方案/审核布点方案
     */
    private String operateItems;

    /**
     * 审核意见;直接通过/退回完善/完善待复核/完善后复核通过/退回重审/重审待复核/重审后复核通过/退回维护
     */
    private String auditOpinion;

    /**
     * 意见说明
     */
    private String opinionExplain;

    /**
     * 意见附件id;关联系统文件表(sys_app_file)id
     */
    private Long opinionFile;

    /**
     * 意见附加url
     */
    private String opinionFileUrl;

    /**
     * 方案json
     */
    private String pointFileJson;

    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String ORG_ID = "org_id";

    public static final String USER_ID = "user_id";

    public static final String ORG_NAME = "org_name";

    public static final String USER_NAME = "user_name";

    public static final String OPERATE_TYPE = "operate_type";

    public static final String SERVICE_LEVEL = "service_level";

    public static final String AUDIT_TYPE = "audit_type";

    public static final String OPERATE_ITEMS = "operate_items";

    public static final String AUDIT_OPINION = "audit_opinion";

    public static final String OPINION_EXPLAIN = "opinion_explain";

    public static final String OPINION_FILE = "opinion_file";

    public static final String OPINION_FILE_URL = "opinion_file_url";

    public static final String SERVICE_ORG_ID = "service_org_id";

    public static final String SERVICE_ORG_NAME = "service_org_name";

    public static final String POINT_FILE_JSON = "point_file_json";

    public PointAuditRecord(String blockWorkStageId, String orgId, String userId, String orgName, String userName,
                            String operateType, String serviceLevel, String serviceOrgId, String serviceOrgName,
                            String operateItems, String pointFileJson) {
        this.blockWorkStageId = blockWorkStageId;
        this.orgId = orgId;
        this.userId = userId;
        this.orgName = orgName;
        this.userName = userName;
        this.operateType = operateType;
        this.serviceLevel = serviceLevel;
        this.serviceOrgId = serviceOrgId;
        this.serviceOrgName = serviceOrgName;
        this.operateItems = operateItems;
        this.pointFileJson = pointFileJson;
    }


}
