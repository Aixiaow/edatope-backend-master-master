package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionTargetClassify;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateTargetClassifyCmd;
import com.csbaic.edatope.app.model.command.UpdateTargetClassifyCmd;
import com.csbaic.edatope.app.model.dto.TargetClassifyDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.TagetClassifyQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;

/**
 * <p>
 * 检测指标分类表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
public interface IDetectionTargetClassifyService extends IService<DetectionTargetClassify> {

    public void create(CreateTargetClassifyCmd cmd);

    public IPage<TargetClassifyDTO> listPage(TagetClassifyQuery query);

    public TargetClassifyDTO getDetail(String id);

    public void update(UpdateTargetClassifyCmd dto);

    public void delete(String id);

    public void authorization(String id);
}
