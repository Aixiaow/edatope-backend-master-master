package com.csbaic.edatope.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.log.entity.OperateLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.log.model.dto.OperateLogDTO;
import com.csbaic.edatope.log.model.dto.OperateLogListItemDTO;
import com.csbaic.edatope.log.model.query.OperateLogQuery;

/**
 * <p>
 * 业务操作日志 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-18
 */
public interface IOperateLogService extends IService<OperateLog> {


    /**
     * 获取日志
     *
     * @param query
     * @return
     */
    IPage<OperateLogListItemDTO> listOperateLog(OperateLogQuery query);

    /**
     * 获取日志详情
     *
     * @param id
     * @return
     */
    OperateLogDTO getOperateLog(String id);

}
