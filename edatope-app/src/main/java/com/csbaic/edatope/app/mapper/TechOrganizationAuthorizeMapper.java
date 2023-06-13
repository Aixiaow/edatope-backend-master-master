package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.TechOrganizationAuthorize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 技术单位授权表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-03-05
 */
public interface TechOrganizationAuthorizeMapper extends BaseMapper<TechOrganizationAuthorize> {
    List<String> getOrgIdByCityAndOwnId(@Param("ownerId") String ownerId, @Param("provinceCode") String provinceCode,
                                        @Param("cityCode") String cityCode, @Param("districtCode") String districtCode);
}
