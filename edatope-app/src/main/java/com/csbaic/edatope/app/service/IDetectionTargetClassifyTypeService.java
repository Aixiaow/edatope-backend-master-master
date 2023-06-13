package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.DetectionTargetClassifyType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 指标分类维护指标关系表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
public interface IDetectionTargetClassifyTypeService extends IService<DetectionTargetClassifyType> {

    public void setBizTypes(String targetClassId, List<String> target);

    public void removeBizTypes(String targetClassId);
}
