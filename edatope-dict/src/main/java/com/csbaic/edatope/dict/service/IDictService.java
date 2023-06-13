package com.csbaic.edatope.dict.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.dict.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.dict.model.command.*;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.model.dto.DictTypeDTO;
import com.csbaic.edatope.dict.model.dto.GroupDict;
import com.csbaic.edatope.dict.model.query.AreaQuery;
import com.csbaic.edatope.dict.model.query.DictQuery;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 系统字典表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
public interface IDictService extends IService<Dict> {

    /**
     * 创建字典
     * @param dict
     */
    void createDict(CreateDictCommand dict);

    /**
     * 更新字典
     * @param dictDTO
     */
    void updateDict(UpdateDictCommand dictDTO);

    /**
     * 字典查询
     * @param query
     * @return
     */
    IPage<DictDTO> listPageDict(DictQuery query);

    /**
     * 获取字典
     * @param id
     * @return
     */
    DictDTO getDictById(String id);

    /**
     * 获取字典
     *
     * @param type
     * @param name
     * @return
     */
    DictDTO getDictByName(String type, String name);

    /**
     * 通过字典编码获取数据（按层级的）
     * @param typeList
     * @return
     */
    List<DictDTO> listLevelDictByType(List<String> typeList);

    /**
     * 通过字典编码获取数据（不按层级）
     *
     * @param typeList
     * @return
     */
    List<GroupDict> listGroupedDictByType(List<String> typeList);

    /**
     * 通过字典编码获取数据（不按层级）
     *
     * @param typeList
     * @return
     */
    List<DictDTO> listFlatDictByType(List<String> typeList);

    /**
     * 字典值map
     * @param type
     * @return
     */
    Map<String, DictDTO> getDictValueMap(String type);

    /**
     * 通过字典编码获取数据（不按层级）,緩存數據
     *
     * @param typeList
     * @return
     */
    List<DictDTO> listCachedFlatDictByType(List<String> typeList);


    /**
     * 查询所有
     */
    List<DictDTO> listAll();

    /**
     * 删除字典
     *
     * @param id
     */
    void deleteDict(DeleteDictCommand id);

    /**
     * 获取字典类型列表
     *
     * @return
     */
    List<DictTypeDTO> listDictType();

    /**
     * 获取字典
     *
     * @param type
     * @param value
     * @return
     */
    DictDTO getDictByValue(String type, String value);

    /**
     * 城市及下级城市查询
     *
     * @return
     */
    AreaDTO getFlatAreaDetailByCode(String code);

    /**
     * 城市及下级城市查询
     *
     * @return
     */
    DictDTO getAreaDetailByCode(String code);

    /**
     * 城市查询
     *
     * @return
     */
    DictDTO getAreaByCode(String code);


    /**
     * @param areaCode
     * @return
     */
    DictDTO getParentArea(String areaCode);
}

