package com.csbaic.edatope.app.model.dto;

import com.csbaic.edatope.app.entity.Point;
import com.csbaic.edatope.app.entity.PointFile;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author bnt
 * @Description 布点方案数据维护-查看结果
 * @Date 2022/4/28 8:25
 */
@Data
public class ResultPointTaskVO extends SubmitPointTaskVO{

    @ApiModelProperty("布点单位")
    private String pointUnitName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty("方案文件")
    private List<PointFileDTO> pointFileList;

    @ApiModelProperty("点位结构化数据")
    private List<Point> pointList;

    @ApiModelProperty("用户审核资料")
    private AuditOpinionVO auditOpinionVO;
}
