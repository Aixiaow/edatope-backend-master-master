package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 指标分类审核表
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Getter
@Setter
@TableName("sys_target_classify_check")
public class TargetClassifyCheck extends BaseEntity {

    /**
     * 检测能力记录id
     */
    private String targetClassifyId;

    /**
     * 操作单位
     */
    private String orgId;

    /**
     * 操作人寰
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
     * 状态
     */
    private String status;


    public static final String TARGET_CLASSIFY_ID = "target_classify_id";

    public static final String ORG_ID = "org_id";

    public static final String CHECK_USER_ID = "check_user_id";

    public static final String CHECK_NODE = "check_node";

    public static final String CHECK_REMARK = "check_remark";

    public static final String STATUS = "status";

}
