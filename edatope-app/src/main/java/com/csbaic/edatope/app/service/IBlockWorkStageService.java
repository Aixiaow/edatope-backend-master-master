package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.BlockWorkStage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.UpdateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.dto.BlockWorkStageDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;

import java.util.List;

/**
 * <p>
 * 地块工作阶段 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
public interface IBlockWorkStageService extends IService<BlockWorkStage> {


    /**
     * 创建工作阶段
     *
     * @param cmd
     */
    void create(CreateBlockWorkStageCmd cmd);

    /**
     * 更新
     *
     * @param cmd
     */
    void update(UpdateBlockWorkStageCmd cmd);

    /**
     * 工作阶段列表
     *
     * @return
     */
    List<WorkStageDTO> listWorkState(String orgId);


    /**
     * 展出工作阶段
     *
     * @param blockId
     * @return
     */
    List<BlockWorkStageDTO> listBlockWordStage(String blockId);


    void delete(DeleteBlockWorkStageCmd deleteBlockCmd);

    /**
     * workState
     *
     * @param id
     * @return
     */
    BlockWorkStageDTO getBlockWorkStage(String id);
}
