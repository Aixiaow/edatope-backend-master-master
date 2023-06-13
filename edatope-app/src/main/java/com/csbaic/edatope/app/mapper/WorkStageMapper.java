package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.WorkStage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import com.csbaic.edatope.app.service.impl.OrganizationServiceImpl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工作阶段表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
public interface WorkStageMapper extends BaseMapper<WorkStage> {

    IPage<WorkStage> listPage(IPage<WorkStage> page, @Param("query") WorkStageQuery query);

    List<WorkStage> listAll(@Param("query") WorkStageQuery query);
}
