package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.ProjectDTO;
import com.csbaic.edatope.app.model.dto.ProjectInfoDTO;
import com.csbaic.edatope.app.model.query.ProjectQuery;
import com.csbaic.edatope.app.service.IProjectService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "项目管理")
@RequestMapping("/api/v1/project")
@RestController()
public class ProjectController {

    @Autowired
    private IProjectService projectService;

    @ApiPermission("sys:project:create")
    @ApiOperation("创建项目")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated CreateProjectCmd cmd) {
        projectService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:project:view")
    @ApiOperation("获取接口列表")
    @PostMapping("/list")
    public Result<IPage<ProjectDTO>> list(@RequestBody @Validated ProjectQuery query) {
        return Result.ok(projectService.page(query));
    }


    @ApiPermission("sys:project:view")
    @ApiOperation("项目信息查询")
    @PostMapping("/listProjectInfo")
    public Result<IPage<ProjectInfoDTO>> listProjectInfo(@RequestBody @Validated ProjectQuery query) {
        return Result.ok(projectService.listProjectInfo(query));
    }



    @ApiPermission("sys:project:view")
    @ApiOperation("按名称查询项目")
    @GetMapping("/listByName")
    public Result<List<ProjectDTO>> listByName(@RequestParam(required = false) String name) {
        return Result.ok(projectService.listByName(name));
    }

    @ApiPermission("sys:project:view")
    @ApiOperation("获取接口详情")
    @GetMapping("/get")
    public Result<ProjectDTO> get(@NotBlank(message = "id不能为空") @RequestParam("id") String id) {
        return Result.ok(projectService.getProjectById(id));
    }

    @ApiPermission("sys:project:update")
    @ApiOperation("更新接口")
    @PostMapping("/update")
    public Result update(@Validated @RequestBody UpdateProjectCmd cmd) {
        projectService.update(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:project:delete")
    @ApiOperation("删除接口")
    @PostMapping("/delete")
    public Result deleteBatch(@Validated @RequestBody DeleteProjectCmd cmd) {
        projectService.delete(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:project:view")
    @ApiOperation("获取项目区域")
    @GetMapping("/area")
    public Result<List<DictDTO>> create(@RequestParam(value = "areaCode", required = false) String areaCode) {
        return Result.ok(
                projectService.getProjectCity(areaCode)
        );
    }

}
