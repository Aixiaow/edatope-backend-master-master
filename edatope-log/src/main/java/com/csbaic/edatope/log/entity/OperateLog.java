package com.csbaic.edatope.log.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 业务操作日志
 * </p>
 *
 * @author bage
 * @since 2022-01-19
 */
@Getter
@Setter
@TableName("sys_operate_log")
public class OperateLog extends BaseEntity {

    /**
     * 日志类型（登陆日志，操作日志，请求日志，错误日志）
     */
    private String logType;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作人id
     */
    private String operatorId;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 日志备注
     */
    private String remark;

    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求实体
     */
    private String requestBody;

    /**
     * 请求ip地址
     */
    private String requestAddr;

    /**
     * 关联目标id
     */
    private String targetId;

    /**
     * 操作对象名称
     */
    private String targetName;


    /**
     * 操作时间
     */
    private LocalDateTime operateTime;


    public static final String LOG_TYPE = "log_type";

    public static final String MODULE = "module";

    public static final String OPERATOR_NAME = "operator_name";

    public static final String OPERATOR_ID = "operator_id";

    public static final String OPERATE_TYPE = "operate_type";

    public static final String REMARK = "remark";

    public static final String REQUEST_PATH = "request_path";

    public static final String REQUEST_PARAM = "request_param";

    public static final String REQUEST_BODY = "request_body";

    public static final String REQUEST_ADDR = "request_addr";

    public static final String TARGET_ID = "target_id";

    public static final String TARGET_NAME = "target_name";

    public static final String OPERATE_TIME = "operate_time";

}
