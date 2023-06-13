package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionTargetClassify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.entity.WorkStage;
import com.csbaic.edatope.app.model.query.TagetClassifyQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检测指标分类表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
public interface DetectionTargetClassifyMapper extends BaseMapper<DetectionTargetClassify> {

    IPage<DetectionTargetClassify> listPage(IPage<DetectionTargetClassify> page, @Param("query") TagetClassifyQuery query);

}
