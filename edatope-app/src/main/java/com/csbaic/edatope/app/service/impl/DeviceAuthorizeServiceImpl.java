package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.convert.OrganizationConvert;
import com.csbaic.edatope.app.entity.DeviceAuthorize;
import com.csbaic.edatope.app.enums.DeviceAuthorizeStatusEnum;
import com.csbaic.edatope.app.mapper.DeviceAuthorizeMapper;
import com.csbaic.edatope.app.model.command.BatchDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.DeviceAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.DeviceDTO;
import com.csbaic.edatope.app.model.query.DeviceAuthorizeQuery;
import com.csbaic.edatope.app.service.IDeviceAuthorizeService;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 设备授权 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Service
public class DeviceAuthorizeServiceImpl extends ServiceImpl<DeviceAuthorizeMapper, DeviceAuthorize> implements IDeviceAuthorizeService {

    @Autowired
    private IUserService userService;

    @Autowired
    private OrganizationConvert organizationConvert;

    @Override
    public IPage<DeviceAuthorizeDTO> listPage(DeviceAuthorizeQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(deviceAuthorize -> {
                    DeviceAuthorizeDTO deviceAuthorizeDTO = new DeviceAuthorizeDTO();
                    BeanCopyUtils.copyNonNullProperties(deviceAuthorize, deviceAuthorizeDTO);
                    if (deviceAuthorize.getUser() != null) {
                        deviceAuthorizeDTO.setUser(userService.getSimpleUser(deviceAuthorize.getUserId()));
                    }
                    if (deviceAuthorize.getOrganization() != null) {
                        deviceAuthorizeDTO.setOrganization(organizationConvert.convertDTO(deviceAuthorize.getOrganization()));
                    }
                    if (deviceAuthorize.getAuthorizer() != null) {
                        deviceAuthorizeDTO.setAuthorizer(userService.getSimpleUser(deviceAuthorize.getAuthorizerId()));
                    }
                    if (deviceAuthorize.getDevice() != null) {
                        DeviceDTO deviceDTO = new DeviceDTO();
                        BeanCopyUtils.copyNonNullProperties(deviceAuthorize.getDevice(), deviceDTO);
                        deviceAuthorizeDTO.setDevice(deviceDTO);
                    }
                    return deviceAuthorizeDTO;
                });
    }

    @Override
    public void batchAuthorize(BatchDeviceAuthorizeCmd cmd) {
        List<DeviceAuthorize> deviceAuthorizeList = listByIds(cmd.getIds());
        if (CollectionUtils.isEmpty(deviceAuthorizeList)) {
            return;
        }

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        deviceAuthorizeList.forEach(deviceAuthorize -> {
            deviceAuthorize.setStatus(DeviceAuthorizeStatusEnum.AUTHORIZED.toString());
            deviceAuthorize.setAuthorizerId(details.getId());
            deviceAuthorize.setAuthorizeTime(LocalDateTime.now());
        });
        updateBatchById(deviceAuthorizeList);
    }

    @Override
    public void delete(DeleteDeviceAuthorizeCmd cmd) {
        DeviceAuthorize deviceAuthorize = getById(cmd.getId());
        if (deviceAuthorize == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到授权记录");
        }
        removeById(deviceAuthorize);
    }
}
