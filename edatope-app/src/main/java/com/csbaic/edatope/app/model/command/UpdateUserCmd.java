package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author bage
 * @since 2022-02-10
 */
@Data
public class UpdateUserCmd {

    @ApiModelProperty("用户Id")
    @NotEmpty(message = "用户id不能为空")
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty("登陆账号")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String nickName;

    /**
     * 用户头像
     */
    @ApiModelProperty("头像地址")
    private String avatarUrl;

    /**
     * 用户手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 组织机构id
     */
    @ApiModelProperty("用户单位id")
    private String orgId;

    /**
     * 所在部门
     */
    @ApiModelProperty("用户所在部门")
    private String department;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    private String gender;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    private String type;


    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    private String education;

    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idCard;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private String status;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDate birthday;


    @ApiModelProperty("是否被锁定")
    private Boolean locked;

    /**
     * 用户角色（传角色id）
     */
    private List<String> roles;

}
