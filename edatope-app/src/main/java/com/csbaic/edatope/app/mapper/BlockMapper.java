package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Block;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.dto.BlockVO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.QualityControlTasksQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 地块 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
public interface BlockMapper extends BaseMapper<Block> {

    IPage<Block> listPage(IPage<Block> page, @Param("query") BlockQuery query);

    IPage<Block> pointUserListPage(IPage<Block> page, @Param("query") PointUserBlockQuery query);

    List<Block> list( @Param("query") BlockQuery query);


    Block getDetailById(@Param("id") String id);


    List<Block> listByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    List<Block> listByProjectId(@Param("projectId") String projectId);

    IPage<Block> pointUserTaskListPage(IPage<Block> page, @Param("query") PointUserBlockQuery query);

    IPage<Block> qualityControlTaskPage(IPage<Block> page, @Param("query") QualityControlTasksQuery query);

    IPage<Block> sendBackPage(IPage<Block> page, @Param("query") QualityControlTasksQuery query);

    IPage<Block> specialistPage(IPage<Block> page, @Param("query") QualityControlTasksQuery query);
}
