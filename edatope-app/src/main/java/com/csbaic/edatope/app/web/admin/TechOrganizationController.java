package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.TechOrganizationDTO;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.TechOrganizationQuery;
import com.csbaic.edatope.app.service.impl.TechOrganizationService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 系统组织机构表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@RestController
@Api(tags = "技术单位管理")
@RequestMapping("/api/v1/tech-organization")
public class TechOrganizationController {

    @Autowired
    private TechOrganizationService organizationService;

    @ApiPermission("sys:tech-organization:create")
    @ApiOperation("创建技术单位")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateTechOrganizationCmd cmd) {
        organizationService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("技术单位列表（分页）")
    @PostMapping("/list")
    public Result<IPage<TechOrganizationDTO>> create(@Validated @RequestBody TechOrganizationQuery cmd) {
        return Result.ok(
                organizationService.listPage(cmd)
        );
    }

    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("技术单位列表")
    @PostMapping("/listAll")
    public Result listAll() {
        return Result.ok(
                organizationService.listAll()
        );
    }

    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("单位用户列表（花名册）")
    @GetMapping("/listOrganizationUser")
    public Result listOrganizationUser(@RequestParam("organizationId") String organizationId) {
        return Result.ok(
                organizationService.listOrganizationUser(organizationId)
        );
    }

    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("角色列表")
    @PostMapping("/listRole")
    public Result listRole() {
        return Result.ok(
                organizationService.listOrganizationRole()
        );
    }

    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("根据业务类型获取当前用户单位的管理员")
    @PostMapping("/listOrganizationAdminRoleWithBizType")
    public Result listOrganizationAdminRoleWithBizType(@RequestBody @Validated @NotEmpty(message = "业务类型不能为空") List<String> bizType) {
        return Result.ok(
                organizationService.listOrganizationAdminRoleWithBizType(bizType)
        );
    }


    @ApiPermission("sys:tech-organization:view")
    @ApiOperation("技术单位详情")
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.ok(
                organizationService.getTechOrganizationDetail(id)
        );
    }


    @ApiPermission("sys:tech-organization:update")
    @ApiOperation("更新技术单位")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody   UpdateTechOrganizationCmd cmd) {
        organizationService.update(cmd);
        return Result.ok(

        );
    }


    @ApiPermission("sys:tech-organization:delete")
    @ApiOperation("删除单位")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteOrganizationCmd cmd) {
        organizationService.delete(cmd);
        return Result.ok(

        );
    }


    @ApiPermission("sys:tech-organization:reset-pwd")
    @ApiOperation("重置密码")
    @PostMapping("/reset-pwd")
    public Result delete(@RequestBody @Validated ResetPasswordCmd cmd) {
        organizationService.resetPwd(cmd);
        return Result.ok(

        );
    }


}

