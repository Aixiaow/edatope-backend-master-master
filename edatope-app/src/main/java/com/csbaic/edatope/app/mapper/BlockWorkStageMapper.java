package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.BlockWorkStage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 地块工作阶段 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
public interface BlockWorkStageMapper extends BaseMapper<BlockWorkStage> {

    List<BlockWorkStage> listByBlockId(@Param("blockId") String blockId);

}
