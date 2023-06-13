package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.csbaic.edatope.app.entity.WorkStageType;
import com.csbaic.edatope.app.mapper.WorkStageTypeMapper;
import com.csbaic.edatope.app.service.IWorkStageTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 工作阶段任务类型表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
@Service
public class WorkStageTypeServiceImpl extends ServiceImpl<WorkStageTypeMapper, WorkStageType> implements IWorkStageTypeService {

    @Override
    @Transactional
    public void setBizTypes(String stageId, List<String> bizTypes) {
        if (CollectionUtils.isEmpty(bizTypes)) {
            return;
        }

        remove(
                Wrappers.<WorkStageType>query().eq(WorkStageType.STAGE_ID, stageId)
        );

        bizTypes.forEach(s -> {
            WorkStageType workStageType = new WorkStageType();
            workStageType.setStageId(stageId);
            workStageType.setBizType(s);
            save(workStageType);
        });
    }

    @Override
    @Transactional
    public void removeBizTypes(String stageId) {
        remove(Wrappers.<WorkStageType>query().eq(WorkStageType.STAGE_ID, stageId));
    }
}
