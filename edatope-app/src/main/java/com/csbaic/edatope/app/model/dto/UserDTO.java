package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
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
public class UserDTO {

    @ApiModelProperty("用户Id")
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
     * 组织机构id
     */
    @ApiModelProperty("用户单位id")
    private String orgId;

    @ApiModelProperty("用户单位")
    private OrganizationDTO org;

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
     * 用户性别
     */
    @ApiModelProperty("用户性别描述")
    @DictProperty(type = "user_gender", value = "gender")
    private String genderDesc;

    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型")
    @NotEmpty(message = "用户类型不能为空")
    private String type;


    /**
     * 用户类型
     */
    @ApiModelProperty("用户类型描述")
    @DictProperty(type = "user_type", value = "type")
    private String typeDesc;

    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    private String education;


    /**
     * 最高学历
     */
    @ApiModelProperty("最高学历")
    @DictProperty(type = "user_education", value = "education")
    private String educationDesc;


    /**
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    @Length(max = 18, min = 18, message = "身份证号必须为18位数字或X")
    private String idCard;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    @NotEmpty(message = "用户状态不能为空")
    private String status;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态描述")
    @DictProperty(type = "user_status", value = "status")
    private String statusDesc;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDate birthday;


    @ApiModelProperty("是否被锁定")
    private Boolean locked;

    /**
     * 用户角色
     */
    @ApiModelProperty("用户的角色")
    private List<RoleDTO> roles;

}
