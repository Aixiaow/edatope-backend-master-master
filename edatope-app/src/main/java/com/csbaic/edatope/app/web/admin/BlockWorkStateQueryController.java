package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.dto.BlockWorkStageQueryResultVO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.service.impl.BlockWorkStageQueryService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "地块工作阶段查询")
@RequestMapping("/api/v1/block/workStageQuery")
@RestController()
public class BlockWorkStateQueryController {

    @Autowired
    private BlockWorkStageQueryService blockWorkStageService;


    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("地块阶段查询")
    @PostMapping("/list")
    public Result<IPage<BlockWorkStageQueryResultVO>> update(@RequestBody @Validated BlockQuery query) {
        return Result.ok(
                blockWorkStageService.listPage(query)
        );
    }

    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("查看企业地块清单")
    @GetMapping("/getBlockForEnterprise")
    public Result getBlockForEnterprise(@RequestParam("id") String id) {
        return Result.ok(
                blockWorkStageService.getBlockForEnterprise(id)
        );
    }

    @ApiPermission("sys:block-work-stage:view")
    @ApiOperation("查看下一级单位地块清单")
    @PostMapping("/listNextLevelBlock")
    public Result listNextLevelBlock(@RequestBody @Validated BlockQuery query) {
        return Result.ok(
                blockWorkStageService.listNextLevelBlock(query)
        );
    }


}
