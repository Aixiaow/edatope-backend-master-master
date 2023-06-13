package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.CreateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.UpdateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.dto.BlockWorkStageDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.service.IBlockWorkStageService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "地块工作阶段管理")
@RequestMapping("/api/v1/block/workStage")
@RestController()
public class BlockWorkStateController {

    @Autowired
    private IBlockWorkStageService blockWorkStageService;


    @ApiPermission("sys:block-work-stage:create")
    @ApiOperation("添加阶段")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateBlockWorkStageCmd cmd) {
        blockWorkStageService.create(cmd);
        return Result.ok();
    }


    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("查看阶段")
    @GetMapping("/get")
    public Result<BlockWorkStageDTO> create(@RequestParam("id") String id) {

        return Result.ok(
                blockWorkStageService.getBlockWorkStage(id)
        );
    }

    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("获取地块的阶段")
    @GetMapping("/list")
    public Result<List<BlockWorkStageDTO>> listBlockWordStage(@RequestParam("blockId") String blockId) {

        return Result.ok(
                blockWorkStageService.listBlockWordStage(blockId)
        );
    }

    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("获取阶段类型（不传为当前用户的单位id）")
    @GetMapping("/listWorkState")
    public Result<List<WorkStageDTO>> listWorkState(@RequestParam(value = "orgId", required = false) String orgId) {

        return Result.ok(
                blockWorkStageService.listWorkState(orgId)
        );
    }


    @ApiPermission("sys:block-work-stage:update")
    @ApiOperation("更新阶段")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateBlockWorkStageCmd cmd) {
        blockWorkStageService.update(cmd);
        return Result.ok();
    }


    @ApiPermission("sys:block-work-stage:delete")
    @ApiOperation("删除阶段")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteBlockWorkStageCmd cmd) {
        blockWorkStageService.delete(cmd);
        return Result.ok();
    }

}
