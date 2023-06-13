package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class BlockWorkStageQueryResultVO {

    /**
     * 地块id
     */
    @ApiModelProperty("地块id")
    private String id;

    @ApiModelProperty("地址编号")
    private String code;

    /**
     * 地区名称
     */
    @ApiModelProperty("地块名称")
    private String name;

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
     * 单位所在省
     */
    @ApiModelProperty("地块所在省名称")
    @DictProperty(type = "area", value = "provinceCode")
    private String provinceName;

    /**
     * 单位所在市名称
     */
    @ApiModelProperty("地块所在市名称")
    @DictProperty(type = "area", value = "cityCode")
    private String cityName;

    /**
     * 单位所在区县
     */
    @ApiModelProperty("地块所在区县名称")
    @DictProperty(type = "area", value = "districtCode")
    private String districtName;

    /**
     * 地块地址
     */
    @ApiModelProperty("地块地址")
    private String address;

    @ApiModelProperty("地块管理单位")
    private OrganizationDTO organization;

    @ApiModelProperty("技术管理单位")
    private OrganizationDTO technicalOrg;

    @ApiModelProperty("任务分配进度")
    private String taskPlan;

    @ApiModelProperty("阶段数量（废弃用workStageCount）")
    private Integer phaseCount = 0;

    @ApiModelProperty("阶段数量")
    private Integer workStageCount = 0;

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterprise;

    @ApiModelProperty("项目信息")
    private ProjectDTO project;

    @ApiModelProperty("工作阶段")
    List<BlockWorkStageDTO> workStageList;

}
