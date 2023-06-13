package com.csbaic.edatope.app.convert;

import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.common.utils.CollectionStreamUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class OrganizationConvert {

    @Autowired
    private IDictService dictService;

    public OrganizationDTO convertDTO(Organization organization) {
        if (organization == null) {
            return null;
        }

        OrganizationDTO dto = new OrganizationDTO();
        BeanCopyUtils.copyNonNullProperties(organization, dto);

        if (CollectionUtils.isNotEmpty(organization.getBizType())) {
            dto.setBizType(
                    organization.getBizType()
                            .stream()
                            .map(OrganizationBizType::getBizType)
                            .collect(Collectors.toList())
            );
            dto.setBizTypeDesc(new ArrayList<>());

            Map<String, DictDTO> dictMap = dictService.getDictValueMap("organization_type");
            dto.getBizType().forEach(s -> {
                DictDTO dictDTO = dictMap.get(s);
                if (dictDTO != null) {
                    dto.getBizTypeDesc().add(dictDTO.getName());
                }
            });
        }

        return dto;
    }
}
