package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.DetectionTargetClassifyType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.entity.OrganizationBizType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指标分类维护指标关系表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
public interface DetectionTargetClassifyTypeMapper extends BaseMapper<DetectionTargetClassifyType> {

    List<DetectionTargetClassifyType> ListClassifyType(@Param("targetId") String targetId);

}
