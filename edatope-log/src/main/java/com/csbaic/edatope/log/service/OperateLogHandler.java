package com.csbaic.edatope.log.service;

import com.csbaic.edatope.log.context.OperateContext;

/**
 * 自动记录日志
 */
public interface OperateLogHandler {

    /**
     * 记录日志
     *
     * @param context
     */
    void handle(OperateContext context);
}
