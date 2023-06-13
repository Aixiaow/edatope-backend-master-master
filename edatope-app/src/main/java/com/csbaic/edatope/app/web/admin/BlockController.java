package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.BlockImportCheckResult;
import com.csbaic.edatope.app.model.dto.BlockVO;
import com.csbaic.edatope.app.model.dto.EnterpriseVO;
import com.csbaic.edatope.app.model.dto.ProjectDTO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.EnterpriseQuery;
import com.csbaic.edatope.app.model.query.ProjectQuery;
import com.csbaic.edatope.app.service.IBlockService;
import com.csbaic.edatope.app.service.IProjectService;
import com.csbaic.edatope.app.service.impl.BlockImportService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "地块管理")
@RequestMapping("/api/v1/block")
@RestController()
public class BlockController {

    @Autowired
    private IBlockService blockService;
    @Autowired
    private BlockImportService blockImportService;

    @ApiPermission("sys:block:create")
    @ApiOperation("创建地块")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateBlockCmd cmd) {
        blockService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:block:create")
    @ApiOperation("导入地块")
    @PostMapping("/import")
    public Result<List<BlockImportCheckResult>> importBlock(@RequestParam("file") MultipartFile file) {
        return blockImportService.importBlock(file);
    }

    @ApiPermission("sys:block:view")
    @ApiOperation("获取地块列表")
    @PostMapping("/list")
    public Result<IPage<BlockVO>> list(@RequestBody @Validated BlockQuery query) {
        return Result.ok(blockService.listPage(query));
    }


    @ApiPermission("sys:block:view")
    @ApiOperation("地块详情")
    @GetMapping("/getDetailById")
    public Result<BlockVO> getDetailById(@RequestParam("id") String id) {
        return Result.ok(blockService.getDetailById(id));
    }


    @ApiPermission("sys:block:update")
    @ApiOperation("更新地块")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateBlockCmd cmd) {
        blockService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:block:delete")
    @ApiOperation("删除地块")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteBlockCmd cmd) {
        blockService.delete(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:block:view")
    @ApiOperation("企业搜索")
    @PostMapping("/ListEnterpriseByName")
    public Result<List<EnterpriseVO>> ListEnterpriseByName(@RequestBody @Validated EnterpriseQuery cmd) {
        return Result.ok(blockService.listEnterpriseByName(cmd));
    }



}
