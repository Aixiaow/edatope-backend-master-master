package com.csbaic.edatope.app.model.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author bnt
 * @Description 创建专家组
 * @Date 2022/5/1 19:55
 */
@Data
public class CreateSpecialistCmd {

    @ApiModelProperty("专家组主键，修改需要传")
    private String id;

    @NotBlank(message = "专家组名称不能为空")
    @ApiModelProperty("专家组名称")
    private String groupName;

    @NotEmpty(message = "专家组人员集合不能为空")
    @ApiModelProperty("专家组人员集合")
    List<CreateSpecialistPerson> specialist;

    @Data
    public static class CreateSpecialistPerson {

        @NotBlank(message = "专家身份不能为空")
        @ApiModelProperty("专家身份")
        private String nature;

        @NotEmpty(message = "工作单位id不能为空")
        @ApiModelProperty("工作单位id")
        private Long orgId;

        @NotBlank(message = "专家用户id不能为空")
        @ApiModelProperty("专家用户id")
        private String userId;

        @NotBlank(message = "专家性质不能为空")
        @ApiModelProperty("专家性质;")
        private String specialistIdentity;
    }

}
