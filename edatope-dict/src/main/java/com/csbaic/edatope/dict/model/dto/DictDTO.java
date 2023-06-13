package com.csbaic.edatope.dict.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.dict.annotation.DictProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DictDTO {

    /**
     * 字典id
     */
    @ApiModelProperty("字典id")
    private String id;
    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    private String name;


    /**
     * 字典值
     */
    @ApiModelProperty("字典值")
    private String value;

    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private String type;

    /**
     * 字典类型名称
     */
    @ApiModelProperty("字典类型名称")
    @DictProperty(type = "dict_type_mate", value = "type")
    private String typeDesc;

    /**
     * 上级字典id
     */
    @ApiModelProperty("上级字典id")
    private String pid;

    /**
     * 上级字典id
     */
    @ApiModelProperty("上级字典")
    private DictDTO parent;


    /**
     * 字典说明
     */
    @ApiModelProperty("字典说明")
    private String description;

    /**
     * 字典状态
     */
    @ApiModelProperty("字典状态")
    private String status;

    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer sort;


    /**
     * 字典状态
     */
    @DictProperty(type = "enable_status", value = "status")
    private String statusDesc;

    /**
     * 下级字典
     */
    @ApiModelProperty("子字典")
    private List<DictDTO> children;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime createTime;

}
