package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.entity.DeviceAuthorize;
import com.csbaic.edatope.app.model.command.BatchDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteDeviceAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.DeviceAuthorizeDTO;
import com.csbaic.edatope.app.model.query.DeviceAuthorizeQuery;

/**
 * <p>
 * 设备授权 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
public interface IDeviceAuthorizeService extends IService<DeviceAuthorize> {

    /**
     * 查询
     *
     * @param query
     * @return
     */
    IPage<DeviceAuthorizeDTO> listPage(DeviceAuthorizeQuery query);


    /**
     * 批量授权
     *
     * @param cmd
     */
    void batchAuthorize(BatchDeviceAuthorizeCmd cmd);

    /**
     * 删除记录
     *
     * @param cmd
     */
    void delete(DeleteDeviceAuthorizeCmd cmd);
}
