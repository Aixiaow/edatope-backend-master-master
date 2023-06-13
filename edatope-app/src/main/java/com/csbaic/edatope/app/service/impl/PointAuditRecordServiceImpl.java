package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.PointAuditRecord;
import com.csbaic.edatope.app.mapper.PointAuditRecordMapper;
import com.csbaic.edatope.app.model.query.PointAuditRecordQuery;
import com.csbaic.edatope.app.service.IPointAuditRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 布点方案审核记录 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
@Service
public class PointAuditRecordServiceImpl extends ServiceImpl<PointAuditRecordMapper, PointAuditRecord> implements IPointAuditRecordService {

    @Override
    public IPage<PointAuditRecord> page(PointAuditRecordQuery query) {
        return getBaseMapper().selectPage(new Page<>(query.getPageIndex(), query.getPageSize()),
                Wrappers.<PointAuditRecord>lambdaQuery()
                        .like(StringUtils.isNotEmpty(query.getOrgName()), PointAuditRecord::getOrgName, query.getOrgName())
                        .eq(StringUtils.isNotEmpty(query.getOperateType()), PointAuditRecord::getOperateType, query.getOperateType())
                        .eq(StringUtils.isNotEmpty(query.getServiceLevel()), PointAuditRecord::getServiceLevel, query.getServiceLevel())
                        .eq(StringUtils.isNotEmpty(query.getAuditType()), PointAuditRecord::getAuditType, query.getAuditType())
        );
    }
}
