package com.csbaic.edatope.log.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.log.model.dto.OperateLogDTO;
import com.csbaic.edatope.log.model.dto.OperateLogListItemDTO;
import com.csbaic.edatope.log.model.query.OperateLogQuery;
import com.csbaic.edatope.log.service.IOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 业务操作日志 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-01-18
 */
@RestController
@Api(tags = "操作日志")
@RequestMapping("/api/v1/log")
public class OperateLogController {

    @Autowired
    private IOperateLogService operateLogService;

    @ApiPermission("sys:log:view")
    @ApiOperation("获取操作日志")
    @PostMapping("list")
    public Result<IPage<OperateLogListItemDTO>> listOperateLog(
            @RequestBody
            @Validated
                    OperateLogQuery query
    ) {
        return Result.ok(
                operateLogService.listOperateLog(query)
        );
    }

    @ApiPermission("sys:log:view")
    @ApiOperation("获取操作日志详情")
    @GetMapping("get")
    public Result<OperateLogDTO> listOperateLog(@RequestParam("id") String id) {
        return Result.ok(
                operateLogService.getOperateLog(id)
        );
    }
}

