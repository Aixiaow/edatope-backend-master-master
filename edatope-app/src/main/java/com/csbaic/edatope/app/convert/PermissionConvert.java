package com.csbaic.edatope.app.convert;

import com.csbaic.edatope.app.model.command.CreatePermissionCmd;
import com.csbaic.edatope.app.model.dto.PermissionDTO;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class PermissionConvert
{


    /**
     * 转换成视图对象
     * @param entity
     * @return
     */
    public PermissionDTO convertToViewObject(Permission entity){
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(entity, permissionDTO);
        permissionDTO.setChildren(new ArrayList<>());
        return permissionDTO;
    }


    /**
     * 转换成实体
     * @param cmd
     * @return
     */
    public Permission convertToEntity(CreatePermissionCmd cmd){
        Permission entity = new Permission();
        BeanCopyUtils.copyNonNullProperties(cmd, entity);
        entity.setStatus(EnableStatus.ENABLED.name());
        return entity;
    }


}
