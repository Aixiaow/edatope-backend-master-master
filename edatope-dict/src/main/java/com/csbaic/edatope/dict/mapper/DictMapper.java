package com.csbaic.edatope.dict.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.dict.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.dict.model.query.DictQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统字典表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 分页查询字典
     * @param page
     * @param query
     * @return
     */
    IPage<Dict> listDictPage(IPage<Dict> page, @Param("query") DictQuery query);

    /**
     * 查询字典
     * @param id
     * @return
     */
    Dict getDictById(@Param("id") String id);

    /**
     * 查询字典
     * @param typeList
     * @return
     */
    List<Dict> listDictByType(@Param("typeList") List<String> typeList);
}
