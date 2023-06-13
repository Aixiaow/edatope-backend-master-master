package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 获取授权单位
 * @Date 2022/5/3 14:04
 */
@Data
public class TechOrgAuthDTO {
    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private String id;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String name;
}
