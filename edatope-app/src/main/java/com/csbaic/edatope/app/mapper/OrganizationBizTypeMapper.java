package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统组织机构业务类型表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-03-07
 */
public interface OrganizationBizTypeMapper extends BaseMapper<OrganizationBizType> {

    /**
     * 获取业务类型
     *
     * @param orgId
     * @return
     */
    List<OrganizationBizType> listOrganizationBizType(@Param("orgId") String orgId);
}
