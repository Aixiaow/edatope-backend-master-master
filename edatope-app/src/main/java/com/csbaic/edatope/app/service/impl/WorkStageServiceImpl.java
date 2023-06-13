package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.OrganizationStatus;
import com.csbaic.edatope.app.enums.StatusEnums;
import com.csbaic.edatope.app.mapper.WorkStageMapper;
import com.csbaic.edatope.app.model.command.CreateStageCmd;
import com.csbaic.edatope.app.model.command.DeleteOrganizationCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.WorkStageQuery;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作阶段表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
@Service
public class WorkStageServiceImpl extends ServiceImpl<WorkStageMapper, WorkStage> implements IWorkStageService {

    @Autowired
    private IWorkStageTypeService workStageTypeService;

    @Autowired
    private IDictService dictService;

    @Autowired
    @Lazy(true)
    private IBlockWorkStageService blockWorkStageService;

    @Autowired
    @Lazy(true)
    private IWorkStageAuthorizeService workStageAuthorizeService;

    @Autowired
    private IWorkStageAuthorizeTaskService workStageAuthorizeTaskService;

    @Override
    @Transactional
    public void create(CreateStageCmd cmd) {
        WorkStage exists = getOne(Wrappers.<WorkStage>query().eq(WorkStage.NAME, cmd.getName()));
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "工作阶段：" + cmd.getName() + " 已存在");
        }
        WorkStage workStage = new WorkStage();
        BeanUtils.copyProperties(cmd, workStage);
        workStage.setStatus(StatusEnums.NORMAL.name());
        save(workStage);

        workStageTypeService.setBizTypes(workStage.getId(), cmd.getBizType());
    }

    @Override
    public IPage<WorkStageDTO> findListPage(WorkStageQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(this::convertDTO);
    }

    @Override
    public List<WorkStageDTO> findAll() {
        WorkStageQuery query = new WorkStageQuery();
        query.setStatus(StatusEnums.NORMAL.name());
        return getBaseMapper().listAll(query).stream().map(this::convertDTO).collect(Collectors.toList());
    }

    @Override
    public WorkStageDTO getWorkStageDetail(String id) {
        WorkStage workStage = getById(id);
        List<WorkStageType> workStageTypes = workStageTypeService.getBaseMapper().selectList(Wrappers.<WorkStageType>query().eq(WorkStageType.STAGE_ID, workStage.getId()));
        workStage.setBizType(workStageTypes);
        if(workStage == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到阶段：" + id);
        }
        return this.convertDTO(workStage);
    }

    @Override
    @Transactional
    public void update(WorkStageDTO dto) {
        WorkStage workStage = getById(dto.getId());
        if(workStage == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到阶段：" + dto.getId());
        }

        BeanCopyUtils.copyNonNullProperties(dto, workStage);
        updateById(workStage);

        workStageTypeService.setBizTypes(workStage.getId(), dto.getBizType());
    }

    @Override
    @Transactional
    public void delete(String id) {
        WorkStage workStage = getById(id);
        if(workStage == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到阶段：" + id);
        }
        //停用删除和地块绑定有逻辑处理 工作阶段【XXXXXX】已在【X】个地块下添加过，无法删除！
        List<BlockWorkStage> blockWorkStageList = blockWorkStageService.list(Wrappers.<BlockWorkStage>query().eq(BlockWorkStage.WORK_STAGE_ID, id));
        if (blockWorkStageList.size() > 0) {
            throw BizRuntimeException.from(ResultCode.ERROR, "工作阶段：【" + workStage.getName() + "】已在【" + blockWorkStageList.size() + "个地块下添加过，无法删除！");
        }

        // 删除授权信息
        List<WorkStageAuthorizeTask> workStageAuthorizeTasks = workStageAuthorizeTaskService.list(Wrappers.<WorkStageAuthorizeTask>query().eq(WorkStageAuthorizeTask.STAGE_ID, workStage.getId()));
        workStageAuthorizeTasks.stream().forEach(t ->{
            workStageAuthorizeService.delete(t.getStageAuthorizeId());
        });

        removeById(id);
        workStageTypeService.removeBizTypes(id);
    }


    public WorkStageDTO convertDTO(WorkStage workStage) {
        if (workStage == null) {
            return null;
        }

        WorkStageDTO dto = new WorkStageDTO();
        BeanCopyUtils.copyNonNullProperties(workStage, dto);

        if (CollectionUtils.isNotEmpty(workStage.getBizType())) {
            dto.setBizType(
                    workStage.getBizType()
                            .stream()
                            .map(WorkStageType::getBizType)
                            .collect(Collectors.toList())
            );
            dto.setBizTypeDesc(new ArrayList<>());

            Map<String, DictDTO> dictMap = dictService.getDictValueMap("StageTask");
            dto.getBizType().forEach(s -> {
                DictDTO dictDTO = dictMap.get(s);
                if (dictDTO != null) {
                    dto.getBizTypeDesc().add(dictDTO.getName());
                }
            });
        }
        return dto;
    }
}
