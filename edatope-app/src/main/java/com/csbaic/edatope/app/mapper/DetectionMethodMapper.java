package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.DetectionMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.entity.DetectionTarget;
import com.csbaic.edatope.app.model.query.DetectionMethodQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 检测方法表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-02
 */
public interface DetectionMethodMapper extends BaseMapper<DetectionMethod> {

    IPage<DetectionMethod> listPage(IPage<DetectionMethod> page, @Param("query") DetectionMethodQuery query);
}
