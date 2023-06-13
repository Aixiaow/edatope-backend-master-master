package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
public class CreateUserCmd {

    /**
     * 用户名
     */
    @ApiModelProperty("登陆账号")
    @NotEmpty(message = "用户登陆账号不能为空")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    @NotEmpty(message = "用户姓名不能为空")
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
    @NotEmpty(message = "手机号不能为空")
    private String mobile;


    /**
     * 组织机构id
     */
    @ApiModelProperty("用户单位id")
    @NotEmpty(message = "工作单位不能为空")
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
    @NotEmpty(message = "用户性别不能为空")
    private String gender;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    @NotEmpty(message = "用户类型不能为空")
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
    @Length(max = 18, min = 18, message = "身份证号必须为18位数字或X")
    private String idCard;


    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDate birthday;


    /**
     * 用户角色（传角色id）
     */
    @NotEmpty(message = "请选择用户的角色")
    private List<String> roles;

}
