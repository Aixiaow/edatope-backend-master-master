package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateDetectionMethodCmd;
import com.csbaic.edatope.app.model.command.CreateDetectionTargetCmd;
import com.csbaic.edatope.app.model.dto.DetectionMethodDTO;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.query.DetectionMethodQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import com.csbaic.edatope.app.service.IDetectionMethodService;
import com.csbaic.edatope.app.service.IDetectionTargetService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 检测方法表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-02
 */
@RestController
@Api(tags = "检测方法维护")
@RequestMapping("/api/v1/detectionMethod")
public class DetectionMethodController {

    @Autowired
    private IDetectionMethodService detectionMethodService;

    @ApiPermission("sys:detection-method:create")
    @ApiOperation("创建检测方法")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateDetectionMethodCmd cmd) {
        detectionMethodService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:detection-method:view")
    @ApiOperation("检测方法列表（分页）")
    @PostMapping("/list")
    public Result<IPage<DetectionMethodDTO>> create(@Validated @RequestBody DetectionMethodQuery query) {
        return Result.ok(
                detectionMethodService.listPage(query)
        );
    }
}

