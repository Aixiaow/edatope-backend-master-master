package com.csbaic.edatope.dict.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.csbaic.edatope.common.persistence.entity.BaseEntity;
import com.csbaic.edatope.dict.annotation.DictProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统接口表
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Data
public class DictItemDTO     {

    @ApiModelProperty("数据项id")
    private String id;
    /**
     * 字典数据项名称
     */
    @ApiModelProperty("数据项名称")
    private String name;

    /**
     * 字典数据值
     */
    @ApiModelProperty("数据项值")
    private String value;

    /**
     * 字典说明
     */
    @ApiModelProperty("数据项描述")
    private String description;

    /**
     * 字典id
     */
    @ApiModelProperty("字典id")
    private Long dictId;


}
