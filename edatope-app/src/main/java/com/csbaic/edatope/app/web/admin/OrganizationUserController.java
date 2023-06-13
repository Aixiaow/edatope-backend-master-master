package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.app.model.command.CreateUserCmd;
import com.csbaic.edatope.app.model.command.DeleteUserCmd;
import com.csbaic.edatope.app.model.command.ResetPasswordCmd;
import com.csbaic.edatope.app.model.command.UpdateUserCmd;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.impl.OrganizationUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@Api(tags = "单位人员维护")
@RequestMapping("/api/v1/organization/users")
@RestController()
public class OrganizationUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private OrganizationUserService organizationUserService;

    @ApiPermission("sys:user-organization:person:create")
    @ApiOperation("创建用户")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateUserCmd cmd) {
        organizationUserService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user-organization:person:view")
    @ApiOperation("用户详情")
    @GetMapping("/get")
    public Result get(@Validated @NotEmpty(message = "用户id不能为空") @RequestParam("id") String userId) {
        return Result.ok(
                userService.getUserDetail(userId)
        );
    }


    @ApiPermission("sys:user-organization:person:reset-pwd")
    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result resetPassword(@Validated @RequestBody ResetPasswordCmd cmd) {
        userService.resetPassword(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user-organization:person:view")
    @ApiOperation("用户列表")
    @PostMapping("/list")
    public Result<IPage<UserDTO>> list(@RequestBody @Validated UserQuery query) {
        return Result.ok(
                organizationUserService.listUserPage(query)
        );
    }

    @ApiPermission("sys:user-organization:person:update")
    @ApiOperation("更新用户")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateUserCmd cmd) {
        userService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user-organization:person:delete")
    @ApiOperation("删除用户")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteUserCmd cmd) {
        userService.delete(cmd);
        return Result.ok();
    }
}
