package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备授权
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Data
public class DeviceAuthorizeDTO {

    private String id;


    /**
     * 首次登陆时间
     */
    @ApiModelProperty("首先登陆时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime firstLoginTime;

    /**
     * 授权时间
     */
    @ApiModelProperty("授权时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime authorizeTime;


    /**
     * 授权状态
     */
    @ApiModelProperty("授权状态")
    private String status;

    /**
     * 授权状态
     */
    @ApiModelProperty("授权状态")
    @DictProperty(value = "status", type = "device_authorize_status")
    private String statusDesc;

    /**
     * 设置
     */
    @ApiModelProperty("设备")
    private DeviceDTO device;
    /**
     * 用户
     */
    @ApiModelProperty("用户")
    private UserDTO user;
    /**
     * 授权人
     */
    @ApiModelProperty("授权人")
    private UserDTO authorizer;
    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private OrganizationDTO organization;


}
