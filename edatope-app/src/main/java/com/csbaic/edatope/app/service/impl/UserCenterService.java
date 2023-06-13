package com.csbaic.edatope.app.service.impl;

import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.model.command.UpdatePasswordCmd;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.PasswordEncoder;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * 用户中心
 */
@Component
public class UserCenterService {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessTokenService accessTokenService;

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserDTO getMyUserInfo() {
        PrincipalDetails details = PrincipalUtils.getOrThrow();
        return userService.getUserDetail(details.getId());
    }


    /**
     * 修改用户密码
     */
    public void updateMyPassword(UpdatePasswordCmd cmd) {
        if (!Objects.equals(cmd.getPassword(), cmd.getConfirmPassword())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "您的密码不符合规范，请修改后重新提交！");
        }
        PrincipalDetails details = PrincipalUtils.getOrThrow();
        User user = userService.getById(details.getId());
        String salt = UUID.randomUUID().toString();
        String newPassword = passwordEncoder.encode(cmd.getPassword() + salt);
        user.setPasswordSalt(salt);
        user.setPassword(newPassword);
        userService.updateById(user);
    }


}
