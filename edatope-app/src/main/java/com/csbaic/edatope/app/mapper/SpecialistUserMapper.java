package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.SpecialistUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.SpecialistUserQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 专家组组员 Mapper 接口
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
public interface SpecialistUserMapper extends BaseMapper<SpecialistUser> {
    IPage<SpecialistUser> specialistUserPage(IPage<SpecialistUser> page, @Param("query") SpecialistUserQuery query);
}
