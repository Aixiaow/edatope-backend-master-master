package com.csbaic.edatope.dict.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.dict.annotation.DictProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统字典表
 * </p>
 *
 * @author bage
 * @since 2022-01-11
 */
@Getter
@Setter
@TableName("sys_dict")
public class Dict extends BaseEntity {

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 上级字典id
     */
    private String pid;

    /**
     * 状态
     */
    private String status;

    /**
     * 字典排序
     */
    private Integer sort;

    /**
     * 字典说明
     */
    private String description;


    public static final String NAME = "name";

    public static final String KEY = "key";

    public static final String VALUE = "value";

    public static final String TYPE = "type";

    public static final String PID = "pid";

    public static final String STATUS = "status";

    public static final String SORT = "sort";

    public static final String DESCRIPTION = "description";

}
