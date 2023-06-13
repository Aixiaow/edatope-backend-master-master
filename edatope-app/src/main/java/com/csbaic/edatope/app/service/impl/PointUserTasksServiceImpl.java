package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.SurveyTaskStatusEnum;
import com.csbaic.edatope.app.enums.TaskPlanColorEnum;
import com.csbaic.edatope.app.enums.UploadPlanTypeEnum;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.mapper.PointUserTasksMapper;
import com.csbaic.edatope.app.model.command.PointUserTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.UserListQuery;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.app.utils.UserUtils;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.service.IAppFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 布点人员任务分配 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-04-26
 */
@Service
public class PointUserTasksServiceImpl extends ServiceImpl<PointUserTasksMapper, PointUserTasks> implements IPointUserTasksService {

    @Autowired
    private IBlockService blockService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private ITechOrganizationAuthorizeService techOrganizationAuthorizeService;

    @Autowired
    private ITechOrganizationAuthorizeCityService techOrganizationAuthorizeCityService;

    @Autowired
    private ISurveyTasksService surveyTasksService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBlockWorkStageService blockWorkStageService;

    @Autowired
    private IEnterpriseService enterpriseService;

    @Autowired
    private IWorkStageService workStageService;

    @Autowired
    @Lazy
    private IPointService pointService;

    @Autowired
    private IPointFileService pointFileService;

    @Autowired
    private IPointTasksRecordService pointTasksRecordService;
    @Autowired
    private IProjectService projectService;

    @Autowired
    private IQualityControlTasksService qualityControlTasksService;

    @Autowired
    private IAuditOpinionService auditOpinionService;

    @Autowired
    private IAppFileService fileService;

    /**
     * 未完成状态字典
     */
    private static final List<String> unCompleteStatelist = Arrays.asList("D018-001", "D018-002", "D018-003", "D018-004",
            "D018-005", "D018-008", "D018-011");

    /**
     * 布点人员角色id
     */
    private static final String pointUserRoleId = "1519198242331099137";

    /**
     * 布点方案数据整改-待整改状态
     */
    private static final List<String> pointReform = Arrays.asList(PointStatusEnum.back_perfect.getValue(),
            PointStatusEnum.back_retrial.getValue());

    /**
     * 分配布点任务
     *
     * @param pointUserTasCmd
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(PointUserTaskCmd pointUserTasCmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        for (String workStage : pointUserTasCmd.getBlockWorkStageId()) {
            BlockWorkStage blockWorkStage = blockWorkStageService.getById(workStage);
            if (Objects.isNull(blockWorkStage)) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块工作阶段信息");
            }

            PointUserTasks pointUserTasks = new PointUserTasks();

            // 查询该任务阶段下是否已分配布点人员
            PointUserTasks userTasks = getBaseMapper().selectOne(Wrappers
                    .<PointUserTasks>query()
                    .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStage));
            if (userTasks != null) {
                if (userTasks.getStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "该任务阶段已分配布点人员任务");
                } else {
                    pointUserTasks.setId(userTasks.getId());
                }
            }

            // 查询该任务阶段的调查任务分配信息
            SurveyTasks surveyTasks = surveyTasksService.getOne(Wrappers.<SurveyTasks>query()
                    .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStage)
                    .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                    .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));

            pointUserTasks.setBlockId(blockWorkStage.getBlockId());
            pointUserTasks.setBlockWorkStageId(workStage);
            pointUserTasks.setUserId(pointUserTasCmd.getUserId());
            pointUserTasks.setStatus(SurveyTaskStatusEnum.ALLOCATED.name());
            pointUserTasks.setDeployPointStatus(PointStatusEnum.untreated.getValue());
            Optional.ofNullable(surveyTasks)
                    .ifPresent(a -> pointUserTasks.setDeadline(a.getDeadline()));
            if (pointUserTasks.getId() != null) {
                updateById(pointUserTasks);
            } else {
                save(pointUserTasks);
            }

            // 记录操作记录
            Organization organization = organizationService.getById(details.getOrgId());
            pointTasksRecordService.save(new PointTasksRecord(workStage, details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    OperateItemsEnum.DISTRIBUTE_POINT_USER_TASK.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()), null, null, null
            ));
        }
    }

    /**
     * 布点人员任务分配列表-分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<PointUserTasksResultVO> listPage(PointUserBlockQuery query) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        query.setOrgId(details.getOrgId());
        IPage<BlockVO> page = blockService.pointUserListPage(query);
        return page.convert(blockVO -> {
            PointUserTasksResultVO resultVO = new PointUserTasksResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);

            // 地块管理单位
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
                // 技术管理单位
                List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(resultVO.getProject().getOrgId(),
                        blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
                if (orgIdList.size() > 0) {
                    resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
                }
            }

            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            AtomicReference<Integer> workStageSize = new AtomicReference<>(0);
            Optional.ofNullable(workStageList).ifPresent(a -> {
                workStageSize.set(a.size());
            });
            // 已分配数量
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_ID, blockVO.getId())
                            .eq(PointUserTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));
            yfpCount = yfpCount == null ? 0 : yfpCount;
            resultVO.setTaskPlan(yfpCount + "/" + workStageSize.get());
            // 设置分配进度颜色
            if (yfpCount == 0) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
            } else if (yfpCount < workStageSize.get()) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
            } else {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.GREEN.name());
            }
            List<PointUserWorkStageDTO> pointUserWorkStageList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 查询该任务阶段的调查任务分配信息
                    SurveyTasks surveyTasks = surveyTasksService.getOne(Wrappers.<SurveyTasks>query()
                            .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                            .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));
                    if (surveyTasks == null || !SurveyTaskStatusEnum.ALLOCATED.name().equals(surveyTasks.getStatus())) {
                        continue;
                    }

                    PointUserWorkStageDTO userWorkStageDTO = new PointUserWorkStageDTO();
                    BeanCopyUtils.copyNonNullProperties(workStageDTO, userWorkStageDTO);

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                            .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    userWorkStageDTO.setTasksList(surveyTaskList.stream().map(task -> {
                        TasksDTO tasksDTO = new TasksDTO();
                        BeanCopyUtils.copyNonNullProperties(task, tasksDTO);
                        if (StringUtils.isNotEmpty(task.getOrgId())) {
                            tasksDTO.setOrgId(organizationService.getDetail(task.getOrgId()));
                        }
                        if (StringUtils.isNotEmpty(task.getPrincipal())) {
                            UserDTO userDetail = userService.getUserDetail(task.getPrincipal());
                            Optional.ofNullable(userDetail).ifPresent(a -> {
                                tasksDTO.setPrincipal(userDetail.getId());
                                tasksDTO.setPrincipalName(userDetail.getUsername());
                                tasksDTO.setPrincipalPhone(userDetail.getMobile());
                            });
                        }
                        return tasksDTO;
                    }).collect(Collectors.toList()));

                    Optional.ofNullable(surveyTasks)
                            .ifPresent(a -> userWorkStageDTO.setTaskDeadline(a.getDeadline()));
                    // 查询该任务阶段的布点人员分配信息
                    PointUserTasks pointUserTasks = baseMapper.selectOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));
                    Optional<PointUserTasks> pointUserTasksOptional = Optional.ofNullable(pointUserTasks);
                    if (pointUserTasksOptional.isPresent()) {
                        PointUserTasks userTasks = pointUserTasksOptional.get();
                        if (!StringUtils.isEmpty(userTasks.getUserId())) {
                            UserDTO userDetail = userService.getUserDetail(userTasks.getUserId());
                            userWorkStageDTO.setUserName(userDetail.getUsername());
                        }
                        userWorkStageDTO.setUserId(userTasks.getUserId());
                        userWorkStageDTO.setStatus(userTasks.getStatus());
                        userWorkStageDTO.setStatusDesc(SurveyTaskStatusEnum.getValueByName(userTasks.getStatus()));
                    } else {
                        userWorkStageDTO.setStatus(SurveyTaskStatusEnum.NOT_ALLOT.name());
                        userWorkStageDTO.setStatusDesc(SurveyTaskStatusEnum.NOT_ALLOT.getValue());
                    }
                    // 过滤状态
                    if (StringUtils.isNotEmpty(query.getStatus())) {
                        if (query.getStatus().equals(PointQueryStatusEnum.WAIT_DISTRIBUTE.name())) {
                            if (userWorkStageDTO.getStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                                continue;
                            }
                        } else if (query.getStatus().equals(PointQueryStatusEnum.ALREADY_DISTRIBUTE.name())) {
                            if (!userWorkStageDTO.getStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                                continue;
                            }
                        }
                    }
                    pointUserWorkStageList.add(userWorkStageDTO);
                }
            }
            resultVO.setWorkStageList(pointUserWorkStageList);
            return resultVO;
        });
    }

    /**
     * 选择布点人员列表-分页
     *
     * @return
     */
    @Override
    public IPage<UserSelectResultVO> userList(UserListQuery query) {
        UserQuery userQuery = new UserQuery();
        BeanCopyUtils.copyNonNullProperties(query, userQuery);
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        userQuery.setOrgIds(new ArrayList<String>() {{
            details.getOrgId();
        }});
        userQuery.setRoles(new ArrayList<String>() {{
            add(pointUserRoleId);
        }});
        IPage<UserDTO> userDTOIPage = userService.listUserPage(userQuery);
        return userDTOIPage.convert(userDTO -> {
            UserSelectResultVO resultVO = new UserSelectResultVO();
            BeanCopyUtils.copyNonNullProperties(userDTO, resultVO);
            List<PointUserTasks> pointUserTasks = getBaseMapper()
                    .selectList(Wrappers.<PointUserTasks>lambdaQuery()
                            .eq(PointUserTasks::getUserId, details.getId())
                            .orderByDesc(PointUserTasks::getDeadline));
            if (!pointUserTasks.isEmpty()) {
                // 未完成任务数：统计对应用户名下所有布点方案状态为：“未处理、维护中、待提交、退回维护、退回完善、退回重审”的任务数。
                long unCompleteCount = pointUserTasks.stream().filter(a ->
                        unCompleteStatelist.contains(a.getDeployPointStatus())).count();
                resultVO.setUnCompleteCount(unCompleteCount);
                resultVO.setTotalCount(pointUserTasks.size());
                resultVO.setDeadLine(pointUserTasks.get(0).getDeadline() + "");
            }
            return resultVO;
        });
    }

    /**
     * 查询布点人员信息
     *
     * @param blockWorkStageId
     * @return
     */
    @Override
    public UserSelectResultVO userInfo(String blockWorkStageId) {
        // 查询布点人员
        PointUserTasks userTasks = getBaseMapper().selectOne(Wrappers
                .<PointUserTasks>query()
                .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, blockWorkStageId));
        if (userTasks == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该布点任务不存在");
        }
        User byId = userService.getById(userTasks.getUserId());
        if (byId == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到布点人员");
        }
        UserDTO userDTO = userService.convertUserDTO(byId);
        UserSelectResultVO resultVO = new UserSelectResultVO();
        BeanCopyUtils.copyNonNullProperties(userDTO, resultVO);
        List<PointUserTasks> pointUserTasks = getBaseMapper()
                .selectList(Wrappers.<PointUserTasks>lambdaQuery()
                        .eq(PointUserTasks::getUserId, userTasks.getUserId())
                        .orderByDesc(PointUserTasks::getDeadline));
        if (!pointUserTasks.isEmpty()) {
            // 未完成任务数：统计对应用户名下所有布点方案状态为：“未处理、维护中、待提交、退回维护、退回完善、退回重审”的任务数。
            long unCompleteCount = pointUserTasks.stream().filter(a ->
                    unCompleteStatelist.contains(a.getDeployPointStatus())).count();
            resultVO.setUnCompleteCount(unCompleteCount);
            resultVO.setTotalCount(pointUserTasks.size());
            resultVO.setDeadLine(pointUserTasks.get(0).getDeadline() + "");
        }
        return resultVO;
    }

    /**
     * 待分配和已分配阶段任务列表
     *
     * @param workStageIdList
     * @return
     */
    @Override
    public List<PointWorkStageDistributeVO> distributionList(List<String> workStageIdList) {
        List<PointWorkStageDistributeVO> list = new ArrayList<>();
        for (String workStageId : workStageIdList) {
            PointWorkStageDistributeVO distributeVO = new PointWorkStageDistributeVO();
            // 查询地块工作阶段
            BlockWorkStage blockWorkStage = blockWorkStageService.getById(workStageId);
            if (blockWorkStage != null) {
                // 查询地块
                Block block = blockService.getById(blockWorkStage.getBlockId());
                Optional.ofNullable(block).ifPresent(a -> {
                    distributeVO.setBlockName(a.getName());
                    distributeVO.setCode(a.getCode());
                    distributeVO.setEnterprise(enterpriseService.getDetailById(a.getEnterpriseId()));
                    // 地块管理单位
                    String projectId = a.getProjectId();
                    ProjectDTO projectById = projectService.getProjectById(projectId);
                    distributeVO.setOrganization(organizationService.getDetail(projectById.getOrgId()));
                });
                distributeVO.setName(blockWorkStage.getName());
                distributeVO.setBlockWorkStageId(workStageId);
                // 查询工作阶段
                WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
                Optional.ofNullable(workStage).ifPresent(a -> {
                    distributeVO.setWorkStageId(a.getId());
                    distributeVO.setWorkStageName(a.getName());
                });

                // 查询调查任务分配
                SurveyTasks surveyTasks = surveyTasksService.getOne(Wrappers.<SurveyTasks>query()
                        .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageId)
                        .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                        .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));
                Optional.ofNullable(surveyTasks).ifPresent(a -> {
                    distributeVO.setTaskDeadline(a.getDeadline());
                });
            }
            list.add(distributeVO);
        }
        return list;
    }

    /**
     * 地块布点方案查询-分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<PointBlockTasksResultVo> pointBlockTaskPage(PointUserBlockQuery query) {
        if (StringUtils.isNotEmpty(query.getStatus())) {
            query.setStatus(PointQueryStatusEnum.valueOf(query.getStatus()).getColumnValue());
        }
        IPage<BlockVO> page = blockService.pointUserTaskListPage(query);
        return page.convert(blockVO -> {
            PointBlockTasksResultVo resultVO = new PointBlockTasksResultVo();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);

            // 地块管理单位
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
                // 技术管理单位
                List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(resultVO.getProject().getOrgId(),
                        blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
                if (orgIdList.size() > 0) {
                    resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
                }
            }

            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            AtomicReference<Integer> workStageSize = new AtomicReference<>(0);
            Optional.ofNullable(workStageList).ifPresent(a -> {
                workStageSize.set(a.size());
            });
            // 已提交数量
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_ID, blockVO.getId())
                            .in(PointUserTasks.DEPLOY_POINT_STATUS, query.getTaskProcessStatusList()));
            yfpCount = yfpCount == null ? 0 : yfpCount;
            resultVO.setTaskPlan(yfpCount + "/" + workStageSize.get());
            // 设置分配进度颜色
            if (yfpCount == 0) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
            } else if (yfpCount < workStageSize.get()) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
            } else {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.GREEN.name());
            }
            List<PointBlockWorkStageDTO> pointUserWorkStageList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 查询该任务阶段的调查任务分配信息
                    SurveyTasks surveyTasks = surveyTasksService.getOne(Wrappers.<SurveyTasks>query()
                            .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                            .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));
                    if (surveyTasks == null || !SurveyTaskStatusEnum.ALLOCATED.name().equals(surveyTasks.getStatus())) {
                        continue;
                    }

                    PointBlockWorkStageDTO userWorkStageDTO = new PointBlockWorkStageDTO();
                    BeanCopyUtils.copyNonNullProperties(workStageDTO, userWorkStageDTO);

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                            .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    // 任务分配单位
                    userWorkStageDTO.setTasksList(surveyTaskList.stream().map(task -> {
                        TasksDTO tasksDTO = new TasksDTO();
                        BeanCopyUtils.copyNonNullProperties(task, tasksDTO);
                        if (StringUtils.isNotEmpty(task.getOrgId())) {
                            tasksDTO.setOrgId(organizationService.getDetail(task.getOrgId()));
                        }
                        if (StringUtils.isNotEmpty(task.getPrincipal())) {
                            UserDTO userDetail = userService.getUserDetail(task.getPrincipal());
                            Optional.ofNullable(userDetail).ifPresent(a -> {
                                tasksDTO.setPrincipal(userDetail.getId());
                                tasksDTO.setPrincipalName(userDetail.getUsername());
                                tasksDTO.setPrincipalPhone(userDetail.getMobile());
                            });
                        }
                        return tasksDTO;
                    }).collect(Collectors.toList()));

                    Optional.ofNullable(surveyTasks)
                            .ifPresent(a -> userWorkStageDTO.setTaskDeadline(a.getDeadline()));
                    // 查询各样点类型数量
                    PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());
                    userWorkStageDTO.setSoilCount(pointCountDTO.getSoilCount());
                    userWorkStageDTO.setWaterCount(pointCountDTO.getWaterCount());
                    userWorkStageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());

                    // 类型：planText方案文本；planAttach方案附件；selfOpinion自审意见及整改说明；plan点位结构化数据
                    long pointFileCount = pointFileService.count(Wrappers.<PointFile>lambdaQuery()
                            .eq(PointFile::getBlockWorkStageId, workStageDTO.getId())
                            .in(PointFile::getType,
                                    Arrays.asList(UploadPlanTypeEnum.planText.name(),
                                            UploadPlanTypeEnum.planAttach.name(), UploadPlanTypeEnum.selfOpinion.name())));
                    userWorkStageDTO.setFileCount(pointFileCount + '/' + "3");

                    // 查询该任务阶段的布点人员分配信息
                    PointUserTasks pointUserTasks = baseMapper.selectOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));
                    Optional<PointUserTasks> pointUserTasksOptional = Optional.ofNullable(pointUserTasks);
                    if (pointUserTasksOptional.isPresent()) {
                        PointUserTasks userTasks = pointUserTasksOptional.get();
                        if (!StringUtils.isEmpty(userTasks.getUserId())) {
                            UserDTO userDetail = userService.getUserDetail(userTasks.getUserId());
                            userWorkStageDTO.setUserName(userDetail.getUsername());
                        }
                        userWorkStageDTO.setUserId(userTasks.getUserId());
                        userWorkStageDTO.setDeployPointStatus(userTasks.getDeployPointStatus());
                    }

                    List<PointFile> pointFiles = pointFileService.getBaseMapper().selectList(Wrappers.<PointFile>query()
                            .eq(PointFile.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            .eq(PointFile.TYPE, UploadPlanTypeEnum.plan)
                            .eq(PointFile.DELETED, 0));
                    if (pointFiles.size() > 0) {
                        userWorkStageDTO.setIsImport(1);
                    }

                    // 查询质控意见
                    List<QualityControlTasks> qualityControlTasks = qualityControlTasksService.list(
                            Wrappers.<QualityControlTasks>lambdaQuery()
                                    .eq(QualityControlTasks::getBlockWorkStageId, workStageDTO.getId())
                                    .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                    );
                    List<String> city = qualityControlTasks.stream().filter(a -> ServiceLevelEnum.CITY_LEVEL.name().equals(a.getQualityType()))
                            .map(QualityControlTasks::getAuditStatus).collect(Collectors.toList());
                    if (city.size() > 0) {
                        userWorkStageDTO.setCityQualityStatus(PlanAuditStatusEnum.getDescByValue(city.get(0)));
                    }
                    List<String> province = qualityControlTasks.stream().filter(a -> ServiceLevelEnum.PROVINCE_LEVEL.name().equals(a.getQualityType()))
                            .map(QualityControlTasks::getAuditStatus).collect(Collectors.toList());
                    if (province.size() > 0) {
                        userWorkStageDTO.setProvinceQualityStatus(PlanAuditStatusEnum.getDescByValue(province.get(0)));
                    }
                    List<String> country = qualityControlTasks.stream().filter(a -> ServiceLevelEnum.COUNTRY_LEVEL.name().equals(a.getQualityType()))
                            .map(QualityControlTasks::getAuditStatus).collect(Collectors.toList());
                    if (country.size() > 0) {
                        userWorkStageDTO.setCountryQualityStatus(PlanAuditStatusEnum.getDescByValue(country.get(0)));
                    }

                    pointUserWorkStageList.add(userWorkStageDTO);
                }
            }
            resultVO.setWorkStageList(pointUserWorkStageList);
            return resultVO;
        });
    }

    /**
     * 布点方案数据维护-待提交布点方案列表
     *
     * @param workStageIdList
     * @return
     */
    @Override
    public List<SubmitPointTaskVO> pointBlockTaskDefendSubmit(List<String> workStageIdList) {
        List<PointWorkStageDistributeVO> distributeVOS = distributionList(workStageIdList);
        ArrayList<SubmitPointTaskVO> resultList = new ArrayList<>();
        for (PointWorkStageDistributeVO distributeVO : distributeVOS) {
            SubmitPointTaskVO submitPointTaskVO = new SubmitPointTaskVO();
            BeanCopyUtils.copyNonNullProperties(distributeVO, submitPointTaskVO);
            // 查询各样点类型数量
            PointCountDTO pointCountDTO = pointService.groupByPointType(distributeVO.getBlockWorkStageId());
            submitPointTaskVO.setSoilCount(pointCountDTO.getSoilCount());
            submitPointTaskVO.setWaterCount(pointCountDTO.getWaterCount());
            submitPointTaskVO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());

            // 类型：planText方案文本；planAttach方案附件；selfOpinion自审意见及整改说明；plan点位结构化数据
            long pointFileCount = pointFileService.count(Wrappers.<PointFile>lambdaQuery()
                    .eq(PointFile::getBlockWorkStageId, distributeVO.getBlockWorkStageId())
                    .in(PointFile::getType, Arrays.asList(UploadPlanTypeEnum.planText.name(),
                            UploadPlanTypeEnum.planAttach.name(), UploadPlanTypeEnum.selfOpinion.name())));
            submitPointTaskVO.setFileCount(pointFileCount + '/' + "3");
            resultList.add(submitPointTaskVO);
        }
        return resultList;
    }

    /**
     * 布点方案数据维护-待提交布点方案列表
     *
     * @param blockWorkStageId
     * @return
     */
    @Override
    public ResultPointTaskVO pointBlockTaskDefendView(String blockWorkStageId) {
        List<SubmitPointTaskVO> submitPointTaskVOS = pointBlockTaskDefendSubmit(new ArrayList<String>() {{
            add(blockWorkStageId);
        }});
        ResultPointTaskVO resultPointTaskVO = new ResultPointTaskVO();
        if (submitPointTaskVOS != null && submitPointTaskVOS.size() > 0) {
            SubmitPointTaskVO pointTaskVO = submitPointTaskVOS.get(0);
            BeanCopyUtils.copyNonNullProperties(pointTaskVO, resultPointTaskVO);

            // 查询调查任务分配
            SurveyTasks surveyTask = surveyTasksService.getOne(Wrappers
                    .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, blockWorkStageId)
                    .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue()));
            Organization organization = organizationService.getById(surveyTask.getOrgId());
            resultPointTaskVO.setPointUnitName(organization.getName());

            // 查询布点人员
            PointUserTasks userTasks = getBaseMapper().selectOne(Wrappers
                    .<PointUserTasks>query()
                    .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, blockWorkStageId));
            resultPointTaskVO.setUpdateTime(userTasks.getUpdateTime());

            // 点位结构化数据
            resultPointTaskVO
                    .setPointList(pointService
                            .list(Wrappers.<Point>lambdaQuery().eq(Point::getBlockWorkStageId, blockWorkStageId)));

            resultPointTaskVO.setPointFileList(pointFileService.listByBlockWorkStageId(blockWorkStageId));

            UserPrincipalDetails details = UserUtils.getOrThrow();
            AuditOpinion auditOpinion = auditOpinionService.getBaseMapper().selectOne(Wrappers.<AuditOpinion>query()
                    .eq(AuditOpinion.BLOCK_WORK_STAGE_ID, blockWorkStageId)
                    .eq(AuditOpinion.SPECIALIST_USER, details.getId())
                    .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.crew.name())
                    .eq(AuditOpinion.DELETED, 0));
            if (!Objects.isNull(auditOpinion)) {
                AuditOpinionVO auditOpinionVO = new AuditOpinionVO();
                BeanUtils.copyProperties(auditOpinion, auditOpinionVO);
                auditOpinionVO.setAuditOpinionDesc(FeedbackStatusEnum.valueOf(auditOpinion.getAuditOpinion()).getValue());

                AppFile appFile = fileService.getById(auditOpinion.getOpinionFileId());
                if (appFile != null) {
                    auditOpinionVO.setOpinionFile(appFile.getPath());
                }
                resultPointTaskVO.setAuditOpinionVO(auditOpinionVO);
            }
        }
        return resultPointTaskVO;
    }

    /**
     * 布点方案数据整改-列表分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<QualityControlSpecialistTaskResultVO> pointBlockTaskReformPage(PointUserBlockQuery query) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        query.setOrgId(details.getOrgId());
        query.setUserId(details.getId());
        // 查询该任务阶段的调查任务分配信息
        String[] split = null;
        if (StringUtils.isNotEmpty(query.getStatus())) {
            query.setStatus(PointQueryStatusEnum.valueOf(query.getStatus()).getColumnValue());
            split = query.getStatus().replace("(", "")
                    .replace(")", "")
                    .replaceAll("'", "").trim()
                    .split(",");
        }
        query.setDeployPointStatusList(Arrays.asList(PointStatusEnum.back_perfect.getValue(),
                PointStatusEnum.perfect_review.getValue(), PointStatusEnum.back_retrial.getValue(),
                PointStatusEnum.retrial_review.getValue()));
        IPage<BlockVO> page = blockService.pointUserTaskListPage(query);
        String[] finalSplit = split;
        return page.convert(blockVO -> {
            QualityControlSpecialistTaskResultVO resultVO = new QualityControlSpecialistTaskResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);

            // 地块管理单位
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
                // 技术管理单位
                List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(resultVO.getProject().getOrgId(),
                        blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
                if (orgIdList.size() > 0) {
                    resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
                }
            }

            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            AtomicReference<Integer> workStageSize = new AtomicReference<>(0);
            Optional.ofNullable(workStageList).ifPresent(a -> {
                workStageSize.set(a.size());
            });
            // 分子为对应地块下所有布点任务中状态为“完善待复核、重审待复核”的任务数
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_ID, blockVO.getId())
                            .in(PointUserTasks.DEPLOY_POINT_STATUS, Arrays.asList(
                                    PointStatusEnum.perfect_review.getValue(),
                                    PointStatusEnum.retrial_review.getValue())));
            // 分母为该布点人员被分配的对应地块下需整改的布点任务总数
            Long totalCount = getBaseMapper()
                    .selectCount(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_ID, blockVO.getId())
                            .in(PointUserTasks.DEPLOY_POINT_STATUS, query.getDeployPointStatusList()));
            yfpCount = yfpCount == null ? 0 : yfpCount;
            resultVO.setTaskPlan(yfpCount + "/" + totalCount);
            // 设置分配进度颜色
            if (yfpCount == 0) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
            } else if (yfpCount < totalCount) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
            } else {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.GREEN.name());
            }
            List<QualityControlSpecialistBlockWorkStageDTO> pointUserWorkStageList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 查询该任务阶段的调查任务分配信息
                    SurveyTasks surveyTasks = surveyTasksService.getOne(Wrappers.<SurveyTasks>query()
                            .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                            .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()));
                    if (surveyTasks == null) {
                        continue;
                    }
                    // 查询该任务阶段的布点人员分配信息
                    PointUserTasks pointUserTasks = baseMapper.selectOne(
                            Wrappers.<PointUserTasks>query()
                                    .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .in(PointUserTasks.DEPLOY_POINT_STATUS, query.getDeployPointStatusList())
                                    .eq(PointUserTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .in(finalSplit != null && finalSplit.length > 0, PointUserTasks.DEPLOY_POINT_STATUS, finalSplit)
                    );
                    if (pointUserTasks == null) {
                        continue;
                    }

                    List<QualityControlTasks> qualityControlTasksA = qualityControlTasksService.getBaseMapper()
                            .selectList(Wrappers.<QualityControlTasks>query()
                                    .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .in(QualityControlTasks.AUDIT_STATUS, Arrays.asList(
                                            PlanAuditStatusEnum.BACK_PERFECT.getValue(),
                                            PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
                                            PlanAuditStatusEnum.BACK_RETRIAL.getValue(),
                                            PlanAuditStatusEnum.RETRIAL_REVIEW.getValue()))
                            );

                    for (QualityControlTasks qualityControlTasks : qualityControlTasksA) {
                        QualityControlSpecialistBlockWorkStageDTO userWorkStageDTO = new QualityControlSpecialistBlockWorkStageDTO();
                        BeanCopyUtils.copyNonNullProperties(workStageDTO, userWorkStageDTO);
                        userWorkStageDTO.setQualityControlTasksId(qualityControlTasks.getId());

                        List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                                .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                                .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                        );

                        Optional.ofNullable(surveyTasks)
                                .ifPresent(a -> userWorkStageDTO.setTaskDeadline(a.getDeadline()));

                        // 任务分配单位
                        userWorkStageDTO.setTasksList(surveyTaskList.stream().map(task -> {
                            TasksDTO tasksDTO = new TasksDTO();
                            BeanCopyUtils.copyNonNullProperties(task, tasksDTO);
                            if (StringUtils.isNotEmpty(task.getOrgId())) {
                                tasksDTO.setOrgId(organizationService.getDetail(task.getOrgId()));
                            }
                            if (StringUtils.isNotEmpty(task.getPrincipal())) {
                                UserDTO userDetail = userService.getUserDetail(task.getPrincipal());
                                Optional.ofNullable(userDetail).ifPresent(a -> {
                                    tasksDTO.setPrincipal(userDetail.getId());
                                    tasksDTO.setPrincipalName(userDetail.getUsername());
                                    tasksDTO.setPrincipalPhone(userDetail.getMobile());
                                });
                            }
                            return tasksDTO;
                        }).collect(Collectors.toList()));


                        // 查询各样点类型数量
                        PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());
                        userWorkStageDTO.setSoilCount(pointCountDTO.getSoilCount());
                        userWorkStageDTO.setWaterCount(pointCountDTO.getWaterCount());
                        userWorkStageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());

                        // 类型：planText方案文本；planAttach方案附件；selfOpinion自审意见及整改说明；plan点位结构化数据
                        long pointFileCount = pointFileService.count(Wrappers.<PointFile>lambdaQuery()
                                .eq(PointFile::getBlockWorkStageId, workStageDTO.getId())
                                .in(PointFile::getType,
                                        Arrays.asList(UploadPlanTypeEnum.planText.name(),
                                                UploadPlanTypeEnum.planAttach.name(), UploadPlanTypeEnum.selfOpinion.name())));
                        userWorkStageDTO.setFileCount(pointFileCount + '/' + "3");


                        Optional<PointUserTasks> pointUserTasksOptional = Optional.ofNullable(pointUserTasks);
                        if (pointUserTasksOptional.isPresent()) {
                            PointUserTasks userTasks = pointUserTasksOptional.get();
                            if (!StringUtils.isEmpty(userTasks.getUserId())) {
                                UserDTO userDetail = userService.getUserDetail(userTasks.getUserId());
                                userWorkStageDTO.setUserName(userDetail.getUsername());
                            }
                            userWorkStageDTO.setDeployPointStatus(userTasks.getDeployPointStatus());
                            userWorkStageDTO.setUpdateTime(userTasks.getUpdateTime());
                        }
                        userWorkStageDTO.setQualityType(qualityControlTasks.getQualityType());
                        userWorkStageDTO.setQualityTypeDesc(ServiceLevelEnum.getValueByName(qualityControlTasks.getQualityType()));
                        userWorkStageDTO.setQualityOrg(organizationService.getById(qualityControlTasks.getQualityOrgId()));
                        userWorkStageDTO.setPointQualityOrg(organizationService.getById(qualityControlTasks.getPointQualityOrgId()));
                        // 查询文件数量
                        long count = pointFileService.count(Wrappers.<PointFile>lambdaQuery()
                                .eq(PointFile::getBlockWorkStageId, workStageDTO.getId()));
                        if (pointReform.contains(pointUserTasks.getDeployPointStatus()) && count == 4) {
                            userWorkStageDTO.setCheckBox(1);
                        }
                        // 控制按钮
                        // 布点方案状态为 完善待复核，重审待复核 可进行下载方案和查看结果操作；
                        // 退回完善/退回重审可进行重传方案、重导点位和查看结果操作。
                        List<Integer> buttonList = new ArrayList<>();
                        if (Arrays.asList("D018-009", "D018-012").contains(pointUserTasks.getDeployPointStatus())) {
                            buttonList.add(1);
                            buttonList.add(2);
                            buttonList.add(5);
                        } else if (Arrays.asList("D018-008", "D018-011").contains(pointUserTasks.getDeployPointStatus())) {
                            buttonList.add(3);
                            buttonList.add(4);
                            buttonList.add(2);
                            buttonList.add(5);
                        }
                        userWorkStageDTO.setPointReformButton(buttonList);
                        pointUserWorkStageList.add(userWorkStageDTO);
                    }
                }
            }
            resultVO.setWorkStageList(pointUserWorkStageList);
            return resultVO;
        });
    }
}
