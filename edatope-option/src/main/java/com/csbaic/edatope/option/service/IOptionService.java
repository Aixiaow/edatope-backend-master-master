package com.csbaic.edatope.option.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.option.entity.Option;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.option.model.OptionDTO;
import com.csbaic.edatope.option.model.OptionQuery;
import com.csbaic.edatope.option.model.command.CreateOptionCmd;
import com.csbaic.edatope.option.model.command.DeleteOptionCmd;
import com.csbaic.edatope.option.model.command.UpdateOptionCmd;

import java.util.List;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-05
 */
public interface IOptionService extends IService<Option> {

    /**
     * 创建配置
     * @param cmd
     */
    void createOption(CreateOptionCmd cmd);

    /**
     * 更新配置
     * @param cmd
     */
    void updateOption(UpdateOptionCmd cmd);

    /**
     * 删除
     * @param cmd
     */
    void deleteOption(DeleteOptionCmd cmd);

    /**
     * 分页查询
     * @param query
     * @return
     */
    IPage<OptionDTO> getOptionPage(OptionQuery query);

    /**
     * 获取详情
     * @param id
     * @return
     */
    OptionDTO getOptionById(String id);

    /**
     * 获取详情
     * @param keys
     * @return
     */
    List<OptionDTO> getOptionByKeys(List<String> keys);

    /**
     * 获取详情
     * @param key
     * @return
     */
    OptionDTO getOptionByKey(String key);


    /**
     * 获取配置值
     * @param key
     * @return
     */
    String getValueByKey(String key);
}
