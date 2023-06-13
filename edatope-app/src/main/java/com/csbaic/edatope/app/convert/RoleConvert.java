package com.csbaic.edatope.app.convert;

import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.app.model.command.CreateRoleCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.RoleDTO;
import com.csbaic.edatope.app.entity.Role;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RoleConvert {

    @Autowired
    private PermissionConvert permissionConvert;


    /**
     * 转换成RoleVo
     * @param role
     * @return
     */
    public RoleDTO convertToViewObject(Role role) {
        RoleDTO roleVo = new RoleDTO();
        if (!Objects.isNull(role)) {
            BeanUtils.copyProperties(role, roleVo);
            if (CollectionUtils.isNotEmpty(role.getPermissions())) {
                roleVo.setPermissions(
                        role.getPermissions()
                                .stream()
                                .map(permissionConvert::convertToViewObject)
                                .collect(Collectors.toList())
                );
            }
            if (role.getOrg() != null) {
                OrganizationDTO organizationDTO = new OrganizationDTO();
                BeanCopyUtils.copyNonNullProperties(role.getOrg(), organizationDTO);
                roleVo.setOrg(organizationDTO);
            }
        }
        return roleVo;
    }

    /**
     * 转换成实体
     * @param cmd
     * @return
     */
    public Role convertToEntity(CreateRoleCmd cmd){
        Role role = new Role();
        BeanUtils.copyProperties(cmd, role);
        return role;
    }
}
