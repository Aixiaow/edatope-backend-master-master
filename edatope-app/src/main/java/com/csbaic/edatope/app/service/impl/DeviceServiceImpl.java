package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.Device;
import com.csbaic.edatope.app.mapper.DeviceMapper;
import com.csbaic.edatope.app.service.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
