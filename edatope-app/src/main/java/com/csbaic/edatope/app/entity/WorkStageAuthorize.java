package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 工作阶段授权表
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@Getter
@Setter
@TableName("sys_work_stage_authorize")
public class WorkStageAuthorize extends BaseEntity {

    /**
     * 单位id
     */
    private String orgId;

    /**
     * 授权到期时间
     */
    private Date expireTime;

    /**
     * 授权状态
     */
    private String status;


    public static final String ORG_ID = "org_id";

    public static final String EXPIRE_TIME = "expire_time";

    public static final String STATUS = "status";

}
