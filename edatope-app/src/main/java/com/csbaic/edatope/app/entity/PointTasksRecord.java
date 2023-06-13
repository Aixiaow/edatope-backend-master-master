package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.app.enums.ServiceLevelEnum;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 布点任务操作记录
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
@Getter
@Setter
@TableName("sys_point_tasks_record")
public class PointTasksRecord extends BaseEntity {

    /**
     * 工作任务阶段id;关联工作阶段表(sys_work_stage)id
     */
    private String blockWorkStageId;

    /**
     * 单位id;关联系统组织机构表(sys_organization)id
     */
    private String orgId;

    /**
     * 操作人员;关联系统用户表(sys_user)id
     */
    private String userId;

    /**
     * 单位名称
     */
    private String orgName;

    /**
     * 操作人员名称
     */
    private String userName;

    /**
     * 操作事项;分配布点单位任务/撤回布点单位任务;分配布点人员任务/撤回布点人员任务;提交布点方案/整改后提交布点方案/维护后提交布点方案;退回维护布点方案、（组长）审核布点方案
     */
    private String operateItems;

    /**
     * 业务类型;布点 布点质控
     */
    private String operateType;

    /**
     * 服务级别;市级/省级/国家级
     */
    private String serviceLevel;

    /**
     * 服务级别英文
     */
    private String serviceLevelEnglish;

    /**
     * json字串
     */
    private String jsonDetail = "{}";

    /**
     * 布点方案当前状态
     */
    private String deployPointStatus;

    /**
     * 方案审核当前状态
     */
    private String auditStatus;


    public static final String BLOCK_WORK_STAGE_ID = "block_work_stage_id";

    public static final String ORG_ID = "org_id";

    public static final String USER_ID = "user_id";

    public static final String ORG_NAME = "org_name";

    public static final String USER_NAME = "user_name";

    public static final String OPERATE_ITEMS = "operate_items";

    public static final String OPERATE_TYPE = "operate_type";

    public static final String SERVICE_LEVEL = "service_level";

    public static final String JSON_DETAIL = "json_detail";

    public static final String DEPLOY_POINT_STATUS = "deploy_point_status";

    public static final String AUDIT_STATUS = "audit_status";

    public static final String SERVICE_LEVEL_ENGLISH = "service_level_english";

    public PointTasksRecord(String blockWorkStageId, String orgId, String userId, String orgName, String userName,
                            String operateItems, String operateType, String serviceLevel, String jsonDetail
            , String deployPointStatus, String auditStatus
    ) {
        this.blockWorkStageId = blockWorkStageId;
        this.orgId = orgId;
        this.userId = userId;
        this.orgName = orgName;
        this.userName = userName;
        this.operateItems = operateItems;
        this.operateType = operateType;
        this.serviceLevel = ServiceLevelEnum.getValueByName(serviceLevel);;
        this.serviceLevelEnglish = serviceLevel;
        this.jsonDetail = jsonDetail;
        this.deployPointStatus = deployPointStatus;
        this.auditStatus = auditStatus;
    }
}
