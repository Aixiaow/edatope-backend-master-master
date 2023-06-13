package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateOrganizationCmd;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.app.model.command.DeleteOrganizationCmd;
import com.csbaic.edatope.app.model.command.SaveOrganizationCmd;
import com.csbaic.edatope.app.model.command.SubmitOrganizationCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.service.IOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "单位管理")
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    @Autowired
    private IOrganizationService organizationService;

    @ApiPermission("sys:organization:create")
    @ApiOperation("创建单位")
    @PostMapping("/create")
    public Result submit(@Validated @RequestBody CreateOrganizationCmd cmd) {
        organizationService.create(cmd);
        return Result.ok();
    }


    @ApiPermission("sys:organization:create")
    @ApiOperation("提交单位")
    @PostMapping("/submit")
    public Result submit(@Validated @RequestBody SubmitOrganizationCmd cmd) {
        organizationService.submit(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:organization:create")
    @ApiOperation("保存单位")
    @PostMapping("/save")
    public Result submit(@Validated @RequestBody SaveOrganizationCmd cmd) {
        organizationService.save(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:organization:view")
    @ApiOperation("获取已保存的单位")
    @GetMapping("/getSavedOrganization")
    public Result getSavedOrganization() {
        return Result.ok(
                organizationService.getSavedOrganization()
        );
    }
    @ApiPermission("sys:organization:view")
    @ApiOperation("查询单位")
    @PostMapping("/list")
    public Result<IPage<OrganizationDTO>> list(@RequestBody @Validated OrganizationQuery query) {
        return Result.ok(
                organizationService.listPage(query)
        );
    }

    @ApiPermission("sys:organization:view")
    @ApiOperation("查询所有正常的单位")
    @PostMapping("/listAll")
    public Result<List<OrganizationDTO>> list() {
        return Result.ok(
                organizationService.listAll()
        );
    }

    @ApiPermission("sys:organization:delete")
    @ApiOperation("删除单位")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteOrganizationCmd cmd) {
        organizationService.delete(cmd);
        return Result.ok(

        );
    }

}

