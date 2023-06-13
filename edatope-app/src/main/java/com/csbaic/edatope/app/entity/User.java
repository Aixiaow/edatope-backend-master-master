package com.csbaic.edatope.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author bage
 * @since 2022-02-10
 */
@Getter
@Setter
@TableName("sys_user")
public class User extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户密码盐值
     */
    private String passwordSalt;

    /**
     * 组织机构id
     */
    private String orgId;

    /**
     * 所在部门
     */
    private String department;

    /**
     * 是否需要重新设置密码
     */
    private Boolean needSetPassword;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 最高学历
     */
    private String education;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 用户是否被锁定
     */
    private Boolean locked;
    /**
     * 用户状态
     */
    private String status;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 是否是超级管理员
     */
    private Boolean admin;

    /**
     * 用户所在单位
     */
    private transient Organization org;
    /**
     * 用户角色
     */
    private transient List<Role> roles;

    public static final String USERNAME = "username";

    public static final String NICK_NAME = "nick_name";

    public static final String AVATAR_URL = "avatar_url";

    public static final String MOBILE = "mobile";

    public static final String PASSWORD = "password";

    public static final String PASSWORD_SALT = "password_salt";

    public static final String ORG_ID = "org_id";

    public static final String DEPARTMENT = "department";

    public static final String NEED_SET_PASSWORD = "need_set_password";

    public static final String GENDER = "gender";

    public static final String TYPE = "type";

    public static final String STATUS = "status";

    public static final String BIRTHDAY = "birthday";

    public static final String ADMIN = "admin";

    public static final String LOCKED = "locked";
}
