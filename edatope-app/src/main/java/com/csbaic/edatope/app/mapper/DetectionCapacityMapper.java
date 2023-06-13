package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionCapacity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.entity.WorkStage;
import com.csbaic.edatope.app.model.query.DetectionCapacityQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 检测能力表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
public interface DetectionCapacityMapper extends BaseMapper<DetectionCapacity> {

    IPage<DetectionCapacity> listPage(IPage<DetectionCapacity> page, @Param("query") DetectionCapacityQuery query);


}
