package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 布点质控单位任务
 * @Date 2022/5/3 15:00
 */
@Data
public class QualityControlTasksQuery extends PageQuery {
    @ApiModelProperty("地块编码")
    private String code;

    @ApiModelProperty("地块名称")
    private String name;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("企业类型")
    private String enterpriseType;

    @ApiModelProperty("行业类别")
    private String category;

    @ApiModelProperty("地块所属省份编码")
    private String provinceCode;

    @ApiModelProperty("地块所属城市编码")
    private String cityCode;

    @ApiModelProperty("地块所属区县编码")
    private String districtCode;

    @ApiModelProperty("状态;WAIT_DISTRIBUTE=待分配;ALREADY_DISTRIBUTE=已分配;")
    private String status;

    @ApiModelProperty(value = "审核状态;WAIT_DEAL=待处理;ALREADY_BACK=已退回;PASS=已通过")
    private String opinionStatus;

    @ApiModelProperty("归属单位id")
    private String authOrgId;

    @ApiModelProperty(value = "行政管理单位id集合", hidden = true)
    private List<String> authOrgIdList;

    @ApiModelProperty(value = "地块所属区域编码", hidden = true)
    private List<String> provinceCodeList;

    @ApiModelProperty(value = "地块区域字段", hidden = true)
    private String areaCodeColumn;

    @ApiModelProperty(value = "布点质控单位id", hidden = true)
    private String pointQualityOrgId;

    @ApiModelProperty(value = "专家组id", hidden = true)
    private List<String> qualitySpecialistId;

    @ApiModelProperty(value = "是否查询质控退回任务", hidden = true)
    private boolean quailtyBack;
}
