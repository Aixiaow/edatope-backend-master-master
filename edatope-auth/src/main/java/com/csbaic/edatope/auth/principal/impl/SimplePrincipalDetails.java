package com.csbaic.edatope.auth.principal.impl;

import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Set;

@Data
public class SimplePrincipalDetails implements PrincipalDetails {

    private String id;
    private Set<String> stringPermissions;
    private Set<String> roles;
    private String status;
    private Boolean admin;



}
