package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.RectifySubmitPlanCmd;
import com.csbaic.edatope.app.model.command.SubmitPlanCmd;
import com.csbaic.edatope.app.model.command.SurveyTasksCmd;
import com.csbaic.edatope.app.model.dto.BlockImportCheckResult;
import com.csbaic.edatope.app.service.IPointService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 点位结构化数据详情 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/api/v1/point")
@Api(tags = "点位结构化数据管理")
public class PointController {

    @Autowired
    private IPointService pointService;

    @ApiPermission("sys:point:import")
    @ApiOperation("导入地块")
    @PostMapping("/import")
    public Result<List<BlockImportCheckResult>> importPoint(@RequestParam("file") MultipartFile file, @RequestParam("blockWorkStageId") String blockWorkStageId) {
        return pointService.importPoint(file, blockWorkStageId);
    }

    @ApiPermission("sys:point:upload")
    @ApiOperation("上传方案")
    @PostMapping("/upload/plan")
    public Result<UploadFileVO> uploadPlan(@RequestParam("file") MultipartFile file, @RequestParam("type") String type, @RequestParam("blockWorkStageId") String blockWorkStageId) {
        return pointService.uploadPlan(file, type, blockWorkStageId);
    }

    @ApiPermission("sys:point:upload")
    @ApiOperation("删除方案")
    @DeleteMapping("/delete/plan")
    public Result<UploadFileVO> deletePlan(@RequestParam("fileId")String fileId, @RequestParam("blockWorkStageId") String blockWorkStageId) {
        pointService.deletePlan(fileId, blockWorkStageId);
        return Result.ok();
    }

    @ApiPermission("sys:point:create")
    @ApiOperation("布点方案数据维护-提交方案")
    @PostMapping("/submit")
    public Result submit(@RequestBody @Validated SubmitPlanCmd cmd) {
        pointService.submit(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:point:create")
    @ApiOperation("布点方案数据整改-提交方案")
    @PostMapping("/rectify/submit")
    public Result rectifySubmit(@RequestBody @Validated RectifySubmitPlanCmd cmd) {
        pointService.rectifySubmit(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:point:create")
    @ApiOperation("采样过程点位调整-提交方案")
    @PostMapping("/adjust/submit")
    public Result adjustSubmit(@RequestBody @Validated SubmitPlanCmd cmd) {
        pointService.adjustSubmit(cmd);
        return Result.ok();
    }
}

