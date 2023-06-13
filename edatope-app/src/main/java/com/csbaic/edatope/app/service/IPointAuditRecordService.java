package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.PointAuditRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.query.PointAuditRecordQuery;

/**
 * <p>
 * 布点方案审核记录 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
public interface IPointAuditRecordService extends IService<PointAuditRecord> {

    IPage<PointAuditRecord> page(PointAuditRecordQuery query);
}
