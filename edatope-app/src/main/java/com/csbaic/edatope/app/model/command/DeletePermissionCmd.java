package com.csbaic.edatope.app.model.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeletePermissionCmd {

    /**
     * 待删除的列表
     */
    @NotNull(message = "删除权限id列表不能为空")
    private String id;
}
