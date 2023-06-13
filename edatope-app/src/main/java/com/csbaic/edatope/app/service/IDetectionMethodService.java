package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionMethod;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateDetectionMethodCmd;
import com.csbaic.edatope.app.model.dto.DetectionMethodDTO;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.query.DetectionMethodQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;

/**
 * <p>
 * 检测方法表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-02
 */
public interface IDetectionMethodService extends IService<DetectionMethod> {

    public void create(CreateDetectionMethodCmd cmd);

    IPage<DetectionMethodDTO> listPage(DetectionMethodQuery query);
}
