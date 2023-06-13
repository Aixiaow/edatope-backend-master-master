package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 设备
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Data
public class DeviceDTO {

    @ApiModelProperty("设备id")
    private String id;
    /**
     * 设备型号
     */
    @ApiModelProperty("设备型号")
    private String model;

    /**
     * 设备品牌
     */
    @ApiModelProperty("设备品牌")
    private String brand;

    /**
     * 设备标识（取MEID）MEID
     */
    @ApiModelProperty("设备标识")
    private String identifier;


}
