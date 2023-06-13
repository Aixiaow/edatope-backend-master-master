package com.csbaic.edatope.option.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
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
@TableName("sys_option")
public class Option extends BaseEntity {

    /**
     * 配置名称
     */
    private String name;

    /**
     * 配置键值
     */
    @TableField("`key`")
    private String key;

    /**
     * 配置值
     */
    private String value;

    /**
     * 配置描述
     */
    private String description;


    public static final String NAME = "name";

    public static final String KEY = "`key`";

    public static final String VALUE = "value";

    public static final String DESCRIPTION = "description";

}
