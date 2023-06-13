package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.csbaic.edatope.app.mapper.OrganizationBizTypeMapper;
import com.csbaic.edatope.app.service.IOrganizationBizTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统组织机构业务类型表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-07
 */
@Service
public class OrganizationBizTypeServiceImpl extends ServiceImpl<OrganizationBizTypeMapper, OrganizationBizType> implements IOrganizationBizTypeService {

    @Override
    public void setBizTypes(String orgId, List<String> bizTypes) {
        if (CollectionUtils.isEmpty(bizTypes)) {
            return;
        }

        remove(
                Wrappers.<OrganizationBizType>query().eq(OrganizationBizType.ORG_ID, orgId)
        );

        bizTypes.forEach(s -> {
            OrganizationBizType organizationBizType = new OrganizationBizType();
            organizationBizType.setOrgId(orgId);
            organizationBizType.setBizType(s);
            save(organizationBizType);
        });
    }

    @Override
    public List<String> getBizTypes(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            return new ArrayList<>();
        }
        List<OrganizationBizType> bizTypes = list(
                Wrappers.<OrganizationBizType>query().eq(OrganizationBizType.ORG_ID, orgId)
        );

        return bizTypes.stream().map(OrganizationBizType::getBizType).collect(Collectors.toList());
    }

    @Override
    public void removeBizTypes(String orgId) {
        remove(Wrappers.<OrganizationBizType>query().eq(OrganizationBizType.ORG_ID, orgId));
    }
}
