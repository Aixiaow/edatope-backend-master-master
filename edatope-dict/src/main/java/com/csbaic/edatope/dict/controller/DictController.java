package com.csbaic.edatope.dict.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.dict.model.command.*;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.model.dto.DictTypeDTO;
import com.csbaic.edatope.dict.model.dto.GroupDict;
import com.csbaic.edatope.dict.model.query.DictQuery;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.service.impl.ClassificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Api(tags = "字典管理")
@RestController
@RequestMapping("/api/v1/dict")
public class DictController {

    @Autowired
    private IDictService dictService;
    @Autowired
    private ClassificationService classificationService;


    @ApiPermission("sys:dict:create")
    @PostMapping("/create")
    @ApiOperation("创建字典")
    @Validated
    public Result create(@RequestBody CreateDictCommand cmd) {
        dictService.createDict(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:dict:view")
    @PostMapping("/list")
    @ApiOperation("分页获取列表")
    @Validated
    public Result<IPage<DictDTO>> listPageDict(@RequestBody  DictQuery query){
        return Result.ok(
                dictService.listPageDict(query)
        );
    }

    @ApiPermission("sys:dict:view")
    @GetMapping("/get")
    @ApiOperation("获取字典")
    @Validated
    public Result<DictDTO> getById(@NotEmpty(message = "字典id不能为空") @RequestParam("id") String id){
        return Result.ok(
                dictService.getDictById(id)
        );
    }

    @ApiPermission("sys:dict:view")
    @GetMapping("/all")
    @ApiOperation("获取所有字典")
    @Validated
    public Result<List<DictDTO>> findAll(){
        return Result.ok(
                dictService.listAll()
        );
    }


    @ApiPermission("sys:dict:view")
    @PostMapping("/listDictByType")
    @ApiOperation("根据类型获取字典（按层级显示）")
    @Validated
    public Result<List<DictDTO>> listDictByType(@NotEmpty(message = "字典类型不能为空") @RequestBody List<String> typeList){
        return Result.ok(
                dictService.listLevelDictByType(typeList)
        );
    }

//    @ApiPermission("sys:dict:view")
    @ApiPermission("sys:tech-organization:view")
    @PostMapping("/listGroupedDictByType")
    @ApiOperation("根据类型获取字典（打平按类型分组显示）")
    @Validated
    public Result<List<GroupDict>> listGroupedDictByType(@NotEmpty(message = "字典类型不能为空") @RequestBody List<String> typeList){
        return Result.ok(
                dictService.listGroupedDictByType(typeList)
        );
    }



    @ApiPermission("sys:dict:update")
    @PostMapping("/update")
    @ApiOperation("更新字典")
    @Validated
    public Result update(@RequestBody @Valid UpdateDictCommand cmd){
        dictService.updateDict(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:dict:delete")
    @PostMapping("/delete")
    @ApiOperation("删除字典")
    @Validated
    public Result update(@RequestBody DeleteDictCommand cmd) {
        dictService.deleteDict(cmd);
        return Result.ok();
    }


    @ApiPermission("sys:dict:view")
    @PostMapping("/listDictType")
    @ApiOperation("获取字典类型列表")
    @Validated
    public Result<List<DictTypeDTO>> listDictType() {
        return Result.ok(
                dictService.listDictType()
        );
    }


    //    @ApiPermission("sys:dict:view")
    @ApiPermission("sys:tech-organization:view")
    @GetMapping("/getAreaByCode")
    @ApiOperation("城市及下级城市查询")
    @Validated
    public Result<DictDTO> getAreaDetailByCode(@RequestParam(value = "code", required = false) String code) {
        return Result.ok(
                dictService.getAreaDetailByCode(code)
        );
    }

    @ApiPermission("sys:dict:view")
    @GetMapping("/getClassificationByCode")
    @ApiOperation("分类及下级分类查询")
    @Validated
    public Result<DictDTO> getClassificationDetail(@RequestParam(value = "code", required = false) String code) {
        return Result.ok(
                classificationService.getClassificationDetail(code)
        );
    }

}

