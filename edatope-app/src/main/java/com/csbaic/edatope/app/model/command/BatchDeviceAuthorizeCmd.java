package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 设备授权
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Data
public class BatchDeviceAuthorizeCmd {

    @NotEmpty(message = "授权id不能为空")
    @ApiModelProperty("授权id")
    private List<String> ids;

}
