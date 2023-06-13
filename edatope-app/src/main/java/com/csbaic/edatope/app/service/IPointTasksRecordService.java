package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.PointTasksRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 布点任务操作记录 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-04-28
 */
public interface IPointTasksRecordService extends IService<PointTasksRecord> {

    /**
     * @param blockWorkStageId 地块工作阶段id
     * @return
     */
    List<PointTasksRecord> queryRecord(String blockWorkStageId, List<String> operateItemsList);
}
