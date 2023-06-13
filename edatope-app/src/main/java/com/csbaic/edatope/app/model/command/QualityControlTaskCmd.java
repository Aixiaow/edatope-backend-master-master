package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * @Author bnt
 * @Description 分配单位任务
 * @Date 2022/5/2 23:10
 */
@Data
public class QualityControlTaskCmd {

    @NotEmpty
    @ApiModelProperty("工作任务阶段id集合")
    private List<QualityControlTaskDeadLineCmd> blockWorkStageIds;

    @NotBlank
    @ApiModelProperty("布点质控单位id")
    private String orgId;

    @NotBlank
    @ApiModelProperty("负责人id")
    private String principal;

    @NotBlank
    @ApiModelProperty("负责人手机")
    private String principalPhone;

    @ApiModelProperty("归属单位id")
    private String ownerId;

    @Data
    public static class QualityControlTaskDeadLineCmd {

        @ApiModelProperty("地块工作阶段")
        private String blockWorkStageId;

        @ApiModelProperty("任务期限")
        @JsonFormat(pattern = DateTimeUtils.DATE_FORMAT)
        private LocalDate deadline;

//        @ApiModelProperty("单位id")
//        private String orgId;
    }
}
