package com.csbaic.edatope.file.model.dto;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 系统文件表
 * </p>
 *
 * @author bage
 * @since 2022-01-08
 */
@Getter
@Setter
public class FileDTO   {

    @ApiModelProperty("文件id")
    private String id;
    /**
     * 文件归属人
     */
    @ApiModelProperty("归属id")
    private Long ownerId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String name;

    /**
     * 文件块大小
     */
    @ApiModelProperty("上传文件块大小")
    private Long chunkSize;

    /**
     * 文件块数量
     */
    @ApiModelProperty("上传文件块数量")
    private Long chunkCount;

    /**
     * 已接收字节数量
     */
    @ApiModelProperty("已接收大小")
    private Long receivedSize;

    /**
     * 接收进度
     */
    @ApiModelProperty("接收进度")
    private Integer progress;

    /**
     * 文件大小（字节）
     */
    @ApiModelProperty("文件长度")
    private Long length;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件存放路径")
    private String path;

    /**
     * 文件状态（创建，上传中，已完成）
     */
    @ApiModelProperty("文件状态")
    private String status;

    /**
     * 文件备注
     */
    @ApiModelProperty("文件备注")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = DateTimeUtils.DATE_TIME_FORMAT)
    private LocalDateTime createTime;
}
