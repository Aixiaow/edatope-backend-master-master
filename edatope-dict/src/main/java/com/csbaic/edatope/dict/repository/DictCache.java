package com.csbaic.edatope.dict.repository;

import com.csbaic.edatope.dict.model.dto.DictDTO;

import java.util.List;

/**
 * 字典仓库
 */
public interface DictCache {

    /**
     * @param key
     * @return
     */
    List<DictDTO> get(String key);

    /**
     * 移除缓存
     *
     * @param key
     */
    void remove(String key);

    /**
     * 按字典id, 缓存字典
     *
     * @param dictDTO
     */
    void save(List<DictDTO> dictDTO);

    /**
     * 按类型缓存字典
     *
     * @param type
     * @param dictList
     */
    void save(String type, List<DictDTO> dictList);
}
