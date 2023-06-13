package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.DetectionTargetClassifyType;
import com.csbaic.edatope.app.entity.WorkStageType;
import com.csbaic.edatope.app.mapper.DetectionTargetClassifyTypeMapper;
import com.csbaic.edatope.app.service.IDetectionTargetClassifyTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 指标分类维护指标关系表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
@Service
public class DetectionTargetClassifyTypeServiceImpl extends ServiceImpl<DetectionTargetClassifyTypeMapper, DetectionTargetClassifyType> implements IDetectionTargetClassifyTypeService {

    @Override
    @Transactional
    public void setBizTypes(String targetClassId, List<String> bizTypes) {
        if (CollectionUtils.isEmpty(bizTypes)) {
            return;
        }

        remove(
                Wrappers.<DetectionTargetClassifyType>query().eq(DetectionTargetClassifyType.TARGET_CLASSIFY_ID, targetClassId)
        );

        bizTypes.forEach(s -> {
            DetectionTargetClassifyType workStageType = new DetectionTargetClassifyType();
            workStageType.setTargetClassifyId(targetClassId);
            workStageType.setTargetId(s);
            save(workStageType);
        });
    }

    @Override
    @Transactional
    public void removeBizTypes(String targetClassId) {
        remove(Wrappers.<DetectionTargetClassifyType>query().eq(DetectionTargetClassifyType.TARGET_CLASSIFY_ID, targetClassId));
    }
}
