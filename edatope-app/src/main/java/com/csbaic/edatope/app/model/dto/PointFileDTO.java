package com.csbaic.edatope.app.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bnt
 * @Description 文件
 * @Date 2022/4/28 8:34
 */
@Data
public class PointFileDTO {

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("类型;planText：方案文本；planAttach：方案附件；selfOpinion：自审意见及整改说明；plan：点位结构化数据")
    private String fileType;
}
