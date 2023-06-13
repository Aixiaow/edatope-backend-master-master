package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * <p>
 * 创建单位
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class SaveOrganizationCmd {

    /**
     * 单位id
     */
    @ApiModelProperty("单位id（保存的时候可选）")
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
    @NotEmpty(message = "请填写统一社会信用代码，18位数字或大写英文字母")
    @Length(max = 18, message = "请填写统一社会信用代码，18位数字或大写英文字母")
    private String code;

    /**
     * 上级单位id
     */
    @ApiModelProperty("上级单位id")
    private String pid;

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
    @NotEmpty(message = "请选择单位类型")
    private String type;

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
    @NotEmpty(message = "请填写单位地址，限200个字以内")
    @Length(max = 200, message = "请填写单位地址，限200个字以内")
    private String address;

    /**
     * 单位法人名称
     */
    @ApiModelProperty("单位法人名称")
    @NotEmpty(message = "请填写单位法人，限20个字以内")
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

//    /**
//     * 单位状态
//     */
//    @ApiModelProperty("单位状态：已保存（TEMPORARY），已提交（SUBMITTED）")
//    private String status;


}
