package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.DetectionTarget;
import com.csbaic.edatope.app.entity.WorkStage;
import com.csbaic.edatope.app.entity.WorkStageType;
import com.csbaic.edatope.app.enums.SampleTypeEnums;
import com.csbaic.edatope.app.enums.StatusEnums;
import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.app.mapper.DetectionTargetMapper;
import com.csbaic.edatope.app.model.command.CreateDetectionTargetCmd;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;
import com.csbaic.edatope.app.model.query.DetectionTargetQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import com.csbaic.edatope.app.service.IDetectionTargetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 检测指标表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@Service
public class DetectionTargetServiceImpl extends ServiceImpl<DetectionTargetMapper, DetectionTarget> implements IDetectionTargetService {

    @Override
    @Transactional
    public void create(CreateDetectionTargetCmd cmd) {
        DetectionTarget nameExists = getOne(Wrappers.<DetectionTarget>query().eq(DetectionTarget.NAME, cmd.getName()));
        if (nameExists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "检测指标：" + cmd.getName() + " 已存在");
        }

        DetectionTarget numberExists = getOne(Wrappers.<DetectionTarget>query().eq(DetectionTarget.TARGET_NUMBER, cmd.getTargetNumber()));
        if (numberExists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "检测指标编号：" + cmd.getName() + " 已存在");
        }
        DetectionTarget target = new DetectionTarget();
        BeanUtils.copyProperties(cmd, target);
        target.setStatus(StatusEnums.NORMAL.name());
        save(target);
    }

    @Override
    public IPage<DetectionTargetDTO> listPage(DetectionTargetQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    @Override
    public List<DetectionTargetDTO> listAll(DetectionTargetAllQuery query) {
        return getBaseMapper().listAll(query)
                .stream()
                .map(this::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DetectionTargetDTO convertDTO(DetectionTarget target) {
        if (target == null) {
            return null;
        }

        DetectionTargetDTO dto = new DetectionTargetDTO();
        BeanCopyUtils.copyNonNullProperties(target, dto);
        if (target.getSampleType().equals("D007-001") || target.getSampleType().equals("D007-002")) {
            dto.setSampleTypeDec("土壤");
        } else {
            dto.setSampleTypeDec("地下水");
        }

        dto.setVolatilizeDes(YesOrNoEnum.valueOf(target.getVolatilize()).getValue());
        return dto;
    }
}
