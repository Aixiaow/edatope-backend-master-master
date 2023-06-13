package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.AuditStatusEnum;
import com.csbaic.edatope.app.enums.YesOrNoEnum;
import com.csbaic.edatope.app.mapper.DetectionCapacityMapper;
import com.csbaic.edatope.app.model.command.CreateDetectionCapacityCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.DetectionCapacityAuditBody;
import com.csbaic.edatope.app.model.query.DetectionCapacityQuery;
import com.csbaic.edatope.app.service.IDetectionCapacityCheckService;
import com.csbaic.edatope.app.service.IDetectionCapacityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.Result;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.csbaic.edatope.common.result.Result.error;
import static com.csbaic.edatope.common.result.Result.ok;

/**
 * <p>
 * 检测能力表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@Service
public class DetectionCapacityServiceImpl extends ServiceImpl<DetectionCapacityMapper, DetectionCapacity> implements IDetectionCapacityService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IDetectionCapacityCheckService detectionCapacityCheckService;

    @Override
    @Transactional
    public void create(CreateDetectionCapacityCmd cmd) {
        DetectionCapacity capacity = new DetectionCapacity();
        BeanUtils.copyProperties(cmd, capacity);
        capacity.setVolatilize(cmd.getVolatilize().name());
        capacity.setStatus(AuditStatusEnum.ToAudit.name());
        //TODO 保存逻辑调整
        save(capacity);

        DetectionCapacityCheck capacityCheck = new DetectionCapacityCheck();
        capacityCheck.setCapacityId(capacity.getId());
        capacityCheck.setCheckNode("提交");
        capacityCheck.setOrgId(cmd.getOrgId());
        Organization organization = organizationService.getById(cmd.getOrgId());
        UserDTO organizationAdmin = userService.getOrganizationAdmin(organization.getId());
        if (!Objects.isNull(organizationAdmin)) {
            capacityCheck.setCheckUserId(organizationAdmin.getId());
        }
        capacityCheck.setCheckRemark("无");
        capacityCheck.setStatus(AuditStatusEnum.ToAudit.name());
        detectionCapacityCheckService.save(capacityCheck);
    }

    @Override
    public IPage<DetectionCapacityDTO> listPage(DetectionCapacityQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    @Override
    public DetectionCapacityDTO getCapacityDetail(String id) {
        return this.convertDTO(getById(id));
    }

    @Override
    @Transactional
    public Result pass(DetectionCapacityAuditBody body) {
        DetectionCapacity byId = getById(body.getId());
        if (Objects.isNull(byId)) {
            return error("审核内容不存在");
        }
        if (body.getType() == 1) {
            byId.setStatus(AuditStatusEnum.AlreadyPassed.name());
        } else {
            byId.setStatus(AuditStatusEnum.NotPass.name());
            if (Objects.isNull(body)) {
                return error("请填写【退回】原因，限200个字以内");
            }
        }
        updateById(byId);

        DetectionCapacityCheck capacityCheck = new DetectionCapacityCheck();
        capacityCheck.setCapacityId(byId.getId());
        capacityCheck.setCheckNode("审核");
        capacityCheck.setOrgId(byId.getOrgId());
        Organization organization = organizationService.getById(byId.getOrgId());
        capacityCheck.setCheckUserId(userService.getOrganizationAdmin(organization.getId()).getId());
        if (body.getType() == 1) {
            capacityCheck.setCheckRemark("无");
            capacityCheck.setStatus(AuditStatusEnum.AlreadyPassed.name());
        } else {
            capacityCheck.setCheckRemark(body.getBackCause());
            capacityCheck.setStatus(AuditStatusEnum.NotPass.name());
        }
        detectionCapacityCheckService.save(capacityCheck);
        return ok();
    }

    @Override
    @Transactional
    public void update(DetectionCapacityDTO dto) {
        DetectionCapacity capacity = getById(dto.getId());
        if(capacity == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到阶段：" + dto.getId());
        }

        BeanCopyUtils.copyNonNullProperties(dto, capacity);
        capacity.setVolatilize(dto.getVolatilize());
        updateById(capacity);

        if (capacity.getStatus().equals(AuditStatusEnum.NotPass.name())) {
            DetectionCapacityCheck capacityCheck = new DetectionCapacityCheck();
            capacityCheck.setCapacityId(capacity.getId());
            capacityCheck.setCheckNode("提交");
            capacityCheck.setOrgId(capacity.getOrgId());
            Organization organization = organizationService.getById(capacity.getOrgId());
            capacityCheck.setCheckUserId(userService.getOrganizationAdmin(organization.getId()).getId());
            capacityCheck.setCheckRemark("无");
            capacityCheck.setStatus(AuditStatusEnum.ToAudit.name());
            detectionCapacityCheckService.save(capacityCheck);
        }
    }

    @Override
    @Transactional
    public void delete(String id) {

        //TODO 删除逻辑判断
        removeById(id);
    }

    public DetectionCapacityDTO convertDTO(DetectionCapacity capacity) {
        if (capacity == null) {
            return null;
        }

        DetectionCapacityDTO dto = new DetectionCapacityDTO();
        BeanCopyUtils.copyNonNullProperties(capacity, dto);

        Organization organization = organizationService.getById(capacity.getOrgId());
        OrganizationDTO organizationDTO = new OrganizationDTO();
        BeanUtils.copyProperties(organization, organizationDTO);
        dto.setOrganizationDTO(organizationDTO);

        Organization laboratoryOrg = organizationService.getById(capacity.getLaboratory());
        OrganizationDTO laboratoryOrgDTO = new OrganizationDTO();
        BeanUtils.copyProperties(laboratoryOrg, laboratoryOrgDTO);
        dto.setLaboratoryOrg(laboratoryOrgDTO);

        dto.setStatusDes(AuditStatusEnum.valueOf(capacity.getStatus()).getValue());
        dto.setVolatilizeDesc(YesOrNoEnum.valueOf(capacity.getVolatilize()).getValue());

        List<DetectionCapacityCheckDTO> capacityCheckDTOList = new ArrayList<>();
        List<DetectionCapacityCheck> detectionCapacityChecks = detectionCapacityCheckService.getBaseMapper().selectList(Wrappers.<DetectionCapacityCheck>query().eq(DetectionCapacityCheck.CAPACITY_Id, capacity.getId()));
        detectionCapacityChecks.stream().forEach(t -> {
            DetectionCapacityCheckDTO checkDTO = new DetectionCapacityCheckDTO();
            BeanUtils.copyProperties(t, checkDTO);

            Organization checkOrg = organizationService.getById(t.getOrgId());
            OrganizationDTO checkOrgDTO = new OrganizationDTO();
            BeanUtils.copyProperties(checkOrg, checkOrgDTO);
            checkDTO.setCheckOrg(checkOrgDTO);
            checkDTO.setUserDTO(userService.getOrganizationAdmin(checkOrg.getId()));
            capacityCheckDTOList.add(checkDTO);
        });
        dto.setCheckList(capacityCheckDTOList);
        return dto;
    }
}
