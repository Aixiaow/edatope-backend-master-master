package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.app.model.dto.EnterpriseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 地块
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Data
public class UpdateBlockCmd {

    @ApiModelProperty("地区id")
    @NotEmpty(message = "地块id不能为空")
    private String id;
    /**
     * 地区名称
     */
    @ApiModelProperty("地区名称")
    private String name;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private String projectId;

    /**
     * 地区所属省份编码
     */
    @ApiModelProperty("地区所属省份编码")
    private String provinceCode;

    /**
     * 地区所属城市编码
     */
    @ApiModelProperty("地区所属城市编码")
    private String cityCode;

    /**
     * 地区所属区县编码
     */
    @ApiModelProperty("地区所属区县编码")
    private String districtCode;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 纬度符号
     */
    @ApiModelProperty("纬度符号")
    @NotEmpty(message = "纬度符号不能为空")
    private String latitudeFlag;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 经度符号
     */
    @ApiModelProperty("经度符号")
    private String longitudeFlag;

    /**
     * 地块地址
     */
    @ApiModelProperty("地块地址")
    private String address;

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contact;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String contactPhone;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业信息(新建企业不传id)")
    private EnterpriseDTO enterprise;


}
