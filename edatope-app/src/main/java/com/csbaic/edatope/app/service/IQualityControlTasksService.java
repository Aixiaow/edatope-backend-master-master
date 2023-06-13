package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.QualityControlTasks;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.FeedbackCmd;
import com.csbaic.edatope.app.model.command.QualityControlBackCmd;
import com.csbaic.edatope.app.model.command.QualityControlSpecialistTaskCmd;
import com.csbaic.edatope.app.model.command.QualityControlTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 布点质控任务 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
public interface IQualityControlTasksService extends IService<QualityControlTasks> {

    /**
     * 根据专家组和状态查询数量
     *
     * @param status
     * @return
     */
    Long getCountByStatus(String qualitySpecialistId, String... status);

    /**
     * 布点质控单位任务-分配单位任务
     *
     * @param qualityControlTaskCmd
     */
    void create(QualityControlTaskCmd qualityControlTaskCmd);

    /**
     * 布点质控单位任务-撤回单位任务
     *
     * @param qualityControlTasksId
     */
    void delete(String qualityControlTasksId);


    /**
     * 布点质控单位任务-列表分页
     *
     * @param query
     * @return
     */
    IPage<QualityControlTaskResultVO> qualityControlTaskPage(QualityControlTasksQuery query);

    /**
     * 布点质控专家组任务-分配任务
     *
     * @param qualityControlSpecialistTaskCmd
     */
    void specialistCreate(QualityControlSpecialistTaskCmd qualityControlSpecialistTaskCmd);

    /**
     * 布点质控专家组任务-撤回专家组任务
     *
     * @param qualityControlTasksId
     */
    void specialistDelete(String qualityControlTasksId);

    /**
     * 布点质控专家组任务-列表分页
     *
     * @param query
     * @return
     */
    IPage<QualityControlSpecialistTaskResultVO> specialistPage(QualityControlTasksQuery query);

    IPage<QualityControlSpecialistTaskResultVO> feedbackPage(QualityControlTasksQuery query);

    IPage<QualityControlSpecialistTaskResultVO> sendBackPage(QualityControlTasksQuery query);

    void sendBack(String qualityControlTaskId);

    void backOrPass(FeedbackCmd query);

    void collectSubmit(FeedbackCmd query);

    OpinionSummaryVO look(String qualityControlTasksId);

    /**
     * 布点质控单位任务-选择布点质控单位-分页
     *
     * @return
     */
    IPage<QualityControlOrgResultVO> orgList(QualityControlOrgList qualityControlOrgList);

    /**
     * 布点质控单位任务-撤回单位任务-布点质控单位信息
     *
     * @return
     */
    QualityControlOrgResultVO orgInfo(String qualityControlTasksId);

    /**
     * @return
     */
    IPage<SpecialistResultVO> pointSpecialistTaskPage(PointSpecialistPageQuery pointSpecialistPageQuery);

    /**
     * 布点质控专家组任务-已选布点质控专家组信息
     * @param qualityControlTasksId
     * @return
     */
    SpecialistResultVO selectSpecialist(String qualityControlTasksId);

    /**
     * 布点质控专家组任务-待分配布点质控任务
     *
     * @param query
     * @return
     */
    List<QualityControlWorkStageVO> waitQualityControlTaskList(WaitQualityControlTaskQuery query);


    /**
     * 采样过程点位退回-列表分页
     *
     * @param query
     * @return
     */
    IPage<QualityControlSpecialistTaskResultVO> backPage(QualityControlTasksQuery query);

    /**
     * 采样过程点位退回-授权调整
     *
     * @param cmd
     */
    void pointBackCreate(QualityControlBackCmd cmd);
}
