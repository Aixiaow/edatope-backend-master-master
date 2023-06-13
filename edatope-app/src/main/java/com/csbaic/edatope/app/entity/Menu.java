package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author bage
 * @since 2021-12-16
 */
@Getter
@Setter
@TableName("sys_acl_menu")
public class Menu extends BaseEntity {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单组件
     */
    private String component;
    /**
     * 上级菜单id
     */
    private String pid;

    /**
     * 系统权限id
     */
    private String permissionId;

    /**
     * 菜单类型：菜单项、按钮
     */
    private String type;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 菜单排序
     */
    private String sort;

    /**
     * 是否为外部链接
     */
    private Boolean external;

    /**
     * 菜单状态：正常、隐藏、禁用
     */
    private String status;

    /**
     * 菜单权限
     */
    private transient Permission permission;

    /**
     * 上级菜单
     */
    private transient Menu parent;

    /**
     * 子菜单
     */
    private transient List<Menu> children;

    public static final String NAME = "name";

    public static final String PID = "pid";

    public static final String PERMISSION_ID = "permission_id";

    public static final String TYPE = "type";

    public static final String PATH = "path";

    public static final String ICON = "icon";

    public static final String SORT = "sort";

    public static final String EXTERNAL = "external";

    public static final String STATUS = "status";

    public static final String COMPONENT = "component";

    public static final String TITLE = "title";

}
