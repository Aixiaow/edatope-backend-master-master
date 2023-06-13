package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 设备授权
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Data
public class DeleteDeviceAuthorizeCmd {

    @ApiModelProperty("授权id")
    private String id;


}
