package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.SpecialistUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.query.SpecialistUserQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 专家组组员 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
public interface ISpecialistUserService extends IService<SpecialistUser> {

    /**
     * 布点质控专家-列表-分页
     * @param query
     * @return
     */
    IPage<SpecialistUser> specialistUserPage(SpecialistUserQuery query);
}
