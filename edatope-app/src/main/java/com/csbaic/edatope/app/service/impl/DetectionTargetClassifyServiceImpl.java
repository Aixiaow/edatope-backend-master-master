package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.AuditStatusEnum;
import com.csbaic.edatope.app.enums.StatusEnums;
import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.app.mapper.DetectionTargetClassifyMapper;
import com.csbaic.edatope.app.model.command.CreateTargetClassifyCmd;
import com.csbaic.edatope.app.model.command.UpdateTargetClassifyCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.TagetClassifyQuery;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 检测指标分类表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-08
 */
@Service
public class DetectionTargetClassifyServiceImpl extends ServiceImpl<DetectionTargetClassifyMapper, DetectionTargetClassify> implements IDetectionTargetClassifyService {

    @Autowired
    private IDetectionTargetService targetService;

    @Autowired
    private IDetectionTargetClassifyTypeService targetClassifyTypeService;

    @Autowired
    private ITargetClassifyCheckService targetClassifyCheckService;

    @Autowired
    private ITargetClassifyCheckService checkService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void create(CreateTargetClassifyCmd cmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();

        DetectionTargetClassify targetClassify = new DetectionTargetClassify();
        BeanUtils.copyProperties(cmd, targetClassify);
        targetClassify.setStatus(StatusEnums.NORMAL.name());

        if (cmd.getQualityLab().equals(YesOrNoEnum.YES.name()) && StringUtils.isEmpty(cmd.getQualityLabOrg())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "请选择质控实验室");
        }
        save(targetClassify);

        TargetClassifyCheck targetClassifyCheck = new TargetClassifyCheck();
        targetClassifyCheck.setTargetClassifyId(targetClassify.getId());
        targetClassifyCheck.setCheckNode("提交");
        targetClassifyCheck.setOrgId(details.getOrgId());
        targetClassifyCheck.setCheckUserId(details.getId());
        targetClassifyCheck.setCheckRemark("无");
        targetClassifyCheck.setStatus(AuditStatusEnum.ToAudit.name());
        checkService.save(targetClassifyCheck);

        //关联指标信息维护
        targetClassifyTypeService.setBizTypes(targetClassify.getId(), cmd.getTargetNumber());
    }

    @Override
    public IPage<TargetClassifyDTO> listPage(TagetClassifyQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    @Override
    public TargetClassifyDTO getDetail(String id) {
        return this.convertDTO(getById(id));
    }

    @Override
    @Transactional
    public void update(UpdateTargetClassifyCmd dto) {
        DetectionTargetClassify targetClassify = getById(dto.getId());
        if(targetClassify == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "检测指标分类：" + dto.getId());
        }

        BeanCopyUtils.copyNonNullProperties(dto, targetClassify);
        updateById(targetClassify);
    }

    @Override
    @Transactional
    public void delete(String id) {

        //TODO 删除逻辑判断
        removeById(id);
    }

    @Override
    @Transactional
    public void authorization(String id) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();

        DetectionTargetClassify targetClassify = getById(id);
        if(targetClassify == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "检测指标分类：" + id);
        }
        targetClassify.setAuthorization(YesOrNoEnum.YES.name());
        updateById(targetClassify);

        TargetClassifyCheck targetClassifyCheck = new TargetClassifyCheck();
        targetClassifyCheck.setTargetClassifyId(targetClassify.getId());
        targetClassifyCheck.setCheckNode("授权维护");
        targetClassifyCheck.setOrgId(details.getOrgId());
        targetClassifyCheck.setCheckUserId(details.getId());
        targetClassifyCheck.setCheckRemark("无");
        targetClassifyCheck.setStatus(AuditStatusEnum.ToAudit.name());
        checkService.save(targetClassifyCheck);
    }

    public TargetClassifyDTO convertDTO(DetectionTargetClassify targetClassify) {
        if (targetClassify == null) {
            return null;
        }

        TargetClassifyDTO dto = new TargetClassifyDTO();
        BeanCopyUtils.copyNonNullProperties(targetClassify, dto);

        if (CollectionUtils.isNotEmpty(targetClassify.getTarget())) {
            dto.setTargetNumber(
                    targetClassify.getTarget()
                            .stream()
                            .map(DetectionTargetClassifyType::getTargetId)
                            .collect(Collectors.toList())
            );
            dto.setTargetNumberDes(new ArrayList<>());
            dto.getTargetNumber().forEach(s -> {
                DetectionTarget target = targetService.getById(s);
                if (target != null) {
                    dto.getTargetNumberDes().add(target.getName());
                }
            });
        }

        dto.setVolatilizeDes(YesOrNoEnum.valueOf(targetClassify.getVolatilize()).getValue());
        dto.setQualityLabDes(YesOrNoEnum.valueOf(targetClassify.getQualityLab()).getValue());
        if (targetClassify.getQualityLab().equals(YesOrNoEnum.YES.name())) {
            //查询检质控验室单位信息
            Organization qualityLabOrg = organizationService.getById(targetClassify.getQualityLabOrg());
            OrganizationDTO organizationDTO = new OrganizationDTO();
            BeanUtils.copyProperties(qualityLabOrg, organizationDTO);
            dto.setQualityLabOrg(organizationDTO);
        }

        Organization detectionLabOrg = organizationService.getById(targetClassify.getDetectionLabOrg());
        OrganizationDTO detectionLabOrgDTO = new OrganizationDTO();
        BeanUtils.copyProperties(detectionLabOrg, detectionLabOrgDTO);
        dto.setDetectionLabOrg(detectionLabOrgDTO);

        Organization stationingLabOrg = organizationService.getById(targetClassify.getStationingLabOrg());
        OrganizationDTO stationingLabOrgDTO = new OrganizationDTO();
        BeanUtils.copyProperties(stationingLabOrg, stationingLabOrgDTO);
        dto.setStationingLabOrg(stationingLabOrgDTO);

        //TODO 需要查询样品数量
        dto.setSampleNum(0);
        //TODO 需要查询样点数量
        dto.setSamplePointNum(0);
        dto.setTargetNum(dto.getTargetNumber().size());

        dto.setCheckList(new ArrayList<>());
        List<TargetClassifyCheck> targetClassifyChecks = targetClassifyCheckService.getBaseMapper().selectList(Wrappers.<TargetClassifyCheck>query().eq(TargetClassifyCheck.TARGET_CLASSIFY_ID, targetClassify.getId()));
        targetClassifyChecks.stream().forEach(t -> {
            TargetClassifyCheckDTO checkDTO = new TargetClassifyCheckDTO();
            BeanUtils.copyProperties(t, checkDTO);

            Organization checkOrg = organizationService.getById(t.getOrgId());
            OrganizationDTO checkOrgDTO = new OrganizationDTO();
            BeanUtils.copyProperties(checkOrg, checkOrgDTO);
            checkDTO.setCheckOrg(checkOrgDTO);
            checkDTO.setUserDTO(userService.getOrganizationAdmin(checkOrg.getId()));
            dto.getCheckList().add(checkDTO);
        });
        return dto;
    }
}
