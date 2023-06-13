package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 系统单位表
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Data
public class OrganizationQuery extends PageQuery {

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String name;


    /**
     * 单位性质
     */
    @ApiModelProperty("单位性质")
    private String category;


    /**
     * 单位类型
     */
    @ApiModelProperty("单位类型")
    private List<String> bizType;


    /**
     * 单位所在省
     */
    @ApiModelProperty("单位所在省编号")
    private String provinceCode;


    /**
     * 单位所在市
     */
    @ApiModelProperty("单位所在市编号")
    private String cityCode;


    /**
     * 单位所在区县
     */
    @ApiModelProperty("单位所在区县编号")
    private String districtCode;


    /**
     * 单位状态
     */
    @ApiModelProperty("单位状态")
    private String status;


    @ApiModelProperty("上级单位id")
    private String pid;

}
