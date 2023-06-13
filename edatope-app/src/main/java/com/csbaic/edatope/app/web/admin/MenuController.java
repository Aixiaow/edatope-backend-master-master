package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.MenuDTO;
import com.csbaic.edatope.app.service.IMenuService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "菜单管理")
@RequestMapping("/api/v1/menu")
@RestController()
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiPermission("sys:menu:create")
    @ApiOperation("创建菜单")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateMenuCmd cmd) {
        menuService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:menu:view")
    @ApiOperation("查询菜单")
    @GetMapping("/list")
    public Result<List<MenuDTO>> list( ){
        return Result.ok(menuService.listMenu());
    }

//    @ApiPermission("sys:menu:view")
    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("查询导航菜单")
    @GetMapping("/listUserMenu")
    public Result<List<MenuDTO>> listUserMenu() {
        return Result.ok(menuService.listUserMenu());
    }

    @ApiPermission("sys:menu:view")
    @ApiOperation("获取菜单详情")
    @GetMapping("/get")
    public Result<MenuDTO> getMenuById(@NotBlank(message = "id不能为空") @RequestParam("id") String id){
        return Result.ok(menuService.getMenuById(id));
    }

    @ApiPermission("sys:menu:update")
    @ApiOperation("更新菜单")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateMenuCmd cmd){
        menuService.updateMenu(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:menu:delete")
    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    public Result deleteBatch(@Validated @RequestBody DeleteMenuCmd cmd){
        menuService.deleteMenu(cmd);
        return Result.ok();
    }
}
