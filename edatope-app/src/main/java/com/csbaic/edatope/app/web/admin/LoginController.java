package com.csbaic.edatope.app.web.admin;


import com.csbaic.edatope.common.annotation.ApiPermission;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.app.model.command.LoginCmd;
import com.csbaic.edatope.app.model.dto.LoginDTO;
import com.csbaic.edatope.app.service.impl.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户登录")
@RequestMapping("/api/v1")
@RestController()
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ApiOperation("管理后台登录")
    @PostMapping("/login")
    @ApiPermission(anonymous = true)
    public Result<LoginDTO> create(@RequestBody @Validated LoginCmd cmd){
        return Result.ok(
                loginService.login(cmd)
        );
    }
}
