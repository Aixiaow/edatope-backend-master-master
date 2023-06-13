package com.csbaic.edatope.dict.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.common.utils.CollectionStreamUtils;
import com.csbaic.edatope.common.utils.TreeListUtils;
import com.csbaic.edatope.dict.entity.Dict;
import com.csbaic.edatope.dict.mapper.DictMapper;
import com.csbaic.edatope.dict.model.command.CreateDictCommand;
import com.csbaic.edatope.dict.model.command.DeleteDictCommand;
import com.csbaic.edatope.dict.model.command.UpdateDictCommand;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.model.dto.DictTypeDTO;
import com.csbaic.edatope.dict.model.dto.GroupDict;
import com.csbaic.edatope.dict.model.query.DictQuery;
import com.csbaic.edatope.dict.repository.DictCache;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.utils.AreaUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.enums.OperateType;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统字典表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-03/
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Autowired
    private DictCache dictCache;

    @OperateLog(operateType = OperateType.CREATE)
    @Transactional
    @Override
    public void createDict(CreateDictCommand cmd) {
        if (!Strings.isNullOrEmpty(cmd.getPid())) {
            Dict parent = getById(cmd.getPid());
            if (parent == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级字典：" + cmd.getPid());
            }


            if (!Objects.equals(cmd.getType(), parent.getType())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "字典类型与上级字典类型不一致：" + cmd.getType());
            }
        }

        //同一类型值不能相同
        List<Dict> exists = list(Wrappers.<Dict>query().eq(Dict.TYPE, cmd.getType()));
        for(Dict d : exists){
            if (Objects.equals(d.getValue(), cmd.getValue())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "字典值已存在：" + cmd.getValue());
            }
        }

        Dict dict = new Dict();
        BeanCopyUtils.copyNonNullProperties(cmd, dict);
        //设置获取字典状态
        if(Strings.isNullOrEmpty(cmd.getStatus())){
            dict.setStatus(EnableStatus.ENABLED.name());
        }
        save(dict);
        dictCache.remove(dict.getType());
    }

    @Transactional
    @Override
    @OperateLog(operateType = OperateType.UPDATE)
    public void updateDict(UpdateDictCommand cmd) {
        Dict dict = getById(cmd.getId());
        if(dict == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到字典：" + cmd.getId());
        }

        String type = Strings.isNullOrEmpty(cmd.getType()) ? dict.getType() : cmd.getType();
        if (!Strings.isNullOrEmpty(cmd.getValue()) && !Objects.equals(cmd.getValue(), dict.getValue())) {
            //同一类型值不能相同
            List<Dict> exists = list(Wrappers.<Dict>query().eq(Dict.TYPE, type));
            for(Dict d : exists){
                if (Objects.equals(d.getValue(), cmd.getValue())) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "字典值已存在：" + cmd.getValue());
                }
            }
        }

        if (!Strings.isNullOrEmpty(cmd.getPid())) {
            Dict parent = getById(cmd.getPid());
            if(parent == null){
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级字典：" + cmd.getPid());
            }

            if (!Objects.equals(type, parent.getType())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "字典类型与上级字典类型不一致：" + type);
            }
        }

        BeanCopyUtils.copyNonNullProperties(cmd, dict);
        updateById(dict);
        dictCache.remove(dict.getId());
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public IPage<DictDTO> listPageDict(DictQuery query) {
        IPage<DictDTO> page = getBaseMapper().listDictPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(DictServiceImpl::convertToDictDTO);

        List<String> parentIds =   page.getRecords()
                .stream()
                .map(DictDTO::getPid)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toList());

        List<Dict> parentList = org.apache.commons.collections.CollectionUtils.isNotEmpty(parentIds) ? listByIds( parentIds  ) : new ArrayList<>();
        Map<String, DictDTO> parentMap = parentList
                .stream()
                .map(DictServiceImpl::convertToDictDTO)
                .collect(Collectors.toMap(DictDTO::getId, java.util.function.Function.identity(), (a, b) -> a));

        //设置上级字典
        page.getRecords().forEach(dictDTO -> {
            if (parentMap.containsKey(dictDTO.getPid())) {
                dictDTO.setParent(parentMap.get(dictDTO.getPid()));
            }
        });
        return page;
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public DictDTO getDictById(String id) {
        Dict dict = getBaseMapper().getDictById(id);
        if(dict == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到字典：" + id);
        }

        final DictDTO dictDTO =  convertToDictDTO(dict);
        if (!Strings.isNullOrEmpty(dictDTO.getPid())) {
            Dict parent = getById(dictDTO.getPid());
            dictDTO.setParent(convertToDictDTO(parent));
        }

        final List<DictDTO> direct = new ArrayList<>();
        List<DictDTO> typeDictList = listFlatDictByType(Lists.newArrayList(dict.getType()));
        TreeListUtils.tree(typeDictList, DictDTO::getId, DictDTO::getPid, (parent, child) -> {
            if (Objects.equals(child.getPid(), dictDTO.getId())) {
                direct.add(child);
            }

            if(parent != null){
                parent.getChildren().add(child);
            }
        });
        dictDTO.setChildren(direct);
        return dictDTO;
    }

    @Override
    public DictDTO getDictByName(String type, String name) {
        Dict dict = getOne(
                Wrappers.<Dict>query().eq(Dict.TYPE, type)
                .eq(Dict.NAME, name)
        );

        return dict != null ? convertToDictDTO(dict) : null;
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<DictDTO> listLevelDictByType(List<String> typeList) {
        List<Dict> dict = getBaseMapper().listDictByType(typeList);
        List<DictDTO> dictDTOList =   dict.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
        List<DictDTO> tree = new ArrayList<>();
        TreeListUtils.tree(dictDTOList, DictDTO::getId, DictDTO::getPid, (parent, node) -> {
            if(parent == null){
                tree.add(node);
            }else{
                parent.getChildren().add(node);
            }
        });
        return tree;
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<GroupDict> listGroupedDictByType(List<String> typeList) {
        List<Dict> dict = getBaseMapper().listDictByType(typeList);
        Map<String, GroupDict> map = new HashMap<>();
        dict.forEach(item -> {
            map.computeIfAbsent(item.getType(), s -> {
                GroupDict groupDict = new GroupDict();
                groupDict.setType(s);
                groupDict.setDictList(new ArrayList<>());
                return groupDict;
            });
            map.get(item.getType()).getDictList().add(convertToDictDTO(item));
        });
        return new ArrayList<>(map.values());
    }

    @Override
    public List<DictDTO> listFlatDictByType(List<String> typeList) {
        List<Dict> dict = getBaseMapper().listDictByType(typeList);
        return dict.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
    }

    @Override
    public List<DictDTO> listCachedFlatDictByType(List<String> typeList) {
        return null;
    }

    @Override
    public List<DictDTO> listAll() {
        List<Dict> dict = list(
                Wrappers.<Dict>query().orderByAsc(Dict.TYPE, Dict.SORT)
        );
        return dict.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList());
    }

    @Override
    public Map<String, DictDTO> getDictValueMap(String type) {
        return CollectionStreamUtils.toMap(listFlatDictByType(Lists.newArrayList(type)), DictDTO::getValue);
    }

    @OperateLog(operateType = OperateType.DELETE)
    @Transactional
    @Override
    public void deleteDict(DeleteDictCommand cmd) {
        Dict dict = getById(cmd.getId());
        if (dict == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到字典：" + cmd.getId());
        }
        List<Dict> children = list(Wrappers.<Dict>query().eq(Dict.PID, dict.getId()));
        if (CollectionUtils.isNotEmpty(children)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "请先删除子字典");
        }
        removeById(dict.getId());
        dictCache.remove(dict.getId());
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<DictTypeDTO> listDictType() {
        List<Dict> dicts = list(
                Wrappers.<Dict>query().groupBy(Dict.TYPE).orderByAsc(Dict.SORT)
        );

        return dicts.stream().map(new Function<Dict, DictTypeDTO>() {
            @Nullable
            @Override
            public DictTypeDTO apply(@Nullable Dict input) {
                DictTypeDTO dictTypeDTO = new DictTypeDTO();
                dictTypeDTO.setType(input.getType());
                return dictTypeDTO;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 获取字典
     *
     * @param type
     * @param value
     * @return
     */
    public DictDTO getDictByValue(String type, String value) {
        return convertToDictDTO(
                getOne(
                        Wrappers.<Dict>query()
                                .eq(Dict.TYPE, type)
                                .eq(Dict.VALUE, value)
                )
        );
    }

    @Override
    public DictDTO getAreaDetailByCode(String code) {
        if (Objects.isNull(code)) {
            DictDTO dictDTO = new DictDTO();
            List<Dict> children = list(
                    Wrappers.<Dict>query().eq(Dict.PID, "").eq(Dict.TYPE, "area")
            );
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(children)) {
                dictDTO.setChildren(
                        children.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList())
                );
            }
            return dictDTO;
        }

        Dict dict = getOne(Wrappers.<Dict>query().eq(Dict.VALUE, code));
        if(dict == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到区域编号：" + code);
        }
        DictDTO dictDTO = convertToDictDTO(dict);
        List<Dict> children = list(
                Wrappers.<Dict>query().eq(Dict.PID, code)
        );
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(children)) {
            dictDTO.setChildren(
                    children.stream().map(DictServiceImpl::convertToDictDTO).collect(Collectors.toList())
            );
        }
        return dictDTO;
    }

    @Override
    public DictDTO getAreaByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        Dict dict = getOne(Wrappers.<Dict>query().eq(Dict.VALUE, code).eq("type", "area"));
        if (dict == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到区域编号：" + code);
        }
        return convertToDictDTO(dict);
    }

    @Override
    public AreaDTO getFlatAreaDetailByCode(String areaCode) {
        DictDTO dictDTO = this.getAreaByCode(areaCode);
        if (dictDTO == null) {
            return null;
        }

        List<DictDTO> cities = new ArrayList<>();
        cities.add(dictDTO);

        while (StringUtils.isNotEmpty(dictDTO.getPid())) {
            dictDTO = this.getAreaByCode(dictDTO.getPid());
            cities.add(dictDTO);
        }

        AreaDTO area = new AreaDTO();
        for(DictDTO dict : cities){
            if (AreaUtils.isProvince(dict.getValue())) {
                area.setProvinceCode(dict.getValue());
                area.setProvinceName(dict.getName());
            } else if (AreaUtils.isCity(dict.getValue())) {
                area.setCityCode(dict.getValue());
                area.setCityName(dict.getName());
            } else if (AreaUtils.isDistrict(dict.getValue())) {
                area.setDistrictCode(dict.getValue());
                area.setDistrictName(dict.getName());
            }
        }
        return area;
    }

    @Override
    public DictDTO getParentArea(String areaCode) {
        AreaDTO areaDTO = getFlatAreaDetailByCode(areaCode);
        if (areaDTO == null) {
            return null;
        }

        DictDTO province = getAreaDetailByCode(areaDTO.getProvinceCode());
        DictDTO city = getAreaDetailByCode(areaDTO.getCityCode());
        DictDTO district = getAreaDetailByCode(areaDTO.getDistrictCode());

        if (district != null) {
            province.setChildren(Lists.newArrayList(city));
            city.setChildren(Lists.newArrayList(district));
            return province;
        } else if (city != null) {
            city.setChildren(Lists.newArrayList(district));
            return city;
        } else {
            return province;
        }
    }

    /**
     * 数据转换
     *
     * @param dict
     * @return
     */
    public static DictDTO convertToDictDTO(Dict dict) {
        DictDTO dictDTO = new DictDTO();
        BeanCopyUtils.copyNonNullProperties(dict, dictDTO);
        dictDTO.setChildren(new ArrayList<>());
        return dictDTO;
    }

}
