package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 被调查的企业
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Getter
@Setter
@TableName("sys_enterprise")
public class Enterprise extends BaseEntity {

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业统一信用编码
     */
    private String code;

    /**
     * 小分类
     */
    private String category;

    /**
     * 企业类型
     */
    private String type;


    public static final String NAME = "name";

    public static final String CODE = "code";

    public static final String CATEGORY = "category";

    public static final String TYPE = "type";

}
