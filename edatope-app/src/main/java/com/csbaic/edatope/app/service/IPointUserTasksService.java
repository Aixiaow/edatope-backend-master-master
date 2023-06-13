package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.PointUserTasks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.PointUserTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.UserListQuery;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 布点人员任务分配 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-04-26
 */
public interface IPointUserTasksService extends IService<PointUserTasks> {

    /**
     * 分配布点任务
     *
     * @param pointUserTasCmd
     */
    void create(PointUserTaskCmd pointUserTasCmd);

    /**
     * 布点人员任务分配列表-分页
     *
     * @param query
     * @return
     */
    IPage<PointUserTasksResultVO> listPage(PointUserBlockQuery query);

    /**
     * 选择布点人员列表-分页
     *
     * @return
     */
    IPage<UserSelectResultVO> userList(UserListQuery userListQuery);

    /**
     * 查询布点人员信息
     * @param blockWorkStageId
     * @return
     */
    UserSelectResultVO userInfo(String blockWorkStageId);

    /**
     * 待分配和已分配阶段任务列表
     *
     * @param workStageIdList
     * @return
     */
    List<PointWorkStageDistributeVO> distributionList(List<String> workStageIdList);

    /**
     * 地块布点方案查询-分页
     *
     * @param query
     * @return
     */
    IPage<PointBlockTasksResultVo> pointBlockTaskPage(PointUserBlockQuery query);

    /**
     * 布点方案数据维护-待提交布点方案列表
     *
     * @param workStageIdList
     * @return
     */
    List<SubmitPointTaskVO> pointBlockTaskDefendSubmit(List<String> workStageIdList);

    /**
     * 布点方案数据维护-待提交布点方案列表
     *
     * @param blockWorkStageId
     * @return
     */
    ResultPointTaskVO pointBlockTaskDefendView(String blockWorkStageId);

    /**
     * 布点方案数据整改-列表分页
     * @param query
     * @return
     */
    IPage<QualityControlSpecialistTaskResultVO> pointBlockTaskReformPage(PointUserBlockQuery query);
}
