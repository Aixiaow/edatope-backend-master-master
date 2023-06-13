package com.csbaic.edatope.app.service.impl;

import com.csbaic.edatope.app.entity.DetectionTarget;
import com.csbaic.edatope.app.entity.DetectionTargetClassifyType;
import com.csbaic.edatope.app.entity.DetectionTargetGroup;
import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.app.mapper.DetectionTargetGroupMapper;
import com.csbaic.edatope.app.model.dto.DetectionTargetDTO;
import com.csbaic.edatope.app.model.dto.TargetGroupDTO;
import com.csbaic.edatope.app.model.query.DetectionTargetAllQuery;
import com.csbaic.edatope.app.service.IDetectionTargetGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.service.IDetectionTargetService;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 检测指标分组表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-09
 */
@Service
public class DetectionTargetGroupServiceImpl extends ServiceImpl<DetectionTargetGroupMapper, DetectionTargetGroup> implements IDetectionTargetGroupService {

    @Autowired
    private IDetectionTargetService targetService;

    @Override
    public List<TargetGroupDTO> list(DetectionTargetAllQuery query) {
        return getBaseMapper().listAll(query)
                .stream()
                .map(this::convertDTO)
                .collect(Collectors.toList());
    }

    public TargetGroupDTO convertDTO(DetectionTargetGroup group) {
        if (group == null) {
            return null;
        }

        TargetGroupDTO dto = new TargetGroupDTO();
        BeanCopyUtils.copyNonNullProperties(group, dto);
        /*if (group.getSampleType().equals("D007-001") || group.getSampleType().equals("D007-002")) {
            dto.setSampleTypeDec("土壤");
        } else {
            dto.setSampleTypeDec("地下水");
        }*/

        dto.setVolatilizeDes(YesOrNoEnum.valueOf(group.getVolatilize()).getValue());

        if (StringUtils.isNotEmpty(group.getTargetId())) {
            List<String> targetList = Arrays.asList(group.getTargetId().split(","));

            dto.setTargetList(new ArrayList<>());
            targetList.forEach(s -> {
                DetectionTarget target = targetService.getById(s);
                if (target != null) {
                    DetectionTargetDTO targetDTO = targetService.convertDTO(target);
                    dto.getTargetList().add(targetDTO);
                }
            });
        }
        return dto;
    }
}
