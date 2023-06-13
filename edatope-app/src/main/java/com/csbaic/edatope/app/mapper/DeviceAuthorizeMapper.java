package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DeviceAuthorize;
import com.csbaic.edatope.app.model.query.DeviceAuthorizeQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备授权 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
public interface DeviceAuthorizeMapper extends BaseMapper<DeviceAuthorize> {

    /**
     * 查询
     *
     * @param authorize
     * @return
     */
    IPage<DeviceAuthorize> listPage(IPage<DeviceAuthorize> authorize, @Param("query") DeviceAuthorizeQuery query);
}
