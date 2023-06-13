package com.csbaic.edatope.app.model.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author bnt
 * @Description 待分配布点质控任务查询
 * @Date 2022/5/5 0:28
 */
@Data
public class WaitQualityControlTaskQuery {
    @NotEmpty(message = "布点质控任务id集合不能为空")
    @ApiModelProperty("布点质控任务id集合")
    List<String> qualityControlTasksIds;
}
