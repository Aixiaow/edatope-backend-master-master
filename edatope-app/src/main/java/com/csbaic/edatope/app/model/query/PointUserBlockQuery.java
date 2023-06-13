package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author bnt
 * @Description 布点人员地块查询
 * @Date 2022/4/27 7:22
 */
@Data
public class PointUserBlockQuery extends PageQuery {
    @ApiModelProperty("地块编码")
    private String code;

    @ApiModelProperty("地块名称")
    private String name;

    @ApiModelProperty("地区所属省份编码")
    private String provinceCode;

    @ApiModelProperty("地区所属城市编码")
    private String cityCode;

    @ApiModelProperty("地区所属区县编码")
    private String districtCode;

    @ApiModelProperty("企业名称")
    private String enterpriseName;

    @ApiModelProperty("阶段类型")
    private String phaseType;

    @ApiModelProperty("状态;WAIT_DISTRIBUTE=待分配;ALREADY_DISTRIBUTE=已分配;" +
            "WAIT_DEAL=待处理;ALREADY_SUBMIT=已提交;" +
            "WAIT_REFORM=布点方案数据整改-待整改;WAIT_AUDIT=布点方案数据整改-待审核" +
            "POINT_WAIT_REFORM=采样过程点位调整-待整改;POINT_WAIT_AUDIT=采样过程点位调整-待审核")
    private String status;

    @ApiModelProperty("任务状态")
    private String taskStatus;

    /**
     * 布点单位id(前端无需传值)
     */
    @ApiModelProperty(hidden = true)
    private String orgId;

    /**
     * 布点方案状态集合
     */
    @ApiModelProperty(hidden = true)
    private List<String> deployPointStatusList;

    /**
     * 已完成进度统计需要的状态
     */
    @ApiModelProperty(hidden = true)
    private List<String> taskProcessStatusList;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private String userId;
}
