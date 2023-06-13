package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 系统接口表
 * </p>
 *
 * @author bage
 * @since 2022-03-04
 */
@Getter
@Setter
@TableName("sys_acl_api")

public class Api extends BaseEntity {

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口组id
     */
    private String gid;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口是否可以匿名访问
     */
    private Boolean anonymous;

    /**
     * 菜单状态：正常、隐藏、禁用
     */
    private String status;

    /**
     * 接口排序
     */
    private Integer sort;

    /**
     * 是否是接口组
     */
    private Boolean apiGroup;

    /**
     * 权限编码
     */
    private String permissionCode;

    private transient List<Api> children;

    private transient Permission permission;

    private transient Api parent;

    public static final String NAME = "name";

    public static final String GID = "gid";

    public static final String PATH = "path";

    public static final String ANONYMOUS = "anonymous";

    public static final String STATUS = "status";

    public static final String SORT = "sort";

    public static final String API_GROUP = "api_group";

    public static final String PERMISSION_CODE = "permission_code";

}
