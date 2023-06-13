package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.CreateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteWorkStageCmd;
import com.csbaic.edatope.app.model.command.UpdateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.TechOrganizationAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.WorkStageAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.WorkStageAuthorizeQuery;
import com.csbaic.edatope.app.service.IWorkStageAuthorizeService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 工作阶段授权表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@RestController
@Api(tags = "工作阶段授权管理")
@RequestMapping("/api/v1/workStageAuthorize")
public class WorkStageAuthorizeController {

    @Autowired
    private IWorkStageAuthorizeService workStageAuthorizeService;

    @ApiPermission("sys:work-stage-authorize:create")
    @ApiOperation("创建工作阶段授权")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateWorkStageAuthorizeCmd cmd) {
        workStageAuthorizeService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:work-stage-authorize:view")
    @ApiOperation("授权工作阶段列表")
    @PostMapping("/list")
    public Result<IPage<WorkStageAuthorizeDTO>> listPage(@Validated @RequestBody WorkStageAuthorizeQuery query) {
        return Result.ok(
                workStageAuthorizeService.listPage(query)
        );
    }

    @ApiPermission("sys:work-stage-authorize:view")
    @ApiOperation("工作阶段详情")
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.ok(
                workStageAuthorizeService.getAuthorizeDetail(id)
        );
    }

    @ApiPermission("sys:work-stage-authorize:update")
    @ApiOperation("更新工作阶段授权")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateWorkStageAuthorizeCmd cmd) {
        workStageAuthorizeService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:work-stage-authorize:delete")
    @ApiOperation("删除工作阶段")
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody DeleteWorkStageCmd cmd) {
        workStageAuthorizeService.delete(cmd.getId());
        return Result.ok();
    }
}

