package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.WorkStageType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工作阶段任务类型表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
public interface IWorkStageTypeService extends IService<WorkStageType> {

    /**
     * 设置任务类型
     *
     * @param stageId
     * @param bizTypes
     */
    public void setBizTypes(String stageId, List<String> bizTypes);

    public void removeBizTypes(String stageId);
}
