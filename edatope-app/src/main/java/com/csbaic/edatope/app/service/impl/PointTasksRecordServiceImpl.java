package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.PointTasksRecord;
import com.csbaic.edatope.app.mapper.PointTasksRecordMapper;
import com.csbaic.edatope.app.service.IPointTasksRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 布点任务操作记录 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
@Service
public class PointTasksRecordServiceImpl extends ServiceImpl<PointTasksRecordMapper, PointTasksRecord> implements IPointTasksRecordService {

    /**
     * @param blockWorkStageId 地块工作阶段id
     * @param operateItemsList
     * @return
     */
    @Override
    public List<PointTasksRecord> queryRecord(String blockWorkStageId, List<String> operateItemsList) {
        if (StringUtils.isEmpty(blockWorkStageId)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "地块工作阶段id不能为空");
        }
        return getBaseMapper().selectList(Wrappers.<PointTasksRecord>lambdaQuery()
                .eq(PointTasksRecord::getBlockWorkStageId, blockWorkStageId)
                .in(operateItemsList != null, PointTasksRecord::getOperateItems, operateItemsList)
                .orderByAsc(PointTasksRecord::getCreateTime));
    }
}
