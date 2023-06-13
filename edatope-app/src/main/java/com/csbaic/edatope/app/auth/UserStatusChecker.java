package com.csbaic.edatope.app.auth;

import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.PrincipalStatusChecker;
import com.csbaic.edatope.app.enums.UserStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserStatusChecker implements PrincipalStatusChecker {
    @Override
    public void check(PrincipalDetails principal) throws AuthenticationException {
        if (Objects.equals(principal.getStatus(), UserStatus.DISABLE.name())) {
            throw new DisabledAccountException("用户已被禁用");
        }
    }
}
