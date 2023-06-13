package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.DeletePermissionCmd;
import com.csbaic.edatope.app.model.command.CreatePermissionCmd;
import com.csbaic.edatope.app.model.command.UpdatePermissionCmd;
import com.csbaic.edatope.app.model.dto.PermissionDTO;
import com.csbaic.edatope.app.model.query.PermissionQuery;
import com.csbaic.edatope.app.service.IPermissionService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "权限管理")
@RequestMapping("/api/v1/permissions")
@RestController()
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    @ApiPermission("sys:permission:create")
    @ApiOperation("创建权限")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreatePermissionCmd cmd){
        permissionService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:permission:view")
    @ApiOperation("获取权限列表")
    @PostMapping("/list")
    public Result<List<PermissionDTO>> list(@RequestBody @Validated PermissionQuery query) {
        return Result.ok(permissionService.list(query));
    }

    @ApiPermission("sys:permission:view")
    @ApiOperation("获取权限详情")
    @GetMapping("/get")
    public Result<PermissionDTO> get(@NotBlank(message = "权限id不能为空") @RequestParam("id") String id){
        return Result.ok(permissionService.getPermissionDetail(id));
    }

    @ApiPermission("sys:permission:update")
    @ApiOperation("更新权限")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdatePermissionCmd cmd){
        permissionService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:permission:delete")
    @ApiOperation("删除权限")
    @PostMapping("/delete")
    public Result deletePermissionBatch(@Validated @RequestBody DeletePermissionCmd cmd){
        permissionService.delete(cmd);
        return Result.ok();
    }
}
