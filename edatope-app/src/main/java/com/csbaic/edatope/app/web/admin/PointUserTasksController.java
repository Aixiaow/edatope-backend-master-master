package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.PointAuditRecord;
import com.csbaic.edatope.app.entity.PointTasksRecord;
import com.csbaic.edatope.app.entity.PointUserTasks;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.model.command.PointUserTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.PointAuditRecordQuery;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.QualityControlTasksQuery;
import com.csbaic.edatope.app.model.query.UserListQuery;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IPointAuditRecordService;
import com.csbaic.edatope.app.service.IPointTasksRecordService;
import com.csbaic.edatope.app.service.IPointUserTasksService;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.result.ResultCode;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 布点人员任务分配 前端控制器
 * </p>
 *
 * @author bnt
 * @since 2022-04-26
 */
@Api(tags = "地块布点管理")
@RestController
@RequestMapping("/api/v1/pointUserTasks")
public class PointUserTasksController {

    @Autowired
    private IPointUserTasksService pointUserTasksService;

    @Autowired
    private IPointTasksRecordService pointTasksRecordService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IPointAuditRecordService pointAuditRecordService;

    /**
     * 操作事项
     */
    private static List<String> operateItemsList = new ArrayList<String>() {{
        add(OperateItemsEnum.DISTRIBUTE_TASK.getDesc());
        add(OperateItemsEnum.RECALL_TASK.getDesc());
        add(OperateItemsEnum.DISTRIBUTE_POINT_USER_TASK.getDesc());
        add(OperateItemsEnum.RECALL_POINT_USER_TASK.getDesc());
        add(OperateItemsEnum.SUBMIT_TASK.getDesc());
        add(OperateItemsEnum.REFORM_SUBMIT_TASK.getDesc());
        add(OperateItemsEnum.DEFEND_SUBMIT_TASK.getDesc());
        add(OperateItemsEnum.RETURN_DEFEND_TASK.getDesc());
        add(OperateItemsEnum.AUDIT_TASK.getDesc());
    }};

    @ApiPermission("sys:point-user-tasks-distribution:create")
    @ApiOperation("布点人员任务分配-分配布点任务")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated PointUserTaskCmd pointUserTasCmd) {
        pointUserTasksService.create(pointUserTasCmd);
        return Result.ok();
    }

    @ApiPermission("sys:point-user-tasks-distribution:view")
    @ApiOperation(value = "布点人员任务分配-列表-分页", response = PointUserTasksResultVO.class)
    @PostMapping("/page")
    public Result page(@RequestBody PointUserBlockQuery blockQuery) {
        return Result.ok(pointUserTasksService.listPage(blockQuery));
    }

    @ApiPermission("sys:point-user-tasks-distribution:delete")
    @ApiOperation("布点人员任务分配-撤回任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blockWorkStageId", value = "地块工作阶段id", required = true, type = "String"),
    })
    @GetMapping("/delete")
    public Result delete(String blockWorkStageId) {
        Optional
                .ofNullable(blockWorkStageId)
                .orElseThrow(() -> BizRuntimeException.from(ResultCode.ERROR, "工作阶段id不能为空"));
        pointUserTasksService.update(Wrappers
                .<PointUserTasks>lambdaUpdate()
                .set(PointUserTasks::getStatus, SurveyTaskStatusEnum.RECALL.name())
                .set(PointUserTasks::getUserId, "")
                .eq(PointUserTasks::getBlockWorkStageId, blockWorkStageId)
                .eq(PointUserTasks::getStatus, SurveyTaskStatusEnum.ALLOCATED.name()));

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 记录操作记录
        Organization organization = organizationService.getById(details.getOrgId());
        pointTasksRecordService.save(new PointTasksRecord(blockWorkStageId, details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.RECALL_POINT_USER_TASK.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()), null, null, null
        ));
        return Result.ok();
    }

    @ApiPermission("sys:point-user-tasks-distribution:view")
    @ApiOperation(value = "布点人员任务分配-选择布点人员列表-分页", response = UserSelectResultVO.class)
    @GetMapping("/userList")
    public Result userList(UserListQuery userListQuery) {
        return Result.ok(pointUserTasksService.userList(userListQuery));
    }

    @ApiPermission("sys:point-user-tasks-distribution:view")
    @ApiOperation(value = "布点人员任务分配-撤回任务-布点人员信息", response = UserSelectResultVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blockWorkStageId", value = "地块工作阶段id", required = true, type = "String"),
    })
    @GetMapping("/userInfo")
    public Result userInfo(String blockWorkStageId) {
        return Result.ok(pointUserTasksService.userInfo(blockWorkStageId));
    }

    @ApiPermission("sys:point-user-tasks-distribution:view")
    @ApiOperation(value = "布点人员任务分配-待分配和已分配阶段任务列表", response = PointWorkStageDistributeVO.class)
    @PostMapping("/distributionList")
    public Result distributionList(@RequestBody List<String> blockWorkStageIdList) {
        return Result.ok(pointUserTasksService.distributionList(blockWorkStageIdList));
    }

    @ApiPermission("sys:point-block-tasks-query:view")
    @ApiOperation(value = "地块布点方案查询-分页", response = PointBlockTasksResultVo.class)
    @PostMapping("/pointBlockTaskPage")
    public Result pointBlockTaskPage(@RequestBody PointUserBlockQuery blockQuery) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        blockQuery.setOrgId(details.getOrgId());
        // 任务完成进度：分母为该布点人员被分配的对应地块下布点任务总数（即工作阶段总数），
        // 分子为对应地块下所有布点任务中状态为“已提交、直接通过、完善后复核通过、重审后复核通过”
        blockQuery.setTaskProcessStatusList(Arrays.asList(PointStatusEnum.submit.getValue(),
                PointStatusEnum.pass.getValue(),
                PointStatusEnum.perfect_review_pass.getValue(),
                PointStatusEnum.retrial_review_pass.getValue()));
        return Result.ok(pointUserTasksService.pointBlockTaskPage(blockQuery));
    }

    @ApiPermission("sys:point-block-tasks-defend:view")
    @ApiOperation(value = "布点方案数据维护-分页", response = PointBlockTasksResultVo.class)
    @PostMapping("/pointBlockTaskDefendPage")
    public Result pointBlockTaskDefendPage(@RequestBody PointUserBlockQuery blockQuery) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        blockQuery.setOrgId(details.getOrgId());
        blockQuery.setUserId(details.getId());
        // 任务完成进度：分母为该布点人员被分配的对应地块下布点任务总数（即工作阶段总数），
        // 分子为对应地块下所有布点任务中状态为“已提交”
        blockQuery.setTaskProcessStatusList(Arrays.asList(PointStatusEnum.submit.getValue()));
        return Result.ok(pointUserTasksService.pointBlockTaskPage(blockQuery));
    }

    @ApiPermission("sys:point-block-tasks-adjust:view")
    @ApiOperation(value = "采样过程点位调整-分页", response = PointBlockTasksResultVo.class)
    @PostMapping("/pointBlockTaskDAdjustPage")
    public Result pointBlockTaskDAdjustPage(@RequestBody PointUserBlockQuery blockQuery) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        blockQuery.setOrgId(details.getOrgId());
        blockQuery.setUserId(details.getId());
        // 任务完成进度：分母为该布点人员被分配的对应地块下布点任务状态为（退回维护、维护待审核）的布点任务总数，
        // 分子为对应地块下所有布点任务中状态为“维护待审核”的任务数。
        blockQuery.setTaskProcessStatusList(Arrays.asList(PointStatusEnum.maintain_audit.getValue()));
        blockQuery.setDeployPointStatusList(Arrays.asList(PointStatusEnum.back_maintain.getValue(),
                PointStatusEnum.maintain_audit.getValue()));
        return Result.ok(pointUserTasksService.pointBlockTaskPage(blockQuery));
    }

    @ApiPermission("sys:point-block-tasks-defend:submit")
    @ApiOperation(value = "布点方案数据维护-待提交布点方案列表", response = SubmitPointTaskVO.class)
    @PostMapping("/pointBlockTaskDefendSubmit")
    public Result pointBlockTaskDefendSubmit(@RequestBody List<String> blockWorkStageIdList) {
        return Result.ok(pointUserTasksService.pointBlockTaskDefendSubmit(blockWorkStageIdList));
    }

    //    @ApiPermission("sys:point-block-tasks-defend:view")
    @ApiOperation(value = "布点方案-查看结果|下载方案", response = ResultPointTaskVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blockWorkStageId", value = "地块工作阶段id", required = true, type = "String"),
    })
    @GetMapping("/pointBlockTaskDefendView")
    public Result pointBlockTaskDefendView(String blockWorkStageId) {
        return Result.ok(pointUserTasksService.pointBlockTaskDefendView(blockWorkStageId));
    }

    @ApiOperation(value = "查看操作记录", response = PointTasksRecord.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blockWorkStageId", value = "地块工作阶段id", required = true, type = "String"),
    })
    @GetMapping("/pointTasksRecord")
    public Result pointTasksRecord(String blockWorkStageId) {
        return Result.ok(pointTasksRecordService.queryRecord(blockWorkStageId, operateItemsList));
    }

    @ApiPermission("sys:point-block-tasks-reform:view")
    @ApiOperation(value = "布点方案数据整改-列表分页", response = QualityControlSpecialistTaskResultVO.class)
    @PostMapping("/pointBlockTaskReformPage")
    public Result pointBlockTaskReformPage(@RequestBody @Validated PointUserBlockQuery query) {
        return Result.ok(pointUserTasksService.pointBlockTaskReformPage(query));
    }

    @ApiPermission("sys:point-block-tasks-reform:view")
    @ApiOperation(value = "布点方案审核记录", response = PointAuditRecord.class)
    @PostMapping("/pointAuditRecord")
    public Result pointAuditRecord(@RequestBody @Validated PointAuditRecordQuery query) {
        return Result.ok(pointAuditRecordService.page(query));
    }
}

