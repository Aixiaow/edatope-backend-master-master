package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.Enterprise;
import com.csbaic.edatope.app.mapper.EnterpriseMapper;
import com.csbaic.edatope.app.model.dto.EnterpriseVO;
import com.csbaic.edatope.app.model.query.EnterpriseQuery;
import com.csbaic.edatope.app.service.IEnterpriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.constants.DictConstants;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 被调查的企业 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
@Service
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, Enterprise> implements IEnterpriseService {

    @Autowired
    private IDictService dictService;

    @Override
    public List<EnterpriseVO> listByName(EnterpriseQuery query) {
        QueryWrapper<Enterprise> queryWrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(query.getName())) {
            queryWrapper.like(Enterprise.NAME, query.getName());
        }

        return list(queryWrapper)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public EnterpriseVO getDetailById(String id) {
        Enterprise enterprise = getById(id);
        if(enterprise == null){
            return null;
        }

        return this.convert(enterprise);
    }

    public EnterpriseVO convert(Enterprise enterprise) {
        EnterpriseVO enterpriseVO = new EnterpriseVO();
        BeanCopyUtils.copyNonNullProperties(enterprise, enterpriseVO);
        enterpriseVO.setCategory(Splitter.on(",").splitToList(enterprise.getCategory()));

        if (CollectionUtils.isNotEmpty(enterpriseVO.getCategory())) {
            List<String> categoryDesc = new ArrayList<>();
            for (String category : enterpriseVO.getCategory()) {
                DictDTO dict = dictService.getDictByValue(DictConstants.CLASSIFICATION_TYPE, category);
                categoryDesc.add(dict.getValue() + "." + dict.getName());
            }
            enterpriseVO.setCategoryDesc(String.join(",", categoryDesc));
        }
        return enterpriseVO;
    }
}
