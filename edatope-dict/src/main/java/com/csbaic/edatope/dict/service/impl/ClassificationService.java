package com.csbaic.edatope.dict.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.dict.constants.DictConstants;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class ClassificationService {

    @Autowired
    private IDictService dictService;


    /**
     * 获取字典
     *
     * @param code
     * @return
     */
    public DictDTO getClassificationDetail(String code) {
        if (Objects.isNull(code)) {
            DictDTO dictDTO = new DictDTO();
            List<Dict> children = dictService.list(
                    Wrappers.<Dict>query().eq(Dict.PID, "").eq(Dict.TYPE, DictConstants.CLASSIFICATION_TYPE)
            );
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(children)) {
                dictDTO.setChildren(
                        children.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList())
                );
            }
            return dictDTO;
        }

        Dict dict = dictService.getOne(Wrappers.<Dict>query().eq(Dict.VALUE, code).eq(Dict.TYPE, DictConstants.CLASSIFICATION_TYPE));
        if (dict == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到分类编号：" + code);
        }
        DictDTO dictDTO = DictServiceImpl.convertToDictDTO(dict);
        List<Dict> children = dictService.list(
                Wrappers.<Dict>query().eq(Dict.PID, code)
        );
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(children)) {
            dictDTO.setChildren(
                    children.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList())
            );
        }
        return dictDTO;
    }


}
