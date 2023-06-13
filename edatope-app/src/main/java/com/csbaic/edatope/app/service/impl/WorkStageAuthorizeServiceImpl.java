package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.WorkStageStatusEnums;
import com.csbaic.edatope.app.mapper.WorkStageAuthorizeMapper;
import com.csbaic.edatope.app.model.command.CreateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.command.UpdateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.WorkStageAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.WorkStageAuthorizeQuery;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.utils.UserUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.common.utils.DateTimeUtils;
import com.csbaic.edatope.common.utils.StringSplitUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作阶段授权表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
@Service
public class WorkStageAuthorizeServiceImpl extends ServiceImpl<WorkStageAuthorizeMapper, WorkStageAuthorize> implements IWorkStageAuthorizeService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IWorkStageService workStageService;

    @Autowired
    private IWorkStageAuthorizeTaskService authorizeTaskService;

    @Autowired
    private IDictService dictService;

    @Override
    @Transactional
    public void create(CreateWorkStageAuthorizeCmd cmd) {
        List<String> orgId = cmd.getOrgId();
        for (String str : orgId) {
            WorkStageAuthorize workStageAuthorize = new WorkStageAuthorize();
            workStageAuthorize.setExpireTime(DateTimeUtils.stringToDate(cmd.getExpireTime() + " 23:59:59"));
            workStageAuthorize.setOrgId(str);
            workStageAuthorize.setStatus(WorkStageStatusEnums.ALREADY.name());
            save(workStageAuthorize);

            cmd.getStageList().stream().forEach(t ->{
                WorkStageAuthorizeTask task = new WorkStageAuthorizeTask();
                task.setStageAuthorizeId(workStageAuthorize.getId());
                task.setStageId(t.getWorkStageId());
                task.setBizType(StringSplitUtils.join(",", t.getBizType()));
                authorizeTaskService.save(task);
            });
        }
    }

    @Override
    public IPage<WorkStageAuthorizeDTO> listPage(WorkStageAuthorizeQuery query) {
        if (query.isExpire()) {
            Date dateAddDay = DateTimeUtils.getDateAddDay(new Date(), 30);
            query.setAboutTime(dateAddDay);
        }
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    @Override
    public WorkStageAuthorizeDTO getAuthorizeDetail(String id) {
        WorkStageAuthorize workStageAuthorize = getById(id);
        if(workStageAuthorize == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到阶段：" + id);
        }
        return this.convertDTO(workStageAuthorize);
    }

    @Override
    @Transactional
    public void update(UpdateWorkStageAuthorizeCmd cmd) {
        WorkStageAuthorize workStageAuthorize = getById(cmd.getId());
        if(workStageAuthorize == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到授权信息：" + cmd.getId());
        }
        BeanCopyUtils.copyNonNullProperties(cmd, workStageAuthorize);
        workStageAuthorize.setExpireTime(DateTimeUtils.stringToDate(cmd.getExpireTime() + " 23:59:59"));
        updateById(workStageAuthorize);

        if (cmd.getStageList().size() > 0) {
            authorizeTaskService.remove(
                    Wrappers.<WorkStageAuthorizeTask>query().eq(WorkStageAuthorizeTask.STAGE_AUTHORIZE_ID, cmd.getId())
            );
            cmd.getStageList().stream().forEach(t -> {
                WorkStageAuthorizeTask task = new WorkStageAuthorizeTask();
                task.setStageAuthorizeId(workStageAuthorize.getId());
                task.setStageId(t.getWorkStageId());
                task.setBizType(StringSplitUtils.join(",", t.getBizType()));
                authorizeTaskService.save(task);
            });
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        removeById(id);
        authorizeTaskService.remove(
                Wrappers.<WorkStageAuthorizeTask>query().eq(WorkStageAuthorizeTask.STAGE_AUTHORIZE_ID,id)
        );
    }

    public WorkStageAuthorizeDTO convertDTO(WorkStageAuthorize authorize) {
        if (authorize == null) {
            return null;
        }
        WorkStageAuthorizeDTO dto = new WorkStageAuthorizeDTO();
        BeanCopyUtils.copyNonNullProperties(authorize, dto);

        Organization organization = organizationService.getById(authorize.getOrgId());
        OrganizationDTO organizationDTO = new OrganizationDTO();
        BeanUtils.copyProperties(organization, organizationDTO);
        dto.setOrganization(organizationDTO);
        dto.setAdmin(userService.getOrganizationAdmin(organization.getId()));

        int days = DateTimeUtils.differentDaysByMillisecond(new Date(), authorize.getExpireTime());
        if (days < 0) {
            days = 0;
        }
        dto.setResidueDay(days + 1);

        List<WorkStageAuthorizeTask> workStageAuthorizeTasks = authorizeTaskService.getBaseMapper().selectList(
                Wrappers.<WorkStageAuthorizeTask>query().eq(WorkStageAuthorizeTask.STAGE_AUTHORIZE_ID, authorize.getId())
        );

        List<WorkStageAuthorizeDTO.StageList> stageList = new ArrayList<>();
        workStageAuthorizeTasks.stream().forEach(t ->{
            WorkStageAuthorizeDTO.StageList workStageList = new WorkStageAuthorizeDTO.StageList();

            workStageList.setWorkStageId(t.getStageId());
            WorkStage workStage = workStageService.getById(t.getStageId());
            if (!Objects.isNull(workStage)) {
                workStageList.setWorkStageName(workStage.getName());
            }

            Set<String> strings = StringSplitUtils.splitToSet(",", t.getBizType());
            List<String> bizType = new ArrayList<>(strings);
            workStageList.setBizType(bizType);
            workStageList.setBizTypeDesc(new ArrayList<>());

            Map<String, DictDTO> dictMap = dictService.getDictValueMap("StageTask");
            workStageList.getBizType().forEach(s -> {
                DictDTO dictDTO = dictMap.get(s);
                if (dictDTO != null) {
                    workStageList.getBizTypeDesc().add(dictDTO.getName());
                }
            });
            stageList.add(workStageList);
        });

        dto.setStageList(stageList);
        return dto;
    }
}
