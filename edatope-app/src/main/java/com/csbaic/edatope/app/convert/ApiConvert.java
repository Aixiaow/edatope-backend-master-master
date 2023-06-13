package com.csbaic.edatope.app.convert;

import com.csbaic.edatope.app.model.command.CreateApiCmd;
import com.csbaic.edatope.app.model.dto.ApiDTO;
import com.csbaic.edatope.app.entity.Api;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ApiConvert {

    @Autowired
    private PermissionConvert permissionConvert;
    /**
     * 转换成RoleVo
     *
     * @param api
     * @return
     */
    public ApiDTO convertToDTO(Api api) {
        ApiDTO apiDTO = new ApiDTO();
        BeanCopyUtils.copyProperties(api, apiDTO);
        if (CollectionUtils.isNotEmpty(api.getChildren())) {
            apiDTO.setChildren(
                    api.getChildren()
                            .stream()
                            .map(this::convertToDTO)
                            .collect(Collectors.toList())
            );
        }
        if (api.getParent() != null) {
            apiDTO.setParent(convertToDTO(api.getParent()));
        }
        if(api.getPermission() != null){
            apiDTO.setPermission(permissionConvert.convertToViewObject(api.getPermission()));
        }
        return apiDTO;
    }

    /**
     * 转换成实体
     * @param cmd
     * @return
     */
    public Api convertToEntity(CreateApiCmd cmd){
        Api api = new Api();
        BeanCopyUtils.copyNonNullProperties(cmd, api);
        return api;
    }
}
