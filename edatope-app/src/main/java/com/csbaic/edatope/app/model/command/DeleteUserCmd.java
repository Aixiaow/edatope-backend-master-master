package com.csbaic.edatope.app.model.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteUserCmd {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    private String id;
}
