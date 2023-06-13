package com.csbaic.edatope.app.model.command;

import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author bage
 * @since 2022-03-19
 */
@Data
public class DeleteProjectCmd {

    @ApiModelProperty("项目id")
    @NotEmpty(message = "项目id不能为空")
    private String id;


}
