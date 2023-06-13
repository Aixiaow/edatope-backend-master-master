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
public class UpdateOrganizationCmd {


    private String id;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String name;

    /**
     * 单位编码（统一社会信用编码）
     */
    @ApiModelProperty("单位编码（统一社会信用编码）")
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
    private String category;

    /**
     * 单位类型
     */
    @ApiModelProperty("业务类型（字典类型：organization_type）")
    private List<String> bizType;

    /**
     * 单位所在省
     */
    @ApiModelProperty("单位所在省")
    private String provinceCode;

    /**
     * 单位所在市
     */
    @ApiModelProperty("单位所在市")
    private String cityCode;

    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县")
    private String districtCode;

    /**
     * 单位地址详情
     */
    @ApiModelProperty("单位地址详情")
    private String address;

    /**
     * 单位法人名称
     */
    @ApiModelProperty("单位法人名称")
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


    @ApiModelProperty("服务级别")
    private String serviceLevel;

    /**
     * 单位状态
     */
    @ApiModelProperty("单位状态")
    private String status;


}
