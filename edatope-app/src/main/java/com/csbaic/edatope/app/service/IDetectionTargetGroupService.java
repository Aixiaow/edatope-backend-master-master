package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.DetectionTargetGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.dto.TargetGroupDTO;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;

import java.util.List;

/**
 * <p>
 * 检测指标分组表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
public interface IDetectionTargetGroupService extends IService<DetectionTargetGroup> {

    List<TargetGroupDTO> list(DetectionTargetAllQuery query);
}
