package com.csbaic.edatope.dict.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.dict.constants.DictConstants;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.utils.AreaUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class AreaService {

    @Autowired
    private IDictService dictService;

    public List<DictDTO> listAll() {
        return dictService.list(
                Wrappers.<Dict>query().eq(Dict.TYPE, "area")
        ).stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
    }

    public List<DictDTO> listAreaInAreaCode(Collection<String> areaCodeList) {
        if (CollectionUtils.isEmpty(areaCodeList)) {
            return new ArrayList<>();
        }
        return dictService.list(
                Wrappers.<Dict>query().eq(Dict.TYPE, "area")
                        .in(Dict.VALUE, areaCodeList)
        ).stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
    }

    /**
     * 只查城市内的区域
     *
     * @param areaCode
     * @return
     */
    public List<DictDTO> listByAreaCode(String areaCode) {
        if (AreaUtils.isProvince(areaCode)) {
            return dictService
                    .list(
                            Wrappers.<Dict>query()
                                    .eq(Dict.TYPE, "area")
                                    .likeRight(Dict.VALUE, areaCode.substring(0, 2))
                    ).stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
        } else if (AreaUtils.isCity(areaCode)) {
            DictDTO dictDTO = dictService.getAreaDetailByCode(areaCode);
            DictDTO parent = dictService.getAreaByCode(dictDTO.getPid());
            List<DictDTO> areaList = new ArrayList<>(0);
            areaList.add(parent);
            areaList.add(dictDTO);
            areaList.addAll(dictDTO.getChildren());
            dictDTO.setChildren(new ArrayList<>());
            return areaList;
        } else {
            List<DictDTO> areaList = new ArrayList<>(0);
            AreaDTO areaDTO = dictService.getFlatAreaDetailByCode(areaCode);
            areaList.add(dictService.getAreaByCode(areaDTO.getProvinceCode()));
            areaList.add(dictService.getAreaByCode(areaDTO.getCityCode()));
            areaList.add(dictService.getAreaByCode(areaDTO.getDistrictCode()));
            return areaList;
        }
    }


    /**
     * 获取字典
     *
     * @param code
     * @param supplier
     * @return
     */
    public List<DictDTO> findAreaDetailByCode(String code, Supplier<List<DictDTO>> supplier) {
        List<DictDTO> areaList = supplier.get();
        Map<String, DictDTO> areaCodeMap = areaList
                .stream()
                .collect(Collectors.toMap(DictDTO::getValue, Function.identity()));

        List<DictDTO> root = new ArrayList<>();
        areaList.forEach(dictDTO -> {
            if (StringUtils.isEmpty(dictDTO.getPid())) {
                root.add(dictDTO);
            }
        });

        if (StringUtils.isEmpty(code)) {
            areaList.forEach(dictDTO -> {
                DictDTO parent = areaCodeMap.get(dictDTO.getPid());
                if (parent != null) {
                    parent.getChildren().add(dictDTO);
                }
            });
            return root;
        }

        DictDTO targetArea = areaCodeMap.get(code);
        if (targetArea == null) {
            return null;
        }

        areaList.forEach(dictDTO -> {
            if (Objects.equals(dictDTO.getPid(), targetArea.getValue())) {
                targetArea.getChildren().add(dictDTO);
            }
        });

        return Lists.newArrayList(targetArea);
    }

    public DictDTO getAreaByName(String name) {
        Dict dict = dictService
                .getOne(
                        Wrappers.<Dict>query().eq(Dict.TYPE, DictConstants.AREA_DICT_TYPE)
                                .eq(Dict.NAME, name)
                );

        return dict != null ? DictServiceImpl.convertToDictDTO(dict) : null;
    }


    public List<AreaDTO> listAreaByPrefix(String prefix) {
        List<Dict> list = dictService
                .list(
                        Wrappers.<Dict>query().eq(Dict.TYPE, DictConstants.AREA_DICT_TYPE)
                                .likeRight(Dict.VALUE, prefix)
                );

        List<AreaDTO> areaDTOS = new ArrayList<>();
        list.forEach(dict -> {
            areaDTOS.add(
                    dictService.getFlatAreaDetailByCode(dict.getValue())
            );
        });

        return areaDTOS;
    }
}
