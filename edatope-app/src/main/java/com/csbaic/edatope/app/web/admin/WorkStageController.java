package com.csbaic.edatope.app.web.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.TechOrganizationDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.TechOrganizationQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import com.csbaic.edatope.app.service.IWorkStageService;
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
 * 工作阶段表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
@RestController
@Api(tags = "工作阶段管理")
@RequestMapping("/api/v1/workStage")
public class WorkStageController {

    @Autowired
    private IWorkStageService workStageService;

    @ApiPermission("sys:work-stage:create")
    @ApiOperation("创建工作阶段")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateStageCmd cmd) {
        workStageService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:work-stage:view")
    @ApiOperation("工作阶段列表（分页）")
    @PostMapping("/list")
    public Result<IPage<WorkStageDTO>> page(@Validated @RequestBody WorkStageQuery query) {
        return Result.ok(
                workStageService.findListPage(query)
        );
    }

    @ApiPermission("sys:work-stage:view")
    @ApiOperation("所有工作阶段")
    @PostMapping("/list/all")
    public Result<List<WorkStageDTO>> findAll() {
        return Result.ok(
                workStageService.findAll()
        );
    }

    @ApiPermission("sys:work-stage:view")
    @ApiOperation("工作阶段详情")
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.ok(
                workStageService.getWorkStageDetail(id)
        );
    }

    @ApiPermission("sys:work-stage:update")
    @ApiOperation("更新工作阶段")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody WorkStageDTO dto) {
        workStageService.update(dto);
        return Result.ok();
    }

    @ApiPermission("sys:work-stage:delete")
    @ApiOperation("删除工作阶段")
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody DeleteWorkStageCmd cmd) {
        workStageService.delete(cmd.getId());
        return Result.ok();
    }
}

