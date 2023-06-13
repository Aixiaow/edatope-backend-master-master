package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 设备
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Getter
@Setter
@TableName("sys_device")
public class Device extends BaseEntity {

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备品牌
     */
    private String brand;

    /**
     * 设备标识（取MEID）MEID
     */
    private String identifier;


    public static final String MODEL = "model";

    public static final String BRAND = "brand";

    public static final String IDENTIFIER = "identifier";

}
