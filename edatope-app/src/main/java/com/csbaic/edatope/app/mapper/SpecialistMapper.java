package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Specialist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.dto.SpecialistPageResultVO;
import com.csbaic.edatope.app.model.query.PointSpecialistPageQuery;
import com.csbaic.edatope.app.model.query.SpecialistQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 专家组表 Mapper 接口
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
public interface SpecialistMapper extends BaseMapper<Specialist> {

    IPage<Specialist> pointSpecialistTaskPage(IPage<Specialist> page, @Param("query") PointSpecialistPageQuery query);

    IPage<SpecialistPageResultVO> page(IPage<SpecialistPageResultVO> page, @Param("query") SpecialistQuery query);
}
