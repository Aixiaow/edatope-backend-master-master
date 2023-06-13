package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.Api;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 系统接口表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface ApiMapper extends BaseMapper<Api> {


    /**
     * 删除所有接口
     */
    void deleteAll();
}


