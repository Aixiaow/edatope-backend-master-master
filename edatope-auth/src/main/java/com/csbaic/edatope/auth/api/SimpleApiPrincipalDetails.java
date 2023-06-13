package com.csbaic.edatope.auth.api;

import com.csbaic.edatope.auth.principal.impl.SimplePrincipalDetails;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleApiPrincipalDetails extends SimplePrincipalDetails implements ApiPrincipalDetails {

    private Boolean anonymous;



}
