package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.SpecialistUser;
import com.csbaic.edatope.app.mapper.SpecialistUserMapper;
import com.csbaic.edatope.app.model.query.SpecialistUserQuery;
import com.csbaic.edatope.app.service.ISpecialistUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专家组组员 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Service
public class SpecialistUserServiceImpl extends ServiceImpl<SpecialistUserMapper, SpecialistUser> implements ISpecialistUserService {

    /**
     * 布点质控专家-列表-分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<SpecialistUser> specialistUserPage(SpecialistUserQuery query) {
        return getBaseMapper().specialistUserPage(new Page<SpecialistUser>(query.getPageIndex(), query.getPageSize()), query);
    }
}
