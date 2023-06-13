package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.SurveyTasks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockWorkStageCmd;
import com.csbaic.edatope.app.model.command.DeleteSurveyTasksCmd;
import com.csbaic.edatope.app.model.command.SurveyTasksCmd;
import com.csbaic.edatope.app.model.dto.BlockWorkStageQueryResultVO;
import com.csbaic.edatope.app.model.dto.SurveyTasksAllotRecordDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationUserDTO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.OrgListAllQuery;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 调查任务分配 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
public interface ISurveyTasksService extends IService<SurveyTasks> {

    /**
     * 分配调查任务
     *
     * @param cmd
     */
    void create(SurveyTasksCmd cmd);

    /**
     * 撤回
     */
    void delete(DeleteSurveyTasksCmd cmd);

    SurveyTasksAllotRecordDTO listRecord(String id);

    SurveyTasksAllotRecordDTO get(String id);

    IPage<BlockWorkStageQueryResultVO> listPage(BlockQuery query);

    List<TechOrganizationUserDTO> listOrgAll(OrgListAllQuery query);

    SurveyTasks getByBlockWorkStageIdAndType(String blockWorkStageId, String type);
}
