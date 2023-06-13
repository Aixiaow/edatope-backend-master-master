package com.csbaic.edatope.app.model.command;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.app.model.dto.EnterpriseDTO;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
public class CreateBlockCmd {

    /**
     * 地区名称
     */
    @ApiModelProperty("地块名称")
    @NotEmpty(message = "地块名称不能为空")
    private String name;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    @NotEmpty(message = "项目id不能为空")
    private String projectId;


    /**
     * 地区所属省份编码
     */
    @ApiModelProperty("地区所属省份编码")
    @NotEmpty(message = "省份编码不能为空")
    private String provinceCode;

    /**
     * 地区所属城市编码
     */
    @ApiModelProperty("地区所属城市编码")
    @NotEmpty(message = "城市编码不能为空")
    private String cityCode;

    /**
     * 地区所属区县编码
     */
    @ApiModelProperty("地区所属区县编码")
    @NotEmpty(message = "区县编码不能为空")
    private String districtCode;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    @NotNull(message = "地块纬度不能为空")
    private BigDecimal latitude;

    /**
     * 纬度符号
     */
    @ApiModelProperty("纬度符号")
    @NotEmpty(message = "纬度符号不能为空")
    @Length(min = 1, max = 1, message = "纬度符号太长")
    private String latitudeFlag;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    @NotNull(message = "经度符号不能为空")
    private BigDecimal longitude;

    /**
     * 经度符号
     */
    @ApiModelProperty("经度符号")
    @NotEmpty(message = "经度符号不能为空")
    @Length(min = 1, max = 1, message = "经度符号太长")
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
    @ApiModelProperty("企业信息（新建企业不用id)")
    @NotNull
    private EnterpriseDTO enterprise;

}
