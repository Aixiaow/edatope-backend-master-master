package com.csbaic.edatope.option.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.option.model.OptionDTO;
import com.csbaic.edatope.option.model.OptionQuery;
import com.csbaic.edatope.option.model.command.CreateOptionCmd;
import com.csbaic.edatope.option.model.command.DeleteOptionCmd;
import com.csbaic.edatope.option.model.command.UpdateOptionCmd;
import com.csbaic.edatope.option.service.IOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-05
 */
@Api(tags = "配置管理")
@RestController
@RequestMapping("/api/v1/option")
public class OptionController {

    @Autowired
    private IOptionService optionService;

    @ApiPermission("sys:option:create")
    @ApiOperation("创建配置")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateOptionCmd cmd){
        optionService.createOption(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:option:update")
    @ApiOperation("更新配置")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated UpdateOptionCmd cmd){
        optionService.updateOption(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:option:view")
    @ApiOperation("分页查询配置")
    @PostMapping("/list")
    public Result<IPage<OptionDTO>> list(@RequestBody @Validated OptionQuery query){
        return Result.ok(
                optionService.getOptionPage(query)
        );
    }

    @ApiPermission("sys:option:view")
    @ApiOperation("查询配置")
    @GetMapping("/getOptionById")
    public Result<OptionDTO> getOptionById(@RequestParam("id") String id){
        return Result.ok(
                optionService.getOptionById(id)
        );
    }

    @ApiPermission("sys:option:view")
    @ApiOperation("查询配置")
    @PostMapping("/getOptionByKeys")
    public Result<List<OptionDTO>> getOptionByKey(@RequestBody List<String> keys){
        return Result.ok(
                optionService.getOptionByKeys(keys)
        );
    }

    @ApiPermission("sys:option:delete")
    @ApiOperation("删除配置")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteOptionCmd cmd){
        optionService.deleteOption(cmd);
        return Result.ok();
    }
}

