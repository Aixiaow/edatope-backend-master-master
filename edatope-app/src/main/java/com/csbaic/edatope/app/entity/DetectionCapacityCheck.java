package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 检测能力审核表
 * </p>
 *
 * @author bage
 * @since 2022-04-04
 */
@Getter
@Setter
@TableName("sys_detection_capacity_check")
public class DetectionCapacityCheck extends BaseEntity {

    /**
     * 检测能力id
     */
    private String capacityId;

    /**
     * 审核单位
     */
    private String orgId;

    /**
     * 审核单位
     */
    private String checkUserId;

    /**
     * 审核节点
     */
    private String checkNode;

    /**
     * 审核备注
     */
    private String checkRemark;

    /**
     * 附件地址
     */
    private String accessoryPath;

    /**
     * 状态
     */
    private String status;


    public static final String ORG_ID = "org_id";

    public static final String CAPACITY_Id = "capacity_id";

    public static final String CHECK_USER_ID = "check_user_id";

    public static final String CHECK_NODE = "check_node";

    public static final String CHECK_REMARK = "check_remark";

    public static final String ACCESSORY_PATH = "accessory_path";

    public static final String STATUS = "status";

}
