package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.CreateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteSurveyTasksCmd;
import com.csbaic.edatope.app.model.command.SurveyTasksCmd;
import com.csbaic.edatope.app.model.dto.BlockWorkStageQueryResultVO;
import com.csbaic.edatope.app.model.dto.SurveyTasksAllotRecordDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationUserDTO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.OrgListAllQuery;
import com.csbaic.edatope.app.service.ISurveyTasksService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 调查任务分配 前端控制器
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
@RestController()
@RequestMapping("/api/v1/surveyTasks")
@Api(tags = "调查任务分配")
public class SurveyTasksController {

    @Autowired
    private ISurveyTasksService surveyTasksService;

    @ApiPermission("sys:survey-tasks-distribution:create")
    @ApiOperation("分配任务")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated SurveyTasksCmd cmd) {
        surveyTasksService.create(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:survey-tasks-distribution:delete")
    @ApiOperation("撤回任务")
    @PostMapping("/delete")
    public Result delete(@RequestBody @Validated DeleteSurveyTasksCmd cmd) {
        surveyTasksService.delete(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:survey-tasks-distribution:view")
    @ApiOperation("查看分配记录")
    @GetMapping("/list/record")
    public Result<SurveyTasksAllotRecordDTO> listRecord(@RequestParam("id") String id) {
        return Result.ok(
                surveyTasksService.listRecord(id)
        );
    }

    @ApiPermission("sys:survey-tasks-distribution:view")
    @ApiOperation("查看已分配信息")
    @GetMapping("/get")
    public Result<SurveyTasksAllotRecordDTO> get(@RequestParam("id") String id) {
        return Result.ok(
                surveyTasksService.get(id)
        );
    }

    @ApiPermission("sys:survey-tasks-distribution:view")
    @ApiOperation("调查任务分配列表")
    @PostMapping("/list")
    public Result<IPage<BlockWorkStageQueryResultVO>> list(@RequestBody @Validated BlockQuery query) {
        return Result.ok(
                surveyTasksService.listPage(query)
        );
    }

    @ApiPermission("sys:survey-tasks-distribution:view")
    @ApiOperation("查询相关业务单位")
    @PostMapping("/org/list")
    public Result<List<TechOrganizationUserDTO>> listOrgAll(@RequestBody @Validated OrgListAllQuery query) {
        return Result.ok(
                surveyTasksService.listOrgAll(query)
        );
    }

}

