package com.csbaic.edatope.app.web.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.model.command.BatchDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.DeviceAuthorizeDTO;
import com.csbaic.edatope.app.model.query.DeviceAuthorizeQuery;
import com.csbaic.edatope.app.service.IDeviceAuthorizeService;
import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "设备授权")
@RequestMapping("/api/v1/deviceAuthorize")
@RestController()
public class DeviceAuthorizeController {

    @Autowired
    private IDeviceAuthorizeService deviceAuthorizeService;


    @ApiPermission("sys:deviceAuthorize:view")
    @ApiOperation("查询授权")
    @PostMapping("/listPage")
    public Result<IPage<DeviceAuthorizeDTO>> list(@RequestBody @Validated DeviceAuthorizeQuery query) {
        return Result.ok(deviceAuthorizeService.listPage(query));
    }

    @ApiPermission("sys:deviceAuthorize:authorize")
    @ApiOperation("批量授权")
    @PostMapping("/batchAuthorize")
    public Result<Void> batchAuthorize(@Validated @RequestBody BatchDeviceAuthorizeCmd cmd) {
        deviceAuthorizeService.batchAuthorize(cmd);
        return Result.ok();
    }

    @ApiPermission("sys:deviceAuthorize:delete")
    @ApiOperation("删除授权")
    @PostMapping("/delete")
    public Result<Void> deleteBatch(@Validated @RequestBody DeleteDeviceAuthorizeCmd cmd) {
        deviceAuthorizeService.delete(cmd);
        return Result.ok();
    }
}
