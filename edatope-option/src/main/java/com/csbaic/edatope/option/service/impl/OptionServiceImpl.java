package com.csbaic.edatope.option.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.context.OperateLogHelper;
import com.csbaic.edatope.log.enums.OperateType;
import com.csbaic.edatope.option.entity.Option;
import com.csbaic.edatope.option.mapper.OptionMapper;
import com.csbaic.edatope.option.model.OptionDTO;
import com.csbaic.edatope.option.model.OptionQuery;
import com.csbaic.edatope.option.model.command.CreateOptionCmd;
import com.csbaic.edatope.option.model.command.DeleteOptionCmd;
import com.csbaic.edatope.option.model.command.UpdateOptionCmd;
import com.csbaic.edatope.option.service.IOptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-05
 */
@Service
public class OptionServiceImpl extends ServiceImpl<OptionMapper, Option> implements IOptionService {

    @OperateLog(operateType = OperateType.CREATE)
    @Override
    public void createOption(CreateOptionCmd cmd) {
        Option option = new Option();
        BeanCopyUtils.copyNonNullProperties(cmd, option);
        save(option);
    }

    @OperateLog(operateType = OperateType.UPDATE)
    @Override
    public void updateOption(UpdateOptionCmd cmd) {
        Option option = getById(cmd.getId());
        if(option == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到配置："  + cmd.getId());
        }

        BeanCopyUtils.copyNonNullProperties(cmd, option);
        updateById(option);
    }

    @OperateLog(operateType = OperateType.DELETE)
    @Override
    public void deleteOption(DeleteOptionCmd cmd) {
        Option option = getById(cmd.getId());
        if(option == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到配置："  + cmd.getId());
        }

        removeById(option);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public IPage<OptionDTO> getOptionPage(OptionQuery query) {
        QueryWrapper<Option> queryWrapper = Wrappers.query();
        if (!Strings.isNullOrEmpty(query.getName())) {
            queryWrapper.like(Option.NAME, query.getName());
        }
        if (!Strings.isNullOrEmpty(query.getKey())) {
            queryWrapper.like(Option.KEY, query.getKey());
        }
        if (!Strings.isNullOrEmpty(query.getValue())) {
            queryWrapper.like(Option.VALUE, query.getValue());
        }
        if (!Strings.isNullOrEmpty(query.getId())) {
            queryWrapper.eq(Option.ID, query.getId());
        }
        return page(new Page<>(query.getPageIndex(), query.getPageSize()), queryWrapper)
                .convert(OptionServiceImpl::convertToOptionDTO);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public OptionDTO getOptionById(String id) {
        Option option = getById(id);
        if(option == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到配置："  + id);
        }
        return convertToOptionDTO(option);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<OptionDTO> getOptionByKeys(List<String> keys) {
        List<Option> optionList = list(Wrappers.<Option>query().in(Option.KEY, keys));
        return optionList.stream().map(OptionServiceImpl::convertToOptionDTO).collect(Collectors.toList());
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public OptionDTO getOptionByKey(String key) {
        Option option = getOne(Wrappers.<Option>query().in(Option.KEY, key));
        return option != null ? convertToOptionDTO(option) : null;
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public String getValueByKey(String key) {
        OptionDTO dto = getOptionByKey(key);
        return dto != null ? dto.getValue() : null;
    }

    public static OptionDTO convertToOptionDTO(Option option) {
        OptionDTO dto = new OptionDTO();
        BeanCopyUtils.copyNonNullProperties(option, dto);
        return dto;
    }

}
