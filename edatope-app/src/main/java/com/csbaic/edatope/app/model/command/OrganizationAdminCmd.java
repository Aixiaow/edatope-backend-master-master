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
 * 创建单位
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class OrganizationAdminCmd {

    /**
     * 单位id
     */
    @ApiModelProperty("单位id（可选，修改时传）")
    private String id;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    @NotEmpty(message = "请填写单位名称，限100个字以内")
    @Length(max = 100, message = "请填写单位名称，限100个字以内")
    private String name;

    /**
     * 单位编码（统一社会信用编码）
     */
    @ApiModelProperty("单位编码（统一社会信用编码）")
//    @NotEmpty(message = "请填写统一社会信用代码，18位数字或大写英文字母")
    @Length(max = 18, message = "请填写统一社会信用代码，18位数字或大写英文字母")
    private String code;

    /**
     * 上级单位id
     */
//    @ApiModelProperty("上级单位id")
//    private String pid;

    /**
     * 单位性质
     */
    @ApiModelProperty("单位性质")
    @NotEmpty(message = "请选择单位性质")
    private String category;

    /**
     * 单位类型
     */
    @ApiModelProperty("单位类型")
//    @NotEmpty(message = "请选择单位类型")
    private String type;

    /**
     * 业务类型
     */
    @ApiModelProperty("业务类型（字典类型：organization_type）")
    @NotEmpty(message = "请选择单位业务类型")
    private List<String> bizType;

    /**
     * 单位所在省
     */
    @ApiModelProperty("单位所在省")
    @NotEmpty(message = "请选择单位所属区域")
    private String provinceCode;

    /**
     * 单位所在市
     */
    @ApiModelProperty("单位所在市")
    @NotEmpty(message = "请选择单位所属区域")
    private String cityCode;

    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县")
    @NotEmpty(message = "请选择单位所属区域")
    private String districtCode;

    /**
     * 单位地址详情
     */
    @ApiModelProperty("单位地址详情")
//    @NotEmpty(message = "请填写单位地址，限200个字以内")
    @Length(max = 200, message = "请填写单位地址，限200个字以内")
    private String address;

    /**
     * 单位法人名称
     */
    @ApiModelProperty("单位法人名称")
//    @NotEmpty(message = "请填写单位法人，限20个字以内")
    @Length(max = 20, message = "请填写单位法人，限20个字以内")
    private String legalPerson;

    /**
     * 单位联系电话
     */
    @ApiModelProperty("单位联系电话")
    private String phone;

    /**
     * 单位成立日期
     */
    @ApiModelProperty("单位成立日期，格式 yyyy年MM月dd")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDate establishmentDate;

    /**
     * 服务级别
     */
    @ApiModelProperty("服务级别")
    private String serviceLevel;

    /**
     * 管理员用户信息
     */
    @ApiModelProperty("管理员用户信息")
    private OrgUserAdminCmd userAdminCmd;

//    /**
//     * 单位状态
//     */
    @ApiModelProperty("单位状态：已保存（TEMPORARY），已提交（SUBMITTED）")
    private String status;

    @Data
    public class OrgUserAdminCmd {

        /**
         * 用户id
         */
        @ApiModelProperty("用户id（可选，修改时传）")
        private String id;

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

        @ApiModelProperty("是否被锁定")
        private Boolean locked;

        /**
         * 用户状态
         */
        @ApiModelProperty("用户状态")
        private String status;

        /**
         * 用户角色（传角色id）
         */
        @NotEmpty(message = "请选择用户的角色")
        private List<String> roles;

    }

}
