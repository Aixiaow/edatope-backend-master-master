package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.WorkStageAuthorize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.WorkStageAuthorizeQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工作阶段授权表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
public interface WorkStageAuthorizeMapper extends BaseMapper<WorkStageAuthorize> {

    IPage<WorkStageAuthorize> listPage(IPage<WorkStageAuthorize> page, @Param("query") WorkStageAuthorizeQuery query);

}
