package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author bnt
 * @Description 地块布点方案查询-阶段类型
 * @Date 2022/4/27 21:56
 */
@Data
public class PointBlockWorkStageDTO {
    @ApiModelProperty("地块工作阶段id")
    private String id;

    @ApiModelProperty("地块工作阶段名称")
    private String name;

    @ApiModelProperty("阶段类型")
    private String workStageId;

    @ApiModelProperty("阶段类型名称")
    private String workStageName;

    @ApiModelProperty("委托方式")
    private String entrustWay;

    @ApiModelProperty("委托方式描述")
    @DictProperty(value = "entrustWay", type = "entrust_Way")
    private String entrustWayDesc;

    @ApiModelProperty("调查任务任务期限")
    @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate taskDeadline;

    @ApiModelProperty("任务分配单位")
    private List<TasksDTO> tasksList;

    //--

    @ApiModelProperty("布点人员id")
    private String userId = "";

    @ApiModelProperty("布点人员名称")
    private String userName = "";

    @ApiModelProperty("土壤点")
    private Integer soilCount = 0;

    @ApiModelProperty("地下水")
    private Integer waterCount = 0;

    @ApiModelProperty("土水复合点")
    private Integer soleWaterCount = 0;

    @ApiModelProperty("方案文件")
    private String fileCount = "0/3";

    @ApiModelProperty("是否导入导入，0：否，1：是")
    private Integer isImport = 0;

    @ApiModelProperty("市级质控状态")
    private String cityQualityStatus = "";

    @ApiModelProperty("省级质控状态")
    private String provinceQualityStatus = "";

    @ApiModelProperty("国家级质控状态")
    private String countryQualityStatus = "";

    /**
     * 布点方案状态;字典类型 D018
     */
    @ApiModelProperty("布点方案状态")
    private String deployPointStatus;

    @ApiModelProperty("布点方案状态")
    @DictProperty(type = "deploy_point_status", value = "deployPointStatus")
    private String deployPointStatusDesc;

}
