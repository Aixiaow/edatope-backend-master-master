package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 布点人员任务分配列表返回数据封装
 * @Date 2022/4/26 20:02
 */
@Data
public class PointUserTasksResultVO {

    @ApiModelProperty("地块id")
    private String id;

    @ApiModelProperty("地块编码")
    private String code;

    @ApiModelProperty("地块名称")
    private String name;

    @ApiModelProperty("地区所属省份编码")
    private String provinceCode;

    @ApiModelProperty("地块所属城市编码")
    private String cityCode;

    @ApiModelProperty("地块所属区县编码")
    private String districtCode;

    @ApiModelProperty("地块所在省名称")
    @DictProperty(type = "area", value = "provinceCode")
    private String provinceName;

    @ApiModelProperty("地块所在市名称")
    @DictProperty(type = "area", value = "cityCode")
    private String cityName;

    @ApiModelProperty("地块所在区县名称")
    @DictProperty(type = "area", value = "districtCode")
    private String districtName;

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterprise;

    //----

    @ApiModelProperty("地块管理单位")
    private OrganizationDTO organization;

    @ApiModelProperty("技术管理单位")
    private OrganizationDTO technicalOrg;

    @ApiModelProperty("任务分配进度")
    private String taskPlan;

    @ApiModelProperty("任务分配进度颜色")
    private String taskPlanColor;

    @ApiModelProperty("项目信息")
    private ProjectDTO project;

    @ApiModelProperty("工作阶段")
    private List<PointUserWorkStageDTO> workStageList;
}
