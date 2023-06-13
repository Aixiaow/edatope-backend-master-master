package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.RoleDTO;
import com.csbaic.edatope.app.model.query.RoleQuery;
import com.csbaic.edatope.app.service.IRoleService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "角色管理")
@RequestMapping("/api/v1/roles")
@RestController()
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @ApiPermission("sys:role:create")
    @ApiOperation("创建角色")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateRoleCmd cmd){
        roleService.createRole(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:role:view")
    @ApiOperation("查询角色")
    @PostMapping("/list")
    public Result<IPage<RoleDTO>> list(@RequestBody RoleQuery query) {
        return Result.ok(roleService.listRole(query));
    }


    @ApiPermission("sys:role:view")
    @ApiOperation("查询所有的角色")
    @PostMapping("/listAll")
    public Result<List<RoleDTO>> listAll() {
        return Result.ok(roleService.listAllRole());
    }


    @ApiPermission("sys:role:view")
    @ApiOperation("获取角色详情")
    @GetMapping("/get")
    public Result<RoleDTO> get(@NotBlank(message = "id不能为空") @RequestParam("id") String id) {
        return Result.ok(roleService.getRoleDetail(id));
    }

    @ApiPermission("sys:role:update")
    @ApiOperation("更新角色")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateRoleCmd cmd) {
        roleService.updateRole(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:role:delete")
    @ApiOperation("删除角色")
    @PostMapping("/delete")
    public Result deleteRole(@Validated @RequestBody DeleteRoleCmd cmd) {
        roleService.deleteRole(cmd);
        return Result.ok();
    }

}
