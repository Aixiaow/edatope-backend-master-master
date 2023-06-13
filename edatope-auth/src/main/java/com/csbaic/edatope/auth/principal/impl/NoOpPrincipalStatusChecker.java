package com.csbaic.edatope.auth.principal.impl;

import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.PrincipalStatusChecker;
import org.apache.shiro.authc.AuthenticationException;

public class NoOpPrincipalStatusChecker implements PrincipalStatusChecker {
    @Override
    public void check(PrincipalDetails principal) throws AuthenticationException {

    }
}
