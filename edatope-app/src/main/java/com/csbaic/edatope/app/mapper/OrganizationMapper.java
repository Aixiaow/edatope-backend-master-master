package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.QualityControlOrgList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统组织机构表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * 查询单位
     *
     * @param query
     * @return
     */
    IPage<Organization> listPage(IPage<Organization> page, @Param("query") OrganizationQuery query);

    /**
     * 查询单位
     *
     * @param query
     * @return
     */
    List<Organization> list(@Param("query") OrganizationQuery query);

    Organization getDetail(@Param("id") String id);

    /**
     * 根据条件查询布点质控单位
     *
     * @param query
     * @return
     */
    IPage<Organization> qualityControlOrgPage(IPage<Organization> page, @Param("query") QualityControlOrgList query);
}
