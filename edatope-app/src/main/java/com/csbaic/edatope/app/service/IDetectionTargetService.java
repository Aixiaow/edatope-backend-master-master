package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionTarget;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateDetectionTargetCmd;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;

import java.util.List;

/**
 * <p>
 * 检测指标表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
public interface IDetectionTargetService extends IService<DetectionTarget> {

    public void create(CreateDetectionTargetCmd cmd);

    IPage<DetectionTargetDTO> listPage(DetectionTargetQuery query);

    List<DetectionTargetDTO> listAll(DetectionTargetAllQuery query);

    DetectionTargetDTO convertDTO(DetectionTarget target);
}
