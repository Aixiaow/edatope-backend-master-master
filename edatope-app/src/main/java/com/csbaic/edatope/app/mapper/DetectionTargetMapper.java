package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionTarget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.entity.WorkStage;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 检测指标表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
public interface DetectionTargetMapper extends BaseMapper<DetectionTarget> {

    IPage<DetectionTarget> listPage(IPage<DetectionTarget> page, @Param("query") DetectionTargetQuery query);

    List<DetectionTarget> listAll(@Param("query") DetectionTargetAllQuery query);
}
