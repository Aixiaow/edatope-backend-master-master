package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.ApiDTO;
import com.csbaic.edatope.app.service.IApiService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "接口管理")
@RequestMapping("/api/v1/apis")
@RestController()
public class ApiController {

    @Autowired
    private IApiService apiService;

    @ApiPermission("sys:api:create")
    @ApiOperation("创建接口")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateApiCmd cmd) {
        apiService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:api:view")
    @ApiOperation("获取接口列表")
    @GetMapping("/list")
    public Result<List<ApiDTO>> get() {
        return Result.ok(apiService.listApi());
    }

    @ApiPermission("sys:api:view")
    @ApiOperation("获取接口详情")
    @GetMapping("/get")
    public Result<ApiDTO> get(@NotBlank(message = "id不能为空") @RequestParam("id") String id) {
        return Result.ok(apiService.getApiDetail(id));
    }

    @ApiPermission("sys:api:update")
    @ApiOperation("更新接口")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateApiCmd cmd) {
        apiService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:api:delete")
    @ApiOperation("删除接口")
    @PostMapping("/delete")
    public Result deleteBatch(@Validated @RequestBody DeleteApiCmd cmd){
        apiService.delete(cmd);
        return Result.ok();
    }
}
