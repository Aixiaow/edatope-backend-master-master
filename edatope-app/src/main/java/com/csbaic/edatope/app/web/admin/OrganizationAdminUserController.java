package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.OrganizationAdminUserDTO;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.OrganizationAdminUserQuery;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.impl.OrganizationUserService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Api(tags = "管理用户维护")
@RequestMapping("/api/v1/admin/user/maintain")
@RestController()
public class OrganizationAdminUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private OrganizationUserService organizationUserService;

    @ApiPermission("sys:user-organization:person:create")
    @ApiOperation("创建修改单位/管理员")
    @PostMapping("/createOrUpdate")
    public Result create(@RequestBody @Validated OrganizationAdminCmd cmd) {
        organizationUserService.createAdminOrg(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user-organization:person:view")
    @ApiOperation("查看详情")
    @GetMapping("/get")
    public Result get(@Validated @NotEmpty(message = "单位id不能为空") @RequestParam("id") String orgId) {
        OrganizationAdminUserDTO org = organizationService.getOrgDetail(orgId);
        org.setAdminUser(userService.getOrganizationAdmin(org.getId()));
        return Result.ok(org);
    }


    @ApiPermission("sys:user-organization:person:reset-pwd")
    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result resetPassword(@Validated @RequestBody ResetPasswordCmd cmd) {
        userService.resetPassword(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:user-organization:person:view")
    @ApiOperation("管理用户单位列表")
    @PostMapping("/list")
    public Result<IPage<OrganizationAdminUserDTO>> list(@RequestBody @Validated OrganizationAdminUserQuery query) {
        IPage<OrganizationAdminUserDTO> page = organizationService.listOrganPage(query);
        page.getRecords().stream().forEach(t ->{
            t.setAdminUser(userService.getOrganizationAdmin(t.getId()));
        });
        return Result.ok(page);
    }

    @ApiPermission("sys:user-organization:person:view")
    @ApiOperation("管理用户单位花名册")
    @GetMapping("/listUser")
    public Result<List<UserDTO>> listUser(@Validated @NotEmpty(message = "单位id不能为空") @RequestParam("id") String orgId) {
        return Result.ok(
                userService.listAllUserByOrgId(orgId)
        );
    }

    /*@ApiPermission("sys:user-organization:person:update")
    @ApiOperation("更新用户")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateUserCmd cmd) {
        userService.update(cmd);
        return Result.ok();
    }*/

    @ApiPermission("sys:user-organization:person:delete")
    @ApiOperation("删除单位")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteOrganizationCmd cmd) {
        organizationService.deleteOrg(cmd);
        return Result.ok();
    }
}
