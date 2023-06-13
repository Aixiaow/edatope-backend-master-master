package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.DetectionMethod;
import com.csbaic.edatope.app.entity.DetectionTarget;
import com.csbaic.edatope.app.enums.StatusEnums;
import com.csbaic.edatope.app.mapper.DetectionMethodMapper;
import com.csbaic.edatope.app.model.command.CreateDetectionMethodCmd;
import com.csbaic.edatope.app.model.dto.DetectionMethodDTO;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.query.DetectionMethodQuery;
import com.csbaic.edatope.app.service.IDetectionMethodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.service.IDetectionTargetService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Objects;

/**
 * <p>
 * 检测方法表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-02
 */
@Service
public class DetectionMethodServiceImpl extends ServiceImpl<DetectionMethodMapper, DetectionMethod> implements IDetectionMethodService {

    @Autowired
    private IDetectionTargetService detectionTargetService;

    @Override
    @Transient
    public void create(CreateDetectionMethodCmd cmd) {
        DetectionMethod target = new DetectionMethod();
        BeanUtils.copyProperties(cmd, target);
        target.setStatus(StatusEnums.NORMAL.name());
        save(target);
    }

    @Override
    public IPage<DetectionMethodDTO> listPage(DetectionMethodQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    public DetectionMethodDTO convertDTO(DetectionMethod method) {
        if (method == null) {
            return null;
        }

        DetectionMethodDTO dto = new DetectionMethodDTO();
        BeanCopyUtils.copyNonNullProperties(method, dto);

        DetectionTarget target = detectionTargetService.getById(method.getTargetId());
        DetectionTargetDTO targetDTO = new DetectionTargetDTO();
        if (!Objects.isNull(target)) {
            BeanUtils.copyProperties(target, targetDTO);
            if (target.getSampleType().equals("D007-001") || target.getSampleType().equals("D007-002")) {
                targetDTO.setSampleTypeDec("土壤");
            } else {
                targetDTO.setSampleTypeDec("地下水");
            }
        }
        dto.setTargetDTO(targetDTO);
        return dto;
    }
}
