package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author bnt
 * @Description 布点质控单位返回结果
 * @Date 2022/5/4 17:40
 */
@Data
public class QualityControlOrgResultVO {

    @ApiModelProperty("布点质控单位id")
    private String id;

    @ApiModelProperty("布点质控单位名称")
    private String name;

    @ApiModelProperty("单位所在省")
    private String provinceCode;

    @ApiModelProperty("单位所在省名称")
    @DictProperty(type = "area", value = "provinceCode")
    private String provinceName;

    @ApiModelProperty("单位所在市")
    private String cityCode;

    @ApiModelProperty("单位所在市名称")
    @DictProperty(type = "area", value = "cityCode")
    private String cityName;

    @ApiModelProperty("单位所在区县")
    private String districtCode;

    @ApiModelProperty("单位所在区县名称")
    @DictProperty(type = "area", value = "districtCode")
    private String districtName;

    @ApiModelProperty("单位地址详情")
    private String address;

    @ApiModelProperty("服务级别")
    private String serviceLevel;

    @ApiModelProperty("服务级别名称")
    @DictProperty(type = "service_level", value = "serviceLevel")
    private String serviceLevelDesc;

    @ApiModelProperty("归属单位")
    private String ownerOrgName;

    @ApiModelProperty("负责人id")
    private String principal;

    @ApiModelProperty("负责人名称")
    private String principalName;

    @ApiModelProperty("负责人手机")
    private String principalPhone;

    @ApiModelProperty("分配任务总数")
    private Long totalCount = 0L;

    @ApiModelProperty("未完成任务数")
    private Long unCompleteCount = 0L;

    @ApiModelProperty("最晚任务期限")
    private String deadLine = "";
}
