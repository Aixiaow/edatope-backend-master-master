package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Api(tags = "用户管理")
@RequestMapping("/api/v1/users")
@RestController()
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiPermission("sys:user:create")
    @ApiOperation("创建用户")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateUserCmd cmd) {
        userService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user:view")
    @ApiOperation("用户详情")
    @GetMapping("/get")
    public Result get(@Validated @NotEmpty(message = "用户id不能为空") @RequestParam("id") String userId) {
        return Result.ok(
                userService.getUserDetail(userId)
        );
    }

    @ApiPermission("sys:user:reset-pwd")
    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result resetPassword(@Validated @RequestBody ResetPasswordCmd cmd) {
        userService.resetPassword(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user:view")
    @ApiOperation("用户列表")
    @PostMapping("/list")
    public Result<IPage<UserDTO>> list(@RequestBody @Validated UserQuery query) {
        return Result.ok(
                userService.listUserPage(query)
        );
    }

    @ApiPermission("sys:user:update")
    @ApiOperation("更新用户")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateUserCmd cmd) {
        userService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user:delete")
    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteUserCmd cmd) {
        userService.delete(cmd);
        return Result.ok();
    }
}
