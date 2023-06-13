package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateDetectionTargetCmd;
import com.csbaic.edatope.app.model.command.CreateStageCmd;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
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
 * 检测指标表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@RestController
@Api(tags = "检测指标维护")
@RequestMapping("/api/v1/detectionTarget")
public class DetectionTargetController {

    @Autowired
    private IDetectionTargetService detectionTargetService;

    @ApiPermission("sys:detection-target:create")
    @ApiOperation("创建检测指标")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateDetectionTargetCmd cmd) {
        detectionTargetService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:work-stage:view")
    @ApiOperation("检测指标列表（分页）")
    @PostMapping("/list")
    public Result<IPage<DetectionTargetDTO>> create(@Validated @RequestBody DetectionTargetQuery query) {
        return Result.ok(
                detectionTargetService.listPage(query)
        );
    }
}

