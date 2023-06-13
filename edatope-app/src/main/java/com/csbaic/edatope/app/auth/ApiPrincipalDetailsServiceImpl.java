package com.csbaic.edatope.app.auth;

import com.csbaic.edatope.app.entity.Api;
import com.csbaic.edatope.app.service.IApiService;
import com.csbaic.edatope.auth.api.ApiPrincipalDetails;
import com.csbaic.edatope.auth.api.ApiPrincipalDetailsService;
import com.csbaic.edatope.auth.api.SimpleApiPrincipalDetails;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiPrincipalDetailsServiceImpl implements ApiPrincipalDetailsService {

    @Autowired
    private IApiService apiService;


    @Override
    public ApiPrincipalDetails getPrincipalDetails(Object principal) {
        Api api = apiService.getMatchedApi(String.valueOf(principal));
        if(api == null){
            return null;
        }

        SimpleApiPrincipalDetails details = new SimpleApiPrincipalDetails();
        details.setId(api.getId());
        //设置权限
        if(api.getPermission() != null){
            details.setStringPermissions(Sets.newHashSet(api.getPermission().getCode()));
        }
        details.setStatus(api.getStatus());
        details.setAnonymous(api.getAnonymous());
        return details;
    }
}
