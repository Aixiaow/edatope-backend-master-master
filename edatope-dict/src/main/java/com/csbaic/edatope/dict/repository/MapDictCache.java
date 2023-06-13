package com.csbaic.edatope.dict.repository;

import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class MapDictCache implements DictCache {

    private Map<String, List<DictDTO>> cache = new ConcurrentHashMap<>();

    @Override
    public List<DictDTO> get(String key) {
        return cache.get(key);
    }

    @Override
    public void remove(String key) {
        List<DictDTO> dictList = cache.remove(key);
        if (dictList != null) {
            //按类型删除
            dictList.forEach(dictDTO -> cache.remove(dictDTO.getType()));
        }
    }

    @Override
    public void save(List<DictDTO> dictDTO) {
        dictDTO.forEach(dto -> cache.put(dto.getId(), Lists.newArrayList(dto)));
    }

    @Override
    public void save(String type, List<DictDTO> dictList) {
        cache.put(type, dictList);
        //同时按id缓存一下
        dictList.forEach(dictDTO -> save(Lists.newArrayList(dictDTO)));
    }
}
