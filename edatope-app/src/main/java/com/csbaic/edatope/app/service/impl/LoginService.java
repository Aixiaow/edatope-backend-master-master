package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.auth.accesstoken.AccessToken;
import com.csbaic.edatope.auth.accesstoken.AccessTokenReposition;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.accesstoken.TokenRequest;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.context.OperateLogHelper;
import com.csbaic.edatope.log.enums.LogType;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.enums.UserStatus;
import com.csbaic.edatope.app.model.command.LoginCmd;
import com.csbaic.edatope.app.model.dto.LoginDTO;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户登陆
 */
@Slf4j
@Service
public class LoginService {

    @Autowired
    private IUserService userService;

    @Autowired
    private AccessTokenService tokenService;

    @Autowired
    private AccessTokenReposition tokenReposition;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IOrganizationService organizationService;

    /**
     * 用户名登陆
     *
     * @param cmd
     * @return
     */
    @OperateLog(logType = LogType.LOGIN, remark = "#{target?.username} 登录了系统")
    public LoginDTO login(LoginCmd cmd) {
        User user = userService.getOne(Wrappers.<User>query().eq(User.USERNAME, cmd.getUsername()));
        if (user == null) {
            user = userService.getOne(Wrappers.<User>query().eq(User.MOBILE, cmd.getUsername()));
        }

        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到用户，请联系管理员");
        }

        if (!passwordEncoder.match(cmd.getPassword() + user.getPasswordSalt(), user.getPassword())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "密码不正确");
        }

        if (Objects.equals(user.getStatus(), UserStatus.DISABLE.name())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该用户已被禁用");
        }

        TokenRequest request = new TokenRequest();
        request.setPrincipal(user.getId());
        request.setPrincipalType("");
        request.setOrgId(user.getOrgId());
        String token = tokenService.create(request);

        AccessToken accessToken = new AccessToken();
        accessToken.setPrincipalType(request.getPrincipalType());
        accessToken.setPrincipal(request.getPrincipal());
        accessToken.setOrgId(request.getOrgId());
        accessToken.setToken(token);
        tokenReposition.save(accessToken);

        LoginDTO loginDTO = new LoginDTO();
        BeanCopyUtils.copyNonNullProperties(user, loginDTO);
        Organization org = StringUtils.isNotEmpty(user.getOrgId()) ? organizationService.getById(user.getOrgId()) : null;
        loginDTO.setOrgName(org != null ? org.getName() : null);
        loginDTO.setToken(token);
        loginDTO.setPermissions(userService.getStringPermissionForUser(user.getId()));
        OrganizationDTO detail = organizationService.getDetail(user.getOrgId());
        if (detail != null){
            loginDTO.setBizType(detail.getBizType());
            loginDTO.setBizTypeDesc(detail.getBizTypeDesc());
        }
        User finalUser = user;
        //记录日志
        OperateLogHelper.get().ifPresent(context -> {
            context.setTarget(finalUser);
            context.setOperatorId(finalUser.getId());
            context.setOperatorName(finalUser.getUsername());
        });
        return loginDTO;
    }
}
