package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionCapacity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateDetectionCapacityCmd;
import com.csbaic.edatope.app.model.command.CreateDetectionMethodCmd;
import com.csbaic.edatope.app.model.dto.DetectionCapacityDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.DetectionCapacityAuditBody;
import com.csbaic.edatope.app.model.query.DetectionCapacityQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import com.csbaic.edatope.common.result.Result;

/**
 * <p>
 * 检测能力表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
public interface IDetectionCapacityService extends IService<DetectionCapacity> {

    public void create(CreateDetectionCapacityCmd cmd);

    public IPage<DetectionCapacityDTO> listPage(DetectionCapacityQuery query);

    public DetectionCapacityDTO getCapacityDetail(String id);

    public Result pass(DetectionCapacityAuditBody body);

    public void update(DetectionCapacityDTO dto);

    public void delete(String id);
}
