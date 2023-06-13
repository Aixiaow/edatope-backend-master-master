package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.Block;
import com.csbaic.edatope.app.entity.SurveyTasksRecord;
import com.csbaic.edatope.app.entity.WorkStageAuthorizeTask;
import com.csbaic.edatope.app.enums.SurveyTaskRecordTypeEnum;
import com.csbaic.edatope.app.mapper.SurveyTasksRecordMapper;
import com.csbaic.edatope.app.model.dto.TasksRecordDto;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.csbaic.edatope.app.service.ISurveyTasksRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 调查任务分配操作记录 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
@Service
public class SurveyTasksRecordServiceImpl extends ServiceImpl<SurveyTasksRecordMapper, SurveyTasksRecord> implements ISurveyTasksRecordService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrganizationService organizationService;

    @Override
    public List<TasksRecordDto> getList(String blockWorkStageId) {
        List<TasksRecordDto> list = new ArrayList<>();
        List<SurveyTasksRecord> record = getBaseMapper().selectList(Wrappers.<SurveyTasksRecord>query().eq(SurveyTasksRecord.BLOCK_WORK_STAGE_ID, blockWorkStageId).eq(SurveyTasksRecord.DELETED, 0).orderByDesc(SurveyTasksRecord.CREATE_TIME));
        record.stream().forEach(t ->{
            TasksRecordDto dto = new TasksRecordDto();
            BeanCopyUtils.copyProperties(t, dto);

            dto.setOrg(organizationService.getDetail(t.getOrgId()));
            dto.setBusinessOrg(organizationService.getDetail(t.getBusinessOrg()));
            dto.setUser(userService.getUserDetail(t.getUserId()));
            dto.setStatusDesc(SurveyTaskRecordTypeEnum.valueOf(t.getStatus()).getValue());
            list.add(dto);
        });
        return list;
    }
}
