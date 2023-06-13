package com.csbaic.edatope.auth.principal.impl;

import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.PrincipalDetailsService;

public   class EmptyPrincipalDetailsService implements PrincipalDetailsService {

    @Override
    public PrincipalDetails getPrincipalDetails(Object principal) {
        return null;
    }
}
