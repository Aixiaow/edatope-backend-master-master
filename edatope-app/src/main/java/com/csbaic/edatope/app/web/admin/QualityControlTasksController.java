package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.FeedbackCmd;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.QualityControlBackCmd;
import com.csbaic.edatope.app.model.command.QualityControlSpecialistTaskCmd;
import com.csbaic.edatope.app.model.command.QualityControlTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.PointSpecialistPageQuery;
import com.csbaic.edatope.app.model.query.QualityControlOrgList;
import com.csbaic.edatope.app.model.query.QualityControlTasksQuery;
import com.csbaic.edatope.app.model.query.WaitQualityControlTaskQuery;
import com.csbaic.edatope.app.service.IPointUserTasksService;
import com.csbaic.edatope.app.service.IQualityControlTasksService;
import com.csbaic.edatope.app.service.ITechOrganizationAuthorizeService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 布点质控任务 前端控制器
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Api(tags = "布点质控管理")
@RestController
@RequestMapping("/api/v1/qualityControlTasks")
@Slf4j
public class QualityControlTasksController {

    @Autowired
    private IQualityControlTasksService qualityControlTasksService;
    @Autowired
    private ITechOrganizationAuthorizeService techOrganizationAuthorizeService;
    @Autowired
    private IPointUserTasksService pointUserTasksService;

    @ApiPermission("sys:point-quality-control-distribution:create")
    @ApiOperation("布点质控单位任务-分配单位任务")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated QualityControlTaskCmd qualityControlTaskCmd) {
        qualityControlTasksService.create(qualityControlTaskCmd);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-distribution:create")
    @ApiOperation(value = "布点质控单位任务-选择布点质控单位-分页", response = QualityControlOrgResultVO.class)
    @PostMapping("/orgList")
    public Result orgList(@RequestBody QualityControlOrgList qualityControlOrgList) {
        return Result.ok(qualityControlTasksService.orgList(qualityControlOrgList));
    }

    @ApiPermission("sys:point-quality-control-distribution:create")
    @ApiOperation(value = "布点质控单位任务-待分配布点质控任务和已分配布点质控任务", response = PointWorkStageDistributeVO.class)
    @PostMapping("/distributionList")
    public Result distributionList(@RequestBody List<String> blockWorkStageIdList) {
        return Result.ok(pointUserTasksService.distributionList(blockWorkStageIdList));
    }

    @ApiPermission("sys:point-quality-control-distribution:delete")
    @ApiOperation(value = "布点质控单位任务-撤回单位任务-布点质控单位信息", response = PointWorkStageDistributeVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qualityControlTasksId", value = "布点质控任务id", required = true, type = "String"),
    })
    @GetMapping("/orgInfo")
    public Result orgInfo(String qualityControlTasksId) {
        return Result.ok(qualityControlTasksService.orgInfo(qualityControlTasksId));
    }

    @ApiPermission("sys:point-quality-control-distribution:delete")
    @ApiOperation("布点质控单位任务-撤回单位任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qualityControlTasksId", value = "布点质控任务id", required = true, type = "String"),
    })
    @GetMapping("/delete")
    public Result delete(String qualityControlTasksId) {
        qualityControlTasksService.delete(qualityControlTasksId);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-distribution:view")
    @ApiOperation(value = "布点质控单位任务-归属单位下拉框", response = TechOrgAuthDTO.class)
    @GetMapping("/view")
    public Result authOrg() {
        return Result.ok(techOrganizationAuthorizeService.getTechOrgAuthList());
    }

    @ApiPermission("sys:point-quality-control-distribution:view")
    @ApiOperation(value = "布点质控单位任务-列表分页", response = QualityControlTaskResultVO.class)
    @PostMapping("/page")
    public Result page(@RequestBody @Validated QualityControlTasksQuery query) {
        return Result.ok(qualityControlTasksService.qualityControlTaskPage(query));
    }

    @ApiPermission("sys:point-quality-control-specialist:create")
    @ApiOperation("布点质控专家组任务-分配任务")
    @PostMapping("/specialistCreate")
    public Result specialistCreate(@RequestBody @Validated QualityControlSpecialistTaskCmd qualityControlSpecialistTaskCmd) {
        qualityControlTasksService.specialistCreate(qualityControlSpecialistTaskCmd);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-specialist:create")
    @ApiOperation(value = "布点质控专家组任务-布点质控专家组列表-分页", response = SpecialistResultVO.class)
    @PostMapping("/pointSpecialistTaskPage")
    public Result pointSpecialistTaskPage(@RequestBody PointSpecialistPageQuery pointSpecialistPageQuery) {
        log.info("pointSpecialistTaskPage入参:" + JSONObject.toJSONString(pointSpecialistPageQuery));
        IPage<SpecialistResultVO> iPage = qualityControlTasksService.pointSpecialistTaskPage(pointSpecialistPageQuery);
        log.info("pointSpecialistTaskPage出参:" + JSONObject.toJSONString(iPage));
        return Result.ok(iPage);
    }

    @ApiPermission("sys:point-quality-control-specialist:create")
    @ApiOperation(value = "布点质控专家组任务-待分配布点质控任务", response = QualityControlWorkStageVO.class)
    @PostMapping("/waitQualityControlTaskList")
    public Result waitQualityControlTaskList(@RequestBody WaitQualityControlTaskQuery query) {
        return Result.ok(qualityControlTasksService.waitQualityControlTaskList(query));
    }

    @ApiPermission("sys:point-quality-control-specialist:delete")
    @ApiOperation("布点质控专家组任务-撤回专家组任务")
    @GetMapping("/specialistDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qualityControlTasksId", value = "布点质控任务id", required = true, type = "String"),
    })
    public Result specialistDelete(String qualityControlTasksId) {
        qualityControlTasksService.specialistDelete(qualityControlTasksId);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-specialist:delete")
    @ApiOperation(value = "布点质控专家组任务-已选布点质控专家组信息", response = SpecialistResultVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qualityControlTasksId", value = "布点质控任务id", required = true, type = "String"),
    })
    @GetMapping("/selectSpecialist")
    public Result selectSpecialist(String qualityControlTasksId) {
        return Result.ok(qualityControlTasksService.selectSpecialist(qualityControlTasksId));
    }

    @ApiPermission("sys:point-quality-control-specialist:view")
    @ApiOperation(value = "布点质控专家组任务-列表分页", response = QualityControlSpecialistTaskResultVO.class)
    @PostMapping("/specialistPage")
    public Result specialistPage(@RequestBody @Validated QualityControlTasksQuery query) {
        return Result.ok(qualityControlTasksService.specialistPage(query));
    }

    @ApiPermission("sys:point-quality-control-specialist:view")
    @ApiOperation(value = "布点质控意见反馈-列表分页", response = QualityControlSpecialistTaskResultVO.class)
    @PostMapping("/feedbackPage")
    public Result feedbackPage(@RequestBody @Validated QualityControlTasksQuery query) {
        return Result.ok(qualityControlTasksService.feedbackPage(query));
    }

    @ApiPermission("sys:point-quality-control-back:view")
    @ApiOperation(value = "采样过程点位退回-列表分页", response = QualityControlSpecialistTaskResultVO.class)
    @PostMapping("/backPage")
    public Result backPage(@RequestBody @Validated QualityControlTasksQuery query) {
        return Result.ok(qualityControlTasksService.backPage(query));
    }

    @ApiPermission("sys:point-quality-control-back:create")
    @ApiOperation("采样过程点位退回-授权调整")
    @PostMapping("/pointBackCreate")
    public Result pointBackCreate(@RequestBody @Validated QualityControlBackCmd cmd) {
        qualityControlTasksService.pointBackCreate(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-specialist:create")
    @ApiOperation(value = "布点质控意见反馈-退回/通过")
    @PostMapping("/backOrPass")
    public Result backOrPass(@RequestBody @Validated FeedbackCmd query) {
        qualityControlTasksService.backOrPass(query);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-specialist:create")
    @ApiOperation(value = "布点质控意见反馈-汇总提交")
    @PostMapping("/collectSubmit")
    public Result collectSubmit(@RequestBody @Validated FeedbackCmd query) {
        qualityControlTasksService.collectSubmit(query);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality-control-specialist:view")
    @ApiOperation(value = "布点质控意见反馈-查看所有审核意见", response = OpinionSummaryVO.class)
    @GetMapping("/look")
    public Result look(@RequestParam(value = "qualityControlTasksId") String qualityControlTasksId) {
        return Result.ok(qualityControlTasksService.look(qualityControlTasksId));
    }

    @ApiPermission("sys:point-quality-control-specialist:view")
    @ApiOperation(value = "布点质控意见退回-列表分页", response = QualityControlSpecialistTaskResultVO.class)
    @PostMapping("/sendBackPage")
    public Result sendBackPage(@RequestBody @Validated QualityControlTasksQuery query) {
        return Result.ok(qualityControlTasksService.sendBackPage(query));
    }

    @ApiPermission("sys:point-quality-control-specialist:view")
    @ApiOperation(value = "布点质控意见退回-意见退回", response = QualityControlSpecialistTaskResultVO.class)
    @GetMapping("/sendBack")
    public Result sendBack(@RequestParam(value = "qualityControlTasks", required = true) String qualityControlTasks) {
        qualityControlTasksService.sendBack(qualityControlTasks);
        return Result.ok();
    }
}

