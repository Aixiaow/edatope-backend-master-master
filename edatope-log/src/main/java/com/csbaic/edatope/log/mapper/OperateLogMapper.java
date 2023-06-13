package com.csbaic.edatope.log.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.log.entity.OperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.log.model.query.OperateLogQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 业务操作日志 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-01-18
 */
public interface OperateLogMapper extends BaseMapper<OperateLog> {

    /**
     * 查询操作日志
     *
     * @param page
     * @param query
     * @return
     */
    IPage<OperateLog> listOperateLog(IPage<OperateLog> page, @Param("query") OperateLogQuery query);

}
