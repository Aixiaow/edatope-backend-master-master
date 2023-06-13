package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 被调查的企业
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Data
public class EnterpriseBlockListVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 企业统一信用编码
     */
    @ApiModelProperty("企业编码")
    private String code;

    /**
     * 小分类
     */
    @ApiModelProperty("企业分类")
    private List<String> category;


    @ApiModelProperty("企业分类")
    private String categoryDesc;

    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    private String type;


    /**
     * 企业类型
     */
    @ApiModelProperty("企业类型")
    @DictProperty(value = "type", type = "EnterpriseType")
    private String typeDesc;


    @ApiModelProperty("企业地块")
    private List<BlockVO> blockList;


}
