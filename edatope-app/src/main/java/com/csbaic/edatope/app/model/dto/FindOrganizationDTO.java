package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class FindOrganizationDTO {

    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private String id;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String name;

    /**
     * 单位编码
     */
    @ApiModelProperty("单位编码")
    private String code;

    /**
     * 上级单位id
     */
    @ApiModelProperty("上级单位id")
    private String pid;

    /**
     * 上级单位
     */
    @ApiModelProperty("上级单位名称")
    private OrganizationDTO parent;
    /**
     * 单位性质
     */
    @ApiModelProperty("单位性质")
    private String category;

    /**
     * 单位性质描述
     */
    @ApiModelProperty("单位性质")
    @DictProperty(type = "organization_category", value = "category")
    private String categoryDesc;


    /**
     * 单位类型
     */
    @ApiModelProperty("单位类型（废弃用bizType）")
    private String type;

    /**
     * 单位类型描述
     */
    @ApiModelProperty("单位类型（废弃用bizTypeDesc）")
    @DictProperty(type = "organization_type", value = "type")
    private String typeDesc;


    /**
     * 单位类型
     */
    @ApiModelProperty("业务类型")
    private List<String> bizType;

    /**
     * 单位类型描述
     */
    @ApiModelProperty("单位类型")
    private List<String> bizTypeDesc;


    /**
     * 单位所在省
     */
    @ApiModelProperty("单位所在省")
    private String provinceCode;

    /**
     * 单位所在省
     */
    @ApiModelProperty("单位所在省名称")
    @DictProperty(type = "area", value = "provinceCode")
    private String provinceName;

    /**
     * 单位所在市
     */
    @ApiModelProperty("单位所在市")
    private String cityCode;

    /**
     * 单位所在市名称
     */
    @ApiModelProperty("单位所在市名称")
    @DictProperty(type = "area", value = "cityCode")
    private String cityName;


    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县")
    private String districtCode;


    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县名称")
    @DictProperty(type = "area", value = "districtCode")
    private String districtName;

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
    @ApiModelProperty("单位成立日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT_CN)
    private LocalDate establishmentDate;

    /**
     * 单位状态
     */
    @ApiModelProperty("单位状态")
    private String status;

    /**
     * 单位状态描述
     */
    @ApiModelProperty("单位状态描述")
    @DictProperty(type = "organization_status", value = "status")
    private String statusDesc;

    /**
     * 服务级别
     */
    @ApiModelProperty("服务级别")
    private String serviceLevel;

    /**
     * 单位状态描述
     */
    @ApiModelProperty("服务级别描述")
    @DictProperty(type = "service_level", value = "serviceLevel")
    private String serviceLevelDesc;


    @ApiModelProperty("创建日期")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT_CN)
    private LocalDateTime createTime;
}
