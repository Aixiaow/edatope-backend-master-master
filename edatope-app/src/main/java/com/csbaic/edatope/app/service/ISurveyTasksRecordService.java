package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.SurveyTasksRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.dto.TasksRecordDto;

import java.util.List;

/**
 * <p>
 * 调查任务分配操作记录 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
public interface ISurveyTasksRecordService extends IService<SurveyTasksRecord> {

    List<TasksRecordDto> getList(String blockWorkStageId);

}
