package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.PointFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.dto.PointFileDTO;

import java.util.List;

/**
 * <p>
 * 布点方案数据维护 服务类
 * </p>
 *
 * @author bug
 * @since 2022-04-26
 */
public interface IPointFileService extends IService<PointFile> {

    /**
     * 根据地块工作阶段id查询方案文件
     * @param blockWorkStageId
     * @return
     */
    List<PointFileDTO> listByBlockWorkStageId(String blockWorkStageId);
}
