package com.csbaic.edatope.app.model.query;

import com.csbaic.edatope.common.dto.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkStageAuthorizeQuery extends PageQuery {

    @ApiModelProperty("单位名称")
    private String name;

    @ApiModelProperty("单位性质")
    private String category;

    @ApiModelProperty("过期时间")
    private String expireTime;

    @ApiModelProperty("是否即将到期")
    private boolean expire;

    @ApiModelProperty("前端不用传")
    private Date aboutTime;

    @ApiModelProperty("单位状态")
    private String status;

}
