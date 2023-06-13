package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateDetectionCapacityCmd;
import com.csbaic.edatope.app.model.command.CreateDetectionMethodCmd;
import com.csbaic.edatope.app.model.command.DeleteWorkStageCmd;
import com.csbaic.edatope.app.model.dto.DetectionCapacityDTO;
import com.csbaic.edatope.app.model.dto.DetectionMethodDTO;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.CapacityMethodQuery;
import com.csbaic.edatope.app.model.query.DetectionCapacityAuditBody;
import com.csbaic.edatope.app.model.query.DetectionCapacityQuery;
import com.csbaic.edatope.app.model.query.DetectionMethodQuery;
import com.csbaic.edatope.app.service.IDetectionCapacityService;
import com.csbaic.edatope.app.service.IDetectionMethodService;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 检测能力表 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@RestController
@Api(tags = "检测能力验证")
@RequestMapping("/api/v1/detectionCapacity")
public class DetectionCapacityController {

    @Autowired
    private IDetectionMethodService detectionMethodService;

    @Autowired
    private IDetectionCapacityService detectionCapacityService;

    @Autowired
    private IOrganizationService organizationService;

    @ApiPermission("sys:detection-capacity:view")
    @ApiOperation("检测方法列表（添加时弹层筛选列表）")
    @PostMapping("/method/list")
    public Result<IPage<DetectionMethodDTO>> create(@Validated @RequestBody DetectionMethodQuery query) {
        return Result.ok(
                detectionMethodService.listPage(query)
        );
    }

    @ApiPermission("sys:detection-capacity:create")
    @ApiOperation("创建检测能力")
    @PostMapping("/create")
    public Result create(@Validated @RequestBody CreateDetectionCapacityCmd cmd) {
        detectionCapacityService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:detection-capacity:view")
    @ApiOperation("检测能力验证列表（分页）")
    @PostMapping("/list")
    public Result<IPage<DetectionCapacityDTO>> findPage(@Validated @RequestBody DetectionCapacityQuery query) {
        return Result.ok(
                detectionCapacityService.listPage(query)
        );
    }

    @ApiPermission("sys:detection-capacity:view")
    @ApiOperation("行政管理单位列表")
    @GetMapping("/org/list")
    public Result<List<OrganizationDTO>> listGovernmentOrgAll() {
        return Result.ok(
                organizationService.listGovernmentOrgAll()
        );
    }

    @ApiPermission("sys:detection-capacity:view")
    @ApiOperation("查看详情")
    @GetMapping("/get")
    public Result get(@RequestParam String id) {
        return Result.ok(
                detectionCapacityService.getCapacityDetail(id)
        );
    }

    @ApiPermission("sys:detection-capacity:view")
    @ApiOperation("审核")
    @PostMapping("/pass")
    public Result pass(@Validated @RequestBody DetectionCapacityAuditBody body) {
        return detectionCapacityService.pass(body);
    }

    @ApiPermission("sys:detection-capacity:update")
    @ApiOperation("更新检测能力")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody DetectionCapacityDTO dto) {
        detectionCapacityService.update(dto);
        return Result.ok();
    }

    @ApiPermission("sys:detection-capacity:delete")
    @ApiOperation("删除检测能力")
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody DeleteWorkStageCmd cmd) {
        detectionCapacityService.delete(cmd.getId());
        return Result.ok();
    }
}

