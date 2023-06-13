package com.csbaic.edatope.app.auth;

import com.csbaic.edatope.auth.accesstoken.AccessToken;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.impl.AccessTokenPrincipalDetailsService;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.service.IUserService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPrincipalDetailsServiceImpl extends AccessTokenPrincipalDetailsService {

    @Autowired
    private IUserService userService;

    public UserPrincipalDetailsServiceImpl(AccessTokenService accessTokenService) {
        super(accessTokenService);
    }

    @Override
    protected PrincipalDetails doGetPrincipalDetails(AccessToken accessToken) {
        String userId=  accessToken.getPrincipal().toString();
        User user = userService.getById(userId);
        if(user == null){
            return null;
        }

        AppUserPrincipalDetails details = new AppUserPrincipalDetails();
        details.setOrgId(user.getOrgId());
        details.setId(user.getId());
        details.setRoles(Sets.newHashSet());
        details.setStringPermissions(userService.getStringPermissionForUser(userId));
        details.setStatus(user.getStatus());
        details.setAdmin(user.getAdmin());
        details.setMobile(user.getMobile());
        details.setUsername(user.getUsername());
        details.setNickName(user.getNickName());
        return details;
    }
}
