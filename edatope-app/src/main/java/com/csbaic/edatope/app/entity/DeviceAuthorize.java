package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备授权
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Getter
@Setter
@TableName("sys_device_authorize")
public class DeviceAuthorize extends BaseEntity {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 单位id
     */
    private String orgId;

    /**
     * 授权人
     */
    private String authorizerId;

    /**
     * 首次登陆时间
     */
    private LocalDateTime firstLoginTime;

    /**
     * 授权时间
     */
    private LocalDateTime authorizeTime;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 授权状态
     */
    private String status;
    /**
     * 设置
     */
    private transient Device device;
    /**
     * 用户
     */
    private transient User user;
    /**
     * 授权人
     */
    private transient User authorizer;
    /**
     * 单位
     */
    private transient Organization organization;

    public static final String USER_ID = "user_id";

    public static final String ORG_ID = "org_id";

    public static final String AUTHORIZER_ID = "authorizer_id";

    public static final String FIRST_LOGIN_TIME = "first_login_time";

    public static final String AUTHORIZE_TIME = "authorize_time";

    public static final String DEVICE_ID = "device_id";

    public static final String STATUS = "status";

}
