package com.csbaic.edatope.app.web.admin;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Specialist;
import com.csbaic.edatope.app.model.command.CreateSpecialistCmd;
import com.csbaic.edatope.app.model.command.PointUserTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.*;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IQualityControlTasksService;
import com.csbaic.edatope.app.service.ISpecialistService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 专家组表 前端控制器
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Api(tags = "布点质控专家组")
@RestController
@RequestMapping("/api/v1/block/specialist")
public class SpecialistController {

    @Autowired
    private ISpecialistService specialistService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IQualityControlTasksService qualityControlTasksService;

    @ApiPermission("sys:point-quality_control-specialist:create")
    @ApiOperation("新建或维护专家组")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateSpecialistCmd specialist) {
        specialistService.create(specialist);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality_control-specialist:create")
    @ApiOperation(value = "新建或维护专家组-选择质控专家-分页", response = UserSelectResultVO.class)
    @GetMapping("/userList")
    public Result userList(UserListQuery userListQuery) {
        return Result.ok(specialistService.userList(userListQuery));
    }

    @ApiPermission("sys:point-quality_control-specialist:create")
    @ApiOperation(value = "新建或维护专家组-选择单位-列表", response = OrganizationDTO.class)
    @GetMapping("/orgList")
    public Result orgList(OrganizationQuery query) {
        return Result.ok(organizationService.list(query));
    }

    @ApiPermission("sys:point-quality_control-specialist:stop")
    @ApiOperation("启用|停用专家组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "专家组id", required = true, type = "String"),
            @ApiImplicitParam(name = "status", value = "状态（DISABLE=停用 NORMAL=正常）", required = true, type = "String"),
    })
    @GetMapping("/stop")
    public Result stop(String id,String status) {
        specialistService.stop(id,status);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality_control-specialist:delete")
    @ApiOperation("删除专家组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "专家组id", required = true, type = "String"),
    })
    @GetMapping("/delete")
    public Result delete(String id) {
        specialistService.delete(id);
        return Result.ok();
    }

    @ApiPermission("sys:point-quality_control-specialist:view")
    @ApiOperation(value = "查看专家组", response = SpecialistUserVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "专家组id", required = true, type = "String"),
    })
    @GetMapping("/view")
    public Result viewDetail(String id) {
        return Result.ok(specialistService.viewDetail(id));
    }

    @ApiPermission("sys:point-quality_control-specialist:view")
    @ApiOperation(value = "布点质控专家组-列表-分页", response = SpecialistPageResultVO.class)
    @PostMapping("/page")
    public Result page(@RequestBody SpecialistQuery blockQuery) {
        return Result.ok(specialistService.page(blockQuery));
    }

    @ApiPermission("sys:point-quality_control-specialistUser:view")
    @ApiOperation(value = "布点质控专家-列表-分页", response = SpecialistUserVO.class)
    @PostMapping("/specialistUserPage")
    public Result specialistUserPage(@RequestBody SpecialistUserQuery query) {
        return Result.ok(specialistService.specialistUserPage(query));
    }

    @ApiPermission("sys:point-quality_control-specialistUser:view")
    @ApiOperation(value = "布点质控专家列表-查看所属专家组", response = SpecialistResultVO.class)
    @PostMapping("/specialistUserDetailPage")
    public Result pointSpecialistTaskPage(@RequestBody PointSpecialistPageQuery pointSpecialistPageQuery) {
        IPage<SpecialistResultVO> iPage = qualityControlTasksService.pointSpecialistTaskPage(pointSpecialistPageQuery);
        return Result.ok(iPage);
    }
}

