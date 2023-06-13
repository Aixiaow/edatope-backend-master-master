package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.BlockWorkStateStatusEnum;
import com.csbaic.edatope.app.enums.WorkStageStatusEnums;
import com.csbaic.edatope.app.mapper.BlockWorkStageMapper;
import com.csbaic.edatope.app.model.command.CreateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.UpdateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.dto.BlockWorkStageDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 地块工作阶段 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-03
 */
@Service
public class BlockWorkStageServiceImpl extends ServiceImpl<BlockWorkStageMapper, BlockWorkStage> implements IBlockWorkStageService {

    @Autowired
    private IWorkStageAuthorizeService authorizeService;
    @Autowired
    private IWorkStageAuthorizeTaskService authorizeTaskService;
    @Autowired
    private IWorkStageService workStageService;
    @Autowired
    private IBlockService blockService;


    @Override
    public void create(CreateBlockWorkStageCmd cmd) {
        Block block = blockService.getById(cmd.getBlockId());
        if (block == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块");
        }
        WorkStage workStage = workStageService.getById(cmd.getWorkStageId());
        if (workStage == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到工作阶段");
        }

        BlockWorkStage blockWorkStage = new BlockWorkStage();
        BeanCopyUtils.copyNonNullProperties(cmd, blockWorkStage);
        blockWorkStage.setStatus(BlockWorkStateStatusEnum.NOT_START.toString());
        save(blockWorkStage);
    }

    @Override
    public void update(UpdateBlockWorkStageCmd cmd) {
        BlockWorkStage blockWorkStage = getById(cmd.getId());
        if (blockWorkStage == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到工作阶段");
        }

        BeanCopyUtils.copyNonNullProperties(cmd, blockWorkStage);
        updateById(blockWorkStage);
    }

    @Override
    public List<WorkStageDTO> listWorkState(String orgId) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isBlank(orgId)) {
            orgId = details.getOrgId();
        }

        List<WorkStageAuthorize> authorizes = authorizeService.list(
                Wrappers.<WorkStageAuthorize>query().eq(WorkStageAuthorize.ORG_ID, orgId)
        );

        //没有授权的任务
        if (CollectionUtils.isEmpty(authorizes)) {
            return new ArrayList<>();
        }

        //查询阶段
        List<String> stageAuthorizeIds = authorizes
                .stream()
                .filter(workStage -> Objects.equals(WorkStageStatusEnums.ALREADY.toString(), workStage.getStatus()))
                .filter(workStageAuthorize -> workStageAuthorize.getExpireTime().getTime() > System.currentTimeMillis())
                .map(WorkStageAuthorize::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(stageAuthorizeIds)) {
            return new ArrayList<>();
        }

        List<WorkStageAuthorizeTask> workStageAuthorizeTasks = authorizeTaskService.list(
                Wrappers.<WorkStageAuthorizeTask>query().in(WorkStageAuthorizeTask.STAGE_AUTHORIZE_ID, stageAuthorizeIds)
        );

        Set<String> stageIds = workStageAuthorizeTasks.stream()
                .map(WorkStageAuthorizeTask::getStageId)
                .collect(Collectors.toSet());

        List<WorkStage> stages = workStageService.listByIds(stageIds);
        return stages.stream()
                .map(workStage -> {
                    WorkStageDTO workStageDTO = new WorkStageDTO();
                    BeanCopyUtils.copyNonNullProperties(workStage, workStageDTO);
                    return workStageDTO;
                }).collect(Collectors.toList());
    }


    @Override
    public List<BlockWorkStageDTO> listBlockWordStage(String blockId) {
        return list(
                Wrappers.<BlockWorkStage>query().eq(BlockWorkStage.BLOCK_ID, blockId).orderByAsc(BlockWorkStage.CREATE_TIME)
        ).stream().map(blockWorkStage -> {
            WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
            BlockWorkStageDTO blockWorkStageDTO = new BlockWorkStageDTO();
            BeanCopyUtils.copyNonNullProperties(blockWorkStage, blockWorkStageDTO);
            blockWorkStageDTO.setWorkStageName(workStage.getName());
            return blockWorkStageDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(DeleteBlockWorkStageCmd deleteBlockCmd) {
        BlockWorkStage blockWorkStage = getById(deleteBlockCmd.getId());
        if (blockWorkStage == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块工作任务");
        }
        removeById(blockWorkStage);
    }

    @Override
    public BlockWorkStageDTO getBlockWorkStage(String id) {
        BlockWorkStage blockWorkStage = getById(id);
        if (blockWorkStage == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块工作任务");
        }
        BlockWorkStageDTO workStageDTO = new BlockWorkStageDTO();
        BeanCopyUtils.copyNonNullProperties(blockWorkStage, workStageDTO);
        return workStageDTO;
    }
}
