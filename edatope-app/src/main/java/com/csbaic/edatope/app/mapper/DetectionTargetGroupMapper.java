package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.DetectionTarget;
import com.csbaic.edatope.app.entity.DetectionTargetGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 检测指标分组表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
public interface DetectionTargetGroupMapper extends BaseMapper<DetectionTargetGroup> {

    List<DetectionTargetGroup> listAll(@Param("query") DetectionTargetAllQuery query);

}
