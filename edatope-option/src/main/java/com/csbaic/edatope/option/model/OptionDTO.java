package com.csbaic.edatope.option.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author bage
 * @since 2022-01-05
 */
@Getter
@Setter
public class OptionDTO     {

    @ApiModelProperty("配置id")
    private String id;
    /**
     * 配置名称
     */
    @ApiModelProperty("配置名称")
    private String name;

    /**
     * 配置键值
     */
    @ApiModelProperty("配置键")
    private String key;

    /**
     * 配置值
     */
    @ApiModelProperty("配置键值")
    private String value;

    /**
     * 配置描述
     */
    @ApiModelProperty("配置描述")
    private String description;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人姓名")
    private String createByName;

}
