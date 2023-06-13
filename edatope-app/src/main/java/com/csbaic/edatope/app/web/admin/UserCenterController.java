package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.app.model.command.UpdatePasswordCmd;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.service.impl.UserCenterService;
import com.csbaic.edatope.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户中心")
@RequestMapping("/api/v1/users/my")
@RestController()
public class UserCenterController {

    @Autowired
    private UserCenterService userCenterService;

    @ApiOperation("用户信息")
    @GetMapping("/get")
    public Result<UserDTO> get() {
        return Result.ok(
                userCenterService.getMyUserInfo()
        );
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public Result<Void> resetPassword(@Validated @RequestBody UpdatePasswordCmd cmd) {
        userCenterService.updateMyPassword(cmd);
        return Result.ok();
    }


}
