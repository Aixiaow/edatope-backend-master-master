package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.DetectionCapacityDTO;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.dto.TargetClassifyDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.*;
import com.csbaic.edatope.app.service.IDetectionTargetClassifyService;
import com.csbaic.edatope.app.service.IDetectionTargetGroupService;
import com.csbaic.edatope.app.service.IDetectionTargetService;
import com.csbaic.edatope.app.service.impl.TechOrganizationService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 检测指标分类表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
@RestController
@Api(tags = "指标分类维护")
@RequestMapping("/api/v1/detectionTargetClassify")
public class DetectionTargetClassifyController {

    @Autowired
    private IDetectionTargetService detectionTargetService;

    @Autowired
    private IDetectionTargetClassifyService targetClassifyService;

    @Autowired
    private TechOrganizationService organizationService;

    @Autowired
    private IDetectionTargetGroupService groupService;

    @ApiPermission("sys:target-classify:view")
    @ApiOperation("检测指标列表（弹层弹层选择查询）")
    @PostMapping("/target/list")
    public Result listAll(@Validated @RequestBody DetectionTargetAllQuery query) {
        return Result.ok(
                detectionTargetService.listAll(query)
        );
    }

    @ApiPermission("sys:target-classify:view")
    @ApiOperation("实验室单位列表（弹层选择）")
    @PostMapping("/org/listAll")
    public Result listOrgAll(@Validated @RequestBody OrgListAllQuery query) {
        return Result.ok(
                organizationService.listOrgAll(query)
        );
    }

    @ApiPermission("sys:target-classify:view")
    @ApiOperation("检测指标分组信息（弹层选择）")
    @PostMapping("/target/group")
    public Result listTargetGroup(@Validated @RequestBody DetectionTargetAllQuery query) {
        return Result.ok(
                groupService.list(query)
        );
    }

    @ApiPermission("sys:target-classify:create")
    @ApiOperation("创建指标分类")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateTargetClassifyCmd cmd) {
        targetClassifyService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:target-classify:view")
    @ApiOperation("指标分类列表（分页）")
    @PostMapping("/list")
    public Result<IPage<TargetClassifyDTO>> page(@Validated @RequestBody TagetClassifyQuery query) {
        return Result.ok(
                targetClassifyService.listPage(query)
        );
    }

    @ApiPermission("sys:target-classify:view")
    @ApiOperation("查看详情")
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.ok(
                targetClassifyService.getDetail(id)
        );
    }

    @ApiPermission("sys:target-classify:update")
    @ApiOperation("更新检测指标分类")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateTargetClassifyCmd cmd) {
        targetClassifyService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:target-classify:delete")
    @ApiOperation("删除检测指标分类")
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody DeleteTargetClassifyCmd cmd) {
        targetClassifyService.delete(cmd.getId());
        return Result.ok();
    }

    @ApiPermission("sys:target-classify:update")
    @ApiOperation("授权维护")
    @GetMapping(value = "/authorization")
    public Result authorization(@RequestParam String id) {
        targetClassifyService.authorization(id);
        return Result.ok();
    }
}

