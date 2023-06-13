package com.csbaic.edatope.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.mapper.QualityControlTasksMapper;
import com.csbaic.edatope.app.mapper.SpecialistMapper;
import com.csbaic.edatope.app.model.command.FeedbackCmd;
import com.csbaic.edatope.app.model.command.QualityControlBackCmd;
import com.csbaic.edatope.app.model.command.QualityControlSpecialistTaskCmd;
import com.csbaic.edatope.app.model.command.QualityControlTaskCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.*;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.utils.UserUtils;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.file.entity.AppFile;
import com.csbaic.edatope.file.model.vo.UploadFileVO;
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
 * 布点质控任务 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Service
public class QualityControlTasksServiceImpl extends ServiceImpl<QualityControlTasksMapper, QualityControlTasks> implements IQualityControlTasksService {

    @Autowired
    private IBlockWorkStageService blockWorkStageService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IOrganizationBizTypeService organizationBizTypeService;
    @Autowired
    @Lazy
    private IBlockService blockService;
    @Autowired
    @Lazy
    private ITechOrganizationAuthorizeService techOrganizationAuthorizeService;
    @Autowired
    @Lazy
    private IPointService pointService;
    @Autowired
    @Lazy
    private ISurveyTasksService surveyTasksService;
    @Autowired
    private IUserService userService;
    @Autowired
    @Lazy
    private ITechOrganizationAuthorizeCityService techOrganizationAuthorizeCityService;
    @Autowired
    @Lazy
    private IPointUserTasksService pointUserTasksService;
    @Autowired
    @Lazy
    private ISpecialistService specialistService;
    @Autowired
    @Lazy
    private ISpecialistUserService specialistUserService;
    @Autowired
    @Lazy
    private IAuditOpinionService auditOpinionService;
    @Autowired
    private SpecialistMapper specialistMapper;
    @Autowired
    private IEnterpriseService enterpriseService;
    @Autowired
    private IWorkStageService workStageService;
    @Autowired
    private IAppFileService fileService;

    @Autowired
    private IPointTasksRecordService pointTasksRecordService;

    @Autowired
    private IPointAuditRecordService pointAuditRecordService;

    private static final List<String> unComplateStatus = Arrays.asList(PlanAuditStatusEnum.CROSS.getValue()
            , PlanAuditStatusEnum.STAY_AUDIT.getValue(), PlanAuditStatusEnum.STAY_COLLECT.getValue(),
            PlanAuditStatusEnum.BACK_PERFECT.getValue(), PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
            PlanAuditStatusEnum.BACK_RETRIAL.getValue(), PlanAuditStatusEnum.RETRIAL_REVIEW.getValue(),
            PlanAuditStatusEnum.BACK_MAINTAIN.getValue());

    private static final List<String> sendBackStatus = Arrays.asList(PlanAuditStatusEnum.RETRIAL_REVIEW_PASS.getValue()
            , PlanAuditStatusEnum.PERFECT_REVIEW_PASS.getValue(), PlanAuditStatusEnum.PASS.getValue(),
            PlanAuditStatusEnum.BACK_PERFECT.getValue(), PlanAuditStatusEnum.BACK_RETRIAL.getValue());

    /**
     * 根据状态查询数量
     *
     * @param status
     * @return
     */
    @Override
    public Long getCountByStatus(String qualitySpecialistId, String... status) {
        return getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                .in(QualityControlTasks::getAuditStatus, status)
                .eq(QualityControlTasks::getQualitySpecialistId, qualitySpecialistId));
    }

    /**
     * 布点质控单位任务-分配单位任务
     * 同一个技术单位的技术负责人可能会同时被多个甚至是不同级别的行政管理单位授权为技术管理单位
     *
     * @param qualityControlTaskCmd
     */
    @Override
    public void create(QualityControlTaskCmd qualityControlTaskCmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 当前用户单位
        Organization organization = organizationService.getById(details.getOrgId());
        List<String> bizTypes = organizationBizTypeService.getBizTypes(organization.getId());
        String pointQualityOrgId = qualityControlTaskCmd.getOrgId();
        Organization pointQualityOrg = organizationService.getById(pointQualityOrgId);
        if (pointQualityOrg == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "布点质控单位不存在！");
        }
        // 服务级别 如果是当前用户属于技术管理单位找到上级单位的服务级别
        String serviceLevel = "";
        if (bizTypes.contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            serviceLevel = organization.getServiceLevel();
        } else if (bizTypes.contains(OrganizationBizTypeEnum.TECHNOLOGY_MANAGEMENT.getValue())) {
            if (StringUtils.isEmpty(qualityControlTaskCmd.getOwnerId())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "归属单位id不能为空");
            }
            Organization ownerOrganization = organizationService.getById(qualityControlTaskCmd.getOwnerId());
            serviceLevel = ownerOrganization.getServiceLevel();
        }
        List<QualityControlTaskCmd.QualityControlTaskDeadLineCmd> blockWorkStageIds = qualityControlTaskCmd.getBlockWorkStageIds();
        List<String> collect = blockWorkStageIds.stream().map(a -> a.getBlockWorkStageId()).collect(Collectors.toList());
        // 查询当前服务级别下是否已分配布点质控单位任务
        Long count = getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                .in(QualityControlTasks::getBlockWorkStageId, collect)
                .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                .eq(QualityControlTasks::getQualityType, serviceLevel));
        if (count > 0) {
            throw BizRuntimeException.from(ResultCode.ERROR, "服务级别:" + serviceLevel + "下，任务已分配布点质控单位");
        }
        for (QualityControlTaskCmd.QualityControlTaskDeadLineCmd blockWorkStageId : blockWorkStageIds) {
            String blockWorkStageIdA = blockWorkStageId.getBlockWorkStageId();
            BlockWorkStage blockWorkStage = blockWorkStageService.getById(blockWorkStageIdA);
            AtomicReference<String> deployPointStatus = new AtomicReference<>("");
            PointUserTasks pointUserTasks = pointUserTasksService.getById(blockWorkStageIdA);
            Optional.ofNullable(pointUserTasks).ifPresent(a -> {
                deployPointStatus.set(a.getDeployPointStatus());
            });
            QualityControlTasks tasks = getBaseMapper().selectOne(
                    Wrappers.<QualityControlTasks>lambdaQuery()
                            .eq(QualityControlTasks::getBlockWorkStageId, blockWorkStageIdA)
                            .eq(QualityControlTasks::getQualityType, serviceLevel)
                            .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.RECALL.name())
            );
            QualityControlTasks qualityControlTasks = new QualityControlTasks();
            if (tasks != null) {
                qualityControlTasks = tasks;
            }
            qualityControlTasks.setBlockId(blockWorkStage.getBlockId());
            qualityControlTasks.setBlockWorkStageId(blockWorkStageIdA);
            qualityControlTasks.setPointQualityOrgId(qualityControlTaskCmd.getOrgId());
            qualityControlTasks.setPrincipal(qualityControlTaskCmd.getPrincipal());
            qualityControlTasks.setPrincipalPhone(qualityControlTaskCmd.getPrincipalPhone());
            qualityControlTasks.setQualityOrgId(details.getOrgId());
            qualityControlTasks.setQualityType(serviceLevel);
            qualityControlTasks.setDeadline(blockWorkStageId.getDeadline());
            qualityControlTasks.setDistributeStatus(SurveyTaskStatusEnum.ALLOCATED.name());
            qualityControlTasks.setAuditStatus(PlanAuditStatusEnum.CROSS.getValue());
            if (tasks != null) {
                updateById(qualityControlTasks);
            } else {
                save(qualityControlTasks);
            }

            // 记录操作记录
            pointTasksRecordService.save(new PointTasksRecord(blockWorkStageIdA,
                    details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    OperateItemsEnum.DISTRIBUTE_CONTROL_TASK.getDesc(), OperateTypeEnum.POINT_CONTROL_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                    null, deployPointStatus.get(), PlanAuditStatusEnum.CROSS.getValue()
            ));
        }
    }

    /**
     * 布点质控单位任务-撤回单位任务
     *
     * @param qualityControlTasksId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String qualityControlTasksId) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 当前用户单位
        Organization organization = organizationService.getById(details.getOrgId());
        QualityControlTasks byId = getById(qualityControlTasksId);
        if (byId == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到该布点质控任务");
        }
        if (!SurveyTaskStatusEnum.ALLOCATED.name().equals(byId.getDistributeStatus())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前状态为" + byId.getDistributeStatus() + ",不可撤回");
        }
        AtomicReference<String> deployPointStatus = new AtomicReference<>("");
        PointUserTasks pointUserTasks = pointUserTasksService.getById(byId.getBlockWorkStageId());
        Optional.ofNullable(pointUserTasks).ifPresent(a -> {
            deployPointStatus.set(a.getDeployPointStatus());
        });
        update(
                Wrappers.<QualityControlTasks>lambdaUpdate()
                        .eq(QualityControlTasks::getId, byId.getId())
                        .set(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.RECALL.name())
                        .set(QualityControlTasks::getPrincipal, null)
                        .set(QualityControlTasks::getPointQualityOrgId, null)
                        .set(QualityControlTasks::getPrincipalPhone, null)
                        .set(QualityControlTasks::getDeadline, null)
        );

        // 记录操作记录
        pointTasksRecordService.save(new PointTasksRecord(byId.getBlockWorkStageId(),
                details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.RECALL_CONTROL_TASK.getDesc(), OperateTypeEnum.POINT_CONTROL_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                null, deployPointStatus.get(), byId.getAuditStatus()
        ));
    }

    /**
     * 布点质控单位任务-列表分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<QualityControlTaskResultVO> qualityControlTaskPage(QualityControlTasksQuery query) {
        // 当前用户单位
        Organization organization = organizationService.getById(UserUtils.getUserOrgId());
        List<String> bizTypes = organizationBizTypeService.getBizTypes(organization.getId());
        List<String> authOrgIdList = new ArrayList<String>();
        IPage<QualityControlTaskResultVO> page = new Page<>();

        String serviceLevel = "";
        // 如果是行政管理单位： 查询当前行政管理单位和辖区内所有下级行政管理单位创建添加的地块及工作阶段任务
        // 如果是技术管理单位的技术负责人：查询该技术负责人被授权负责的区域内的地块及工作阶段任务
        boolean government = true;
        if (bizTypes.contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            authOrgIdList.addAll(organizationService.getGovernmentOrgId(organization.getId()));
            authOrgIdList.add(organization.getId());
            query.setAuthOrgIdList(authOrgIdList);
            serviceLevel = organization.getServiceLevel();
        } else if (bizTypes.contains(OrganizationBizTypeEnum.TECHNOLOGY_MANAGEMENT.getValue())) {
            government = false;
            if (StringUtils.isEmpty(query.getAuthOrgId())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "归属单位id不能为空");
            }
            authOrgIdList.add(query.getAuthOrgId());
            Organization ownerOrganization = organizationService.getById(query.getAuthOrgId());
            serviceLevel = ownerOrganization.getServiceLevel();

            // 根据行政管理单位的服务级别确定技术管理单位负责的区域下地块
            TechOrganizationAuthorize city = techOrganizationAuthorizeService.getAuthorizeCityByOwnerIdAndOrgId(query.getAuthOrgId(), organization.getId());
            if (city == null) {
                return page;
            } else {
                List<TechOrganizationAuthorizeCity> cityList = techOrganizationAuthorizeCityService.list(
                        Wrappers.<TechOrganizationAuthorizeCity>lambdaQuery()
                                .eq(TechOrganizationAuthorizeCity::getAuthorizeId, city.getId())
                );
                if (cityList.isEmpty()) {
                    return page;
                }
                List<String> areaList = new ArrayList<>();
                String areaCodeColumn = "";
                switch (serviceLevel) {
                    // 1.如果行政管理单位为国家级，则授权的技术管理单位负责的区域为省级区域
                    case "COUNTRY_LEVEL":
                        areaCodeColumn = "province_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getProvinceCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    // 2.如果行政管理单位为省级，则授权的技术管理单位负责的区域为市级区域
                    case "PROVINCE_LEVEL":
                        areaCodeColumn = "city_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getCityCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    // 3.如果行政管理单位为市级，则授权的技术管理单位负责的区域为区县级区域
                    case "CITY_LEVEL":
                        areaCodeColumn = "district_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getDistrictCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    default:
                        break;
                }
                query.setAreaCodeColumn(areaCodeColumn);
                query.setProvinceCodeList(areaList);
            }
        } else {
            return page;
        }
        IPage<BlockVO> blockVOIPage = blockService.qualityControlTaskPage(query);
        boolean finalGovernment = government;
        String finalServiceLevel = serviceLevel;
        String finalServiceLevel1 = serviceLevel;
        return blockVOIPage.convert(blockVO -> {
            QualityControlTaskResultVO resultVO = new QualityControlTaskResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);

            // 地块管理单位
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
            }

            if (finalGovernment) {
                List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(organization.getId(),
                        blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
                if (orgIdList.size() > 0) {
                    resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
                }
            } else {
                resultVO.setTechnicalOrg(organizationService.getDetail(organization.getId()));
            }

            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            AtomicReference<Integer> workStageSize = new AtomicReference<>(0);
            Optional.ofNullable(workStageList).ifPresent(a -> {
                workStageSize.set(a.size());
            });
            // 已分配数量
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .eq(QualityControlTasks.QUALITY_TYPE, finalServiceLevel));
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
            List<QualityControlBlockWorkStageDTO> resultList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    QualityControlBlockWorkStageDTO stageDTO = new QualityControlBlockWorkStageDTO();
                    BeanCopyUtils.copyNonNullProperties(workStageDTO, stageDTO);
                    // 查询该任务阶段的调查任务分配信息
                    QualityControlTasks qualityControlTasksA = getBaseMapper()
                            .selectOne(Wrappers.<QualityControlTasks>query()
                                    .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .eq(QualityControlTasks.QUALITY_TYPE, finalServiceLevel1));
                    if (qualityControlTasksA == null) {
                        stageDTO.setStatus(SurveyTaskStatusEnum.NOT_ALLOT.name());
                    } else {
                        stageDTO.setQualityControlTasksId(qualityControlTasksA.getId());
                        stageDTO.setStatus(qualityControlTasksA.getDistributeStatus());
                    }
                    String status = query.getStatus();
                    if (status != null) {
                        if (status.equals("ALREADY_DISTRIBUTE") &&
                                !stageDTO.getStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                            continue;
                        } else if (status.equals("WAIT_DISTRIBUTE") &&
                                stageDTO.getStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                            continue;
                        }
                    }

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                            .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    // 任务分配单位
                    stageDTO.setTasksList(surveyTaskList.stream().map(task -> {
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
                    stageDTO.setSoilCount(pointCountDTO.getSoilCount());
                    stageDTO.setWaterCount(pointCountDTO.getWaterCount());
                    stageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());

                    // 各服务级别审核状态
                    List<QualityControlTasks> qualityControlTasks = getBaseMapper()
                            .selectList(Wrappers.<QualityControlTasks>query()
                                    .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            );
                    stageDTO.setQualityControlDTOList(qualityControlTasks.stream().map(a -> {
                        QualityControlDTO qualityControlDTO = new QualityControlDTO();
                        qualityControlDTO.setQualityOrg(organizationService.getById(a.getQualityOrgId()));
                        qualityControlDTO.setQualityType(a.getQualityType());
                        qualityControlDTO.setAuditStatus(a.getAuditStatus());
                        return qualityControlDTO;
                    }).collect(Collectors.toList()));

                    resultList.add(stageDTO);
                }
            }
            resultVO.setWorkStageList(resultList);
            return resultVO;
        });
    }

    /**
     * 布点质控专家组任务-分配任务
     *
     * @param qualityControlSpecialistTaskCmd
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void specialistCreate(QualityControlSpecialistTaskCmd qualityControlSpecialistTaskCmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 当前用户单位
        Organization organization = organizationService.getById(details.getOrgId());
        List<String> qualityControlTasksIds = qualityControlSpecialistTaskCmd.getQualityControlTasksIds();
        String qualitySpecialistId = qualityControlSpecialistTaskCmd.getQualitySpecialistId();
        for (String qualityControlTasksId : qualityControlTasksIds) {
            QualityControlTasks byId = getById(qualityControlTasksId);
            if (byId.getDistributeSpecialistStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该任务已分配质控专家组");
            }
            byId.setQualitySpecialistId(qualitySpecialistId);
            byId.setAuditStatus(PlanAuditStatusEnum.CROSS.getValue());
            byId.setDistributeSpecialistStatus(SurveyTaskStatusEnum.ALLOCATED.name());
            updateById(byId);

            AtomicReference<String> deployPointStatus = new AtomicReference<>("");
            PointUserTasks pointUserTasks = pointUserTasksService.getById(byId.getBlockWorkStageId());
            Optional.ofNullable(pointUserTasks).ifPresent(a -> {
                deployPointStatus.set(a.getDeployPointStatus());
            });

            // 记录操作记录
            pointTasksRecordService.save(new PointTasksRecord(byId.getBlockWorkStageId(),
                    details.getOrgId(), details.getId(),
                    organization.getName(), details.getNickName(),
                    OperateItemsEnum.DISTRIBUTE_CONTROL_EXPERTS_TASK.getDesc(), OperateTypeEnum.POINT_CONTROL_TASK.getDesc(),
                    ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                    null, deployPointStatus.get(), byId.getAuditStatus()
            ));
        }
    }

    /**
     * 布点质控专家组任务-撤回专家组任务
     *
     * @param qualityControlTasksId
     */
    @Override
    public void specialistDelete(String qualityControlTasksId) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 当前用户单位
        Organization organization = organizationService.getById(details.getOrgId());
        QualityControlTasks byId = getById(qualityControlTasksId);
        if (!byId.getDistributeSpecialistStatus().equals(SurveyTaskStatusEnum.ALLOCATED.name())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该任务未分配质控专家组");
        }
        update(Wrappers.<QualityControlTasks>lambdaUpdate()
                .set(QualityControlTasks::getQualitySpecialistId, null)
                .set(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.RECALL.name())
                .eq(QualityControlTasks::getId, qualityControlTasksId)
        );

        AtomicReference<String> deployPointStatus = new AtomicReference<>("");
        PointUserTasks pointUserTasks = pointUserTasksService.getById(byId.getBlockWorkStageId());
        Optional.ofNullable(pointUserTasks).ifPresent(a -> {
            deployPointStatus.set(a.getDeployPointStatus());
        });

        // 记录操作记录
        pointTasksRecordService.save(new PointTasksRecord(byId.getBlockWorkStageId(),
                details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.RECALL_CONTROL_EXPERTS_TASK.getDesc(), OperateTypeEnum.POINT_CONTROL_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                null, deployPointStatus.get(), byId.getAuditStatus()
        ));
    }

    /**
     * 布点质控专家组任务-列表分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<QualityControlSpecialistTaskResultVO> specialistPage(QualityControlTasksQuery query) {
        String userOrgId = UserUtils.getUserOrgId();
        query.setPointQualityOrgId(userOrgId);
        if (StringUtils.isNotEmpty(query.getStatus())) {
            query.setStatus(QualityControlStatusEnum.valueOf(query.getStatus()).getColumnValue());
        }
        IPage<BlockVO> blockIPage = blockService.specialistPage(query);
        return blockIPage.convert(blockVO -> {
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

            // 总数
            Long totalCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                    );
            // 已分配数量
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                            .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                    );
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
            if (totalCount == 0) {
                return resultVO;
            }
            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            List<QualityControlSpecialistBlockWorkStageDTO> resultList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 查询该任务阶段的调查任务分配信息
                    String[] split = null;
                    if (StringUtils.isNotEmpty(query.getStatus())) {
                        split = query.getStatus().replace("(", "")
                                .replace(")", "")
                                .replaceAll("'", "").trim()
                                .split(",");
                    }
                    List<QualityControlTasks> qualityControlTasksA = getBaseMapper()
                            .selectList(Wrappers.<QualityControlTasks>query()
                                    .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                    .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .in(StringUtils.isNotEmpty(query.getStatus())
                                            , QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS,
                                            split)
                            );
                    if (qualityControlTasksA == null || qualityControlTasksA.isEmpty()) {
                        continue;
                    }

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                            .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    // 任务分配单位
                    List<TasksDTO> tasksDTOS = surveyTaskList.stream().map(task -> {
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
                    }).collect(Collectors.toList());

                    // 查询各样点类型数量
                    PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());

                    // 查询布点任务信息
                    PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    for (QualityControlTasks task : qualityControlTasksA) {
                        QualityControlSpecialistBlockWorkStageDTO stageDTO = new QualityControlSpecialistBlockWorkStageDTO();
                        BeanCopyUtils.copyNonNullProperties(workStageDTO, stageDTO);
                        stageDTO.setQualityControlTasksId(task.getId());
                        stageDTO.setTasksList(tasksDTOS);
                        stageDTO.setSoilCount(pointCountDTO.getSoilCount());
                        stageDTO.setWaterCount(pointCountDTO.getWaterCount());
                        stageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());
                        Optional.ofNullable(pointUserTasks).ifPresent(a -> {
                            stageDTO.setDeployPointStatus(pointUserTasks.getDeployPointStatus());
                        });
                        stageDTO.setQualityType(task.getQualityType());
                        stageDTO.setQualityTypeDesc(ServiceLevelEnum.getValueByName(task.getQualityType()));
                        stageDTO.setQualityOrg(organizationService.getById(task.getQualityOrgId()));
                        stageDTO.setSpecialist(specialistService.getById(task.getQualitySpecialistId()));
                        stageDTO.setStatus(task.getDistributeSpecialistStatus());
                        resultList.add(stageDTO);
                    }
                }
            }
            resultVO.setWorkStageList(resultList);
            return resultVO;
        });
    }

    @Override
    public IPage<QualityControlSpecialistTaskResultVO> feedbackPage(QualityControlTasksQuery query) {
        UserPrincipalDetails details = UserUtils.getOrThrow();
        if (StringUtils.isNotEmpty(query.getOpinionStatus())) {
            query.setOpinionStatus(QualityControlStatusEnum.valueOf(query.getOpinionStatus()).getColumnValue());
        }
        List<SpecialistUser> specialistUsers = specialistUserService.getBaseMapper().selectList(Wrappers.<SpecialistUser>query().eq(SpecialistUser.USER_ID, details.getId()).eq(SpecialistUser.DELETED, 0));
        List<String> collect = specialistUsers.stream().map(SpecialistUser::getSpecialistId).collect(Collectors.toList());
        if (collect.size() > 0) {
            query.setQualitySpecialistId(collect);

            IPage<BlockVO> blockIPage = blockService.specialistPage(query);
            return blockIPage.convert(blockVO -> {
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

                // 总数
                Long totalCount = getBaseMapper()
                        .selectCount(Wrappers.<QualityControlTasks>query()
                                .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                .in(QualityControlTasks.QUALITY_SPECIALIST_ID, collect)
                        );
                // 已通过数量
                List<String> auditStatus = new ArrayList<>();
                auditStatus.add("D020-004");
                auditStatus.add("D020-007");
                auditStatus.add("D020-010");
                Long ytgCount = getBaseMapper()
                        .selectCount(Wrappers.<QualityControlTasks>query()
                                .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                .in(QualityControlTasks.QUALITY_SPECIALIST_ID, collect)
                                .in(QualityControlTasks.AUDIT_STATUS, auditStatus)
                        );
                ytgCount = ytgCount == null ? 0 : ytgCount;
                resultVO.setTaskPlan(ytgCount + "/" + totalCount);
                // 设置分配进度颜色
                if (ytgCount == 0) {
                    resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
                } else if (ytgCount < totalCount) {
                    resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
                } else {
                    resultVO.setTaskPlanColor(TaskPlanColorEnum.GREEN.name());
                }
                if (totalCount == 0) {
                    return resultVO;
                }
                List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
                List<QualityControlSpecialistBlockWorkStageDTO> resultList = new ArrayList<>();
                if (workStageList != null) {
                    for (BlockWorkStageDTO workStageDTO : workStageList) {
                        // 查询该任务阶段的调查任务分配信息
                        String[] split = null;
                        if (StringUtils.isNotEmpty(query.getOpinionStatus())) {
                            split = query.getOpinionStatus().replace("(", "")
                                    .replace(")", "")
                                    .replaceAll("'", "").trim()
                                    .split(",");
                        }
                        // 查询该任务阶段的调查任务分配信息
                        List<QualityControlTasks> qualityControlTasksA = getBaseMapper()
                                .selectList(Wrappers.<QualityControlTasks>query()
                                        .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                        .in(QualityControlTasks.QUALITY_SPECIALIST_ID, collect)
                                        .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                        .in(StringUtils.isNotEmpty(query.getOpinionStatus())
                                                , QualityControlTasks.AUDIT_STATUS, split)
                                );
                        if (qualityControlTasksA == null || qualityControlTasksA.isEmpty()) {
                            continue;
                        }

                        List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                                .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                        // 任务分配单位
                        List<TasksDTO> tasksDTOS = surveyTaskList.stream().map(task -> {
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
                        }).collect(Collectors.toList());

                        // 查询各样点类型数量
                        PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());

                        // 查询布点任务信息
                        PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                                .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                        for (QualityControlTasks task : qualityControlTasksA) {
                            QualityControlSpecialistBlockWorkStageDTO stageDTO = new QualityControlSpecialistBlockWorkStageDTO();
                            BeanCopyUtils.copyNonNullProperties(workStageDTO, stageDTO);
                            stageDTO.setQualityControlTasksId(task.getId());
                            stageDTO.setTasksList(tasksDTOS);
                            stageDTO.setSoilCount(pointCountDTO.getSoilCount());
                            stageDTO.setWaterCount(pointCountDTO.getWaterCount());
                            stageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());
                            stageDTO.setDeployPointStatus(pointUserTasks.getDeployPointStatus());
                            stageDTO.setQualityType(task.getQualityType());
                            stageDTO.setQualityOrg(organizationService.getById(task.getQualityOrgId()));
                            stageDTO.setPointQualityOrg(organizationService.getById(task.getPointQualityOrgId()));
                            stageDTO.setSpecialist(specialistService.getById(task.getQualitySpecialistId()));
                            stageDTO.setStatus(task.getDistributeSpecialistStatus());
                            stageDTO.setAuditStatus(task.getAuditStatus());

                            Long totalAuditPlan = specialistUserService.getBaseMapper().selectCount(Wrappers.<SpecialistUser>query().eq(SpecialistUser.SPECIALIST_ID, task.getQualitySpecialistId()));
                            Long cyCount = auditOpinionService.getBaseMapper().selectCount(Wrappers.<AuditOpinion>query()
                                    .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, task.getId())
                                    .eq(AuditOpinion.QUALITY_SPECIALIST_ID, task.getQualitySpecialistId())
                                    .eq(AuditOpinion.DELETED, 0));
                            cyCount = cyCount == null ? 0 : cyCount;
                            stageDTO.setAuditPlan(cyCount + "/" + totalAuditPlan);
                            // 设置审核进度颜色
                            if (cyCount == 0) {
                                stageDTO.setAuditPlanColor(TaskPlanColorEnum.RED.name());
                            } else if (cyCount < totalAuditPlan) {
                                stageDTO.setAuditPlanColor(TaskPlanColorEnum.ORANGE.name());
                            } else {
                                stageDTO.setAuditPlanColor(TaskPlanColorEnum.GREEN.name());
                            }

                            Long feedback = auditOpinionService.getBaseMapper().selectCount(Wrappers.<AuditOpinion>query()
                                    .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, task.getId())
                                    .eq(AuditOpinion.QUALITY_SPECIALIST_ID, task.getQualitySpecialistId())
                                    .eq(AuditOpinion.SPECIALIST_USER, details.getId())
                                    .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.crew.name())
                                    .eq(AuditOpinion.DELETED, 0));
                            stageDTO.setFeedback(feedback > 0 ? 1 : 0);

                           /* List<String> auditStatuList = Arrays.asList(PlanAuditStatusEnum.PASS.getValue()
                                    , PlanAuditStatusEnum.PERFECT_REVIEW_PASS.getValue(), PlanAuditStatusEnum.RETRIAL_REVIEW_PASS.getValue(),
                                    PlanAuditStatusEnum.BACK_PERFECT.getValue(), PlanAuditStatusEnum.BACK_RETRIAL.getValue());
                                    if (auditStatuList.stream().anyMatch(item -> item.equals(task.getAuditStatus()))) {
                                stageDTO.setCollect(1);
                            }*/
                            Long feedbackCollect = auditOpinionService.getBaseMapper().selectCount(Wrappers.<AuditOpinion>query()
                                    .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, task.getId())
                                    .eq(AuditOpinion.QUALITY_SPECIALIST_ID, task.getQualitySpecialistId())
                                    .eq(AuditOpinion.SPECIALIST_USER, details.getId())
                                    .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.collect.name())
                                    .eq(AuditOpinion.DELETED, 0));
                            stageDTO.setCollect(feedbackCollect > 0 ? 1 : 0);

                            resultList.add(stageDTO);
                        }
                    }
                }
                resultVO.setWorkStageList(resultList);
                return resultVO;
            });
        }
        return null;
    }

    @Override
    public IPage<QualityControlSpecialistTaskResultVO> sendBackPage(QualityControlTasksQuery query) {
        // 当前用户单位
        Organization organization = organizationService.getById(UserUtils.getUserOrgId());
        List<String> bizTypes = organizationBizTypeService.getBizTypes(organization.getId());
        List<String> authOrgIdList = new ArrayList<String>();
        IPage<QualityControlSpecialistTaskResultVO> page = new Page<>();

        String serviceLevel = "";
        // 如果是行政管理单位： 查询当前行政管理单位创建添加的地块及工作阶段任务
        // 如果是技术管理单位的技术负责人：查询该技术负责人被授权负责的区域内的地块及工作阶段任务
        if (bizTypes.contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            authOrgIdList.add(organization.getId());
            query.setAuthOrgIdList(authOrgIdList);
        } else if (bizTypes.contains(OrganizationBizTypeEnum.TECHNOLOGY_MANAGEMENT.getValue())) {
            if (StringUtils.isEmpty(query.getAuthOrgId())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "归属单位id不能为空");
            }
            authOrgIdList.add(query.getAuthOrgId());
            Organization ownerOrganization = organizationService.getById(query.getAuthOrgId());
            serviceLevel = ownerOrganization.getServiceLevel();

            // 根据行政管理单位的服务级别确定技术管理单位负责的区域下地块
            TechOrganizationAuthorize city = techOrganizationAuthorizeService.getAuthorizeCityByOwnerIdAndOrgId(query.getAuthOrgId(), organization.getId());
            if (city == null) {
                return page;
            } else {
                List<TechOrganizationAuthorizeCity> cityList = techOrganizationAuthorizeCityService.list(
                        Wrappers.<TechOrganizationAuthorizeCity>lambdaQuery()
                                .eq(TechOrganizationAuthorizeCity::getAuthorizeId, city.getId())
                );
                if (cityList.isEmpty()) {
                    return page;
                }
                List<String> areaList = new ArrayList<>();
                String areaCodeColumn = "";
                switch (serviceLevel) {
                    // 1.如果行政管理单位为国家级，则授权的技术管理单位负责的区域为省级区域
                    case "COUNTRY_LEVEL":
                        areaCodeColumn = "province_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getProvinceCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    // 2.如果行政管理单位为省级，则授权的技术管理单位负责的区域为市级区域
                    case "PROVINCE_LEVEL":
                        areaCodeColumn = "city_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getCityCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    // 3.如果行政管理单位为市级，则授权的技术管理单位负责的区域为区县级区域
                    case "CITY_LEVEL":
                        areaCodeColumn = "district_code";
                        areaList = cityList.stream()
                                .map(TechOrganizationAuthorizeCity::getDistrictCode)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                    default:
                        break;
                }
                query.setAreaCodeColumn(areaCodeColumn);
                query.setProvinceCodeList(areaList);
            }
        } else {
            return page;
        }
        IPage<BlockVO> blockVOIPage = blockService.sendBackPage(query);
        return blockVOIPage.convert(blockVO -> {
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

            // 总数
            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            AtomicReference<Integer> workStageSize = new AtomicReference<>(0);
            Optional.ofNullable(workStageList).ifPresent(a -> {
                workStageSize.set(a.size());
            });
            // 已通过数量
            List<String> auditStatus = Arrays.asList(PlanAuditStatusEnum.PASS.getValue()
                    , PlanAuditStatusEnum.PERFECT_REVIEW_PASS.getValue(), PlanAuditStatusEnum.RETRIAL_REVIEW_PASS.getValue(),
                    PlanAuditStatusEnum.BACK_PERFECT.getValue(), PlanAuditStatusEnum.BACK_RETRIAL.getValue());
            Long ytgCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .notIn(QualityControlTasks.AUDIT_STATUS, auditStatus)
                    );
            ytgCount = ytgCount == null ? 0 : ytgCount;
            resultVO.setTaskPlan(ytgCount + "/" + workStageSize);
            // 设置分配进度颜色
            resultVO.setTaskPlan(ytgCount + "/" + workStageSize.get());
            // 设置分配进度颜色
            if (ytgCount == 0) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
            } else if (ytgCount < workStageSize.get()) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
            } else {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.GREEN.name());
            }

            List<QualityControlSpecialistBlockWorkStageDTO> resultList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 查询该任务阶段的调查任务分配信息
                    String[] split = null;
                    if (StringUtils.isNotEmpty(query.getOpinionStatus())) {
                        split = query.getOpinionStatus().replace("(", "")
                                .replace(")", "")
                                .replaceAll("'", "").trim()
                                .split(",");
                    }
                    // 查询该任务阶段的调查任务分配信息
                    List<QualityControlTasks> qualityControlTasksA = getBaseMapper()
                            .selectList(Wrappers.<QualityControlTasks>query()
                                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
//                                    .in(QualityControlTasks.QUALITY_SPECIALIST_ID, collect)
                                            .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                            .in(StringUtils.isNotEmpty(query.getOpinionStatus())
                                                    , QualityControlTasks.AUDIT_STATUS, split)
                            );
                    if (qualityControlTasksA == null || qualityControlTasksA.isEmpty()) {
                        continue;
                    }

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(Wrappers
                            .<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                            .eq(SurveyTasks.TYPE, OrganizationBizTypeEnum.LAYOUT.getValue())
                    );

                    // 任务分配单位
                    List<TasksDTO> tasksDTOS = surveyTaskList.stream().map(task -> {
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
                    }).collect(Collectors.toList());

                    // 查询各样点类型数量
                    PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());

                    // 查询布点任务信息
                    PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    for (QualityControlTasks task : qualityControlTasksA) {
                        QualityControlSpecialistBlockWorkStageDTO stageDTO = new QualityControlSpecialistBlockWorkStageDTO();
                        BeanCopyUtils.copyNonNullProperties(workStageDTO, stageDTO);
                        stageDTO.setQualityControlTasksId(task.getId());
                        stageDTO.setTasksList(tasksDTOS);
                        stageDTO.setSoilCount(pointCountDTO.getSoilCount());
                        stageDTO.setWaterCount(pointCountDTO.getWaterCount());
                        stageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());
                        if (!Objects.isNull(pointUserTasks)) {
                            stageDTO.setDeployPointStatus(pointUserTasks.getDeployPointStatus());
                        }
                        stageDTO.setQualityType(task.getQualityType());
                        stageDTO.setQualityOrg(organizationService.getById(task.getQualityOrgId()));
                        stageDTO.setPointQualityOrg(organizationService.getById(task.getPointQualityOrgId()));
                        stageDTO.setSpecialist(specialistService.getById(task.getQualitySpecialistId()));
                        stageDTO.setStatus(task.getDistributeSpecialistStatus());
                        stageDTO.setAuditStatus(task.getAuditStatus());
                        if (sendBackStatus.contains(task.getAuditStatus())) {
                            stageDTO.setSendBack(1);
                        }
                        stageDTO.setOpinionBackTime(task.getOpinionBackTime());

                        resultList.add(stageDTO);
                    }
                }
            }
            resultVO.setWorkStageList(resultList);
            return resultVO;
        });
    }

    @Override
    @Transactional
    public void sendBack(String qualityControlTaskId) {

        QualityControlTasks controlTasks = getById(qualityControlTaskId);
        if (Objects.isNull(controlTasks)) {
            throw new BizRuntimeException("质控任务不存在");
        }

        // 查询布点任务信息
        PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, controlTasks.getBlockWorkStageId()));

        List<PointTasksRecord> pointTasksRecords = pointTasksRecordService.getBaseMapper().selectList(Wrappers.<PointTasksRecord>query()
                .eq(PointTasksRecord.BLOCK_WORK_STAGE_ID, controlTasks.getBlockWorkStageId())
                .eq(PointTasksRecord.SERVICE_LEVEL_ENGLISH, controlTasks.getQualityType()).orderByDesc(PointTasksRecord.CREATE_TIME));
        if (pointTasksRecords.size() > 0) {
            PointTasksRecord tasksRecord = pointTasksRecords.get(1);
            if (tasksRecord == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "未找到该任务阶段操作流程");
            }
            List<String> level = new ArrayList<>();
            if (controlTasks.getQualityType().equals(ServiceLevelEnum.CITY_LEVEL)) {
                level.add(ServiceLevelEnum.PROVINCE_LEVEL.name());
                level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
            } else if (controlTasks.getQualityType().equals(ServiceLevelEnum.PROVINCE_LEVEL)) {
                level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
            }
            //level size>0表示为市级或者省级。则查询是否有上级质控，没有则更新布点方案状态
            if (level.size() > 0) {
                List<QualityControlTasks> qualityControlTasks = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, controlTasks.getBlockWorkStageId())
                        .in(QualityControlTasks.QUALITY_TYPE, level));

                //没有上级质控才更新布点方案状态，否则以上级的为准
                if (qualityControlTasks.size() <= 0) {
                    pointUserTasks.setDeployPointStatus(tasksRecord.getDeployPointStatus());
                    pointUserTasksService.updateById(pointUserTasks);
                }
            } else {
                // 否则为国家级则最高级别则无需查询，直接更新布点方案状态
                pointUserTasks.setDeployPointStatus(tasksRecord.getDeployPointStatus());
                pointUserTasksService.updateById(pointUserTasks);
            }

            controlTasks.setAuditStatus(tasksRecord.getAuditStatus());
            updateById(controlTasks);
        }
    }

    @Override
    @Transactional
    public void backOrPass(FeedbackCmd query) {

        UserPrincipalDetails orThrow = UserUtils.getOrThrow();

        QualityControlTasks byId = getById(query.getQualityControlTasksId());
        if (Objects.isNull(byId)) {
            throw new BizRuntimeException("质控任务不存在");
        }

        SpecialistUser specialistUser = specialistUserService.getBaseMapper().selectOne(Wrappers.<SpecialistUser>query()
                .eq(SpecialistUser.SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(SpecialistUser.USER_ID, orThrow.getId())
                .eq(SpecialistUser.DELETED, 0));

        if (Objects.isNull(specialistUser)) {
            throw new BizRuntimeException("用户专家组信息错误");
        }

        //先逻辑删除当前用户在质控任务下对应专家组的审核信息再新增
        auditOpinionService.remove(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, byId.getId())
                .eq(AuditOpinion.QUALITY_SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.crew.name())
                .eq(AuditOpinion.SPECIALIST_USER, orThrow.getId()));

        AuditOpinion auditOpinion = new AuditOpinion();
        BeanUtils.copyProperties(query, auditOpinion);

        auditOpinion.setOpinionFileId(query.getFileId());
        auditOpinion.setQualitySpecialistId(byId.getQualitySpecialistId());
        auditOpinion.setSpecialistUser(orThrow.getId());
        auditOpinion.setBlockWorkStageId(byId.getBlockWorkStageId());
        auditOpinion.setAuditType(AuditOpinionTypeEnum.crew.name());
        auditOpinionService.save(auditOpinion);

        Long totalAuditPlan = specialistUserService.getBaseMapper().selectCount(Wrappers.<SpecialistUser>query()
                .eq(SpecialistUser.SPECIALIST_ID, byId.getQualitySpecialistId()));
        Long cyCount = auditOpinionService.getBaseMapper().selectCount(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, byId.getId())
                .eq(AuditOpinion.QUALITY_SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.crew.name())
                .eq(AuditOpinion.DELETED, 0));
        if (byId.getAuditStatus().equals(PlanAuditStatusEnum.STAY_AUDIT.getValue()) && totalAuditPlan.equals(cyCount)) {
            byId.setAuditStatus(PlanAuditStatusEnum.STAY_COLLECT.getValue());
        }
        updateById(byId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectSubmit(FeedbackCmd query) {
        UserPrincipalDetails details = UserUtils.getOrThrow();

        QualityControlTasks byId = getById(query.getQualityControlTasksId());
        if (Objects.isNull(byId)) {
            throw new BizRuntimeException("质控任务不存在");
        }

        String deployPointStatus = "";
        String auditStatus = "";

        Long totalAuditPlan = specialistUserService.getBaseMapper().selectCount(Wrappers.<SpecialistUser>query()
                .eq(SpecialistUser.SPECIALIST_ID, byId.getQualitySpecialistId()));
        Long cyCount = auditOpinionService.getBaseMapper().selectCount(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, byId.getId())
                .eq(AuditOpinion.QUALITY_SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.crew.name())
                .eq(AuditOpinion.DELETED, 0));
        if (!cyCount.equals(totalAuditPlan)) {
            throw new BizRuntimeException("还有组员未提交反馈");
        }

        SpecialistUser specialistUser = specialistUserService.getBaseMapper().selectOne(Wrappers.<SpecialistUser>query()
                .eq(SpecialistUser.SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(SpecialistUser.USER_ID, details.getId())
                .eq(SpecialistUser.DELETED, 0));

        if (Objects.isNull(specialistUser)) {
            throw new BizRuntimeException("用户专家组信息错误");
        }

        AppFile appFile = fileService.getById(query.getFileId());
        UploadFileVO uploadFileVO = new UploadFileVO();
        if (!Objects.isNull(appFile)) {
            uploadFileVO.setId(appFile.getId());
            uploadFileVO.setUrl(appFile.getPath());
        }
        if (query.getAuditOpinion().equals(FeedbackStatusEnum.pass.name())) {
            if (byId.getAuditStatus().equals(PlanAuditStatusEnum.STAY_COLLECT.getValue()) || byId.getAuditStatus().equals(PlanAuditStatusEnum.BACK_MAINTAIN.getValue())) {
                auditStatus = PlanAuditStatusEnum.PASS.getValue();
                byId.setAuditStatus(PlanAuditStatusEnum.PASS.getValue());
            } else if (byId.getAuditStatus().equals(PlanAuditStatusEnum.PERFECT_REVIEW.getValue())) {
                auditStatus = PlanAuditStatusEnum.PERFECT_REVIEW_PASS.getValue();
                byId.setAuditStatus(PlanAuditStatusEnum.PERFECT_REVIEW_PASS.getValue());
            } else if (byId.getAuditStatus().equals(PlanAuditStatusEnum.RETRIAL_REVIEW.getValue())) {
                auditStatus = PlanAuditStatusEnum.RETRIAL_REVIEW_PASS.getValue();
                byId.setAuditStatus(PlanAuditStatusEnum.RETRIAL_REVIEW_PASS.getValue());
            }
        } else if (query.getAuditOpinion().equals(FeedbackStatusEnum.back_perfect.name())) {
            auditStatus = PlanAuditStatusEnum.BACK_PERFECT.getValue();
            byId.setAuditStatus(PlanAuditStatusEnum.BACK_PERFECT.getValue());
        } else {
            auditStatus = PlanAuditStatusEnum.BACK_RETRIAL.getValue();
            byId.setAuditStatus(PlanAuditStatusEnum.BACK_RETRIAL.getValue());
        }
//        byId.setOpinionBackTime(DateTimeUtils.asLocalDateTime(new Date()));
//        byId.setOpinionBackUserId(details.getId());
        updateById(byId);

        List<String> level = new ArrayList<>();
        if (byId.getQualityType().equals(ServiceLevelEnum.CITY_LEVEL)) {
            level.add(ServiceLevelEnum.PROVINCE_LEVEL.name());
            level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
        } else if (byId.getQualityType().equals(ServiceLevelEnum.PROVINCE_LEVEL)) {
            level.add(ServiceLevelEnum.COUNTRY_LEVEL.name());
        }
        //level size>0表示为市级或者省级。则查询是否有上级质控，没有则更新布点方案状态
        if (level.size() > 0) {
            List<QualityControlTasks> qualityControlTasks = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                    .in(QualityControlTasks.QUALITY_TYPE, level));

            if (qualityControlTasks.size() <= 0) {
                PointUserTasks pointUserTasks = pointUserTasksService.getBaseMapper().selectOne(Wrappers.<PointUserTasks>query()
                        .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                        .eq(PointUserTasks.DELETED, 0));
                deployPointStatus = pointUserTasks.getDeployPointStatus();
                if (query.getAuditOpinion().equals(FeedbackStatusEnum.back_perfect.name())) {
                    deployPointStatus = PointStatusEnum.back_perfect.getValue();
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.back_perfect.getValue());
                } else if (query.getAuditOpinion().equals(FeedbackStatusEnum.back_retrial.name())) {
                    deployPointStatus = PointStatusEnum.back_retrial.getValue();
                    pointUserTasks.setDeployPointStatus(PointStatusEnum.back_retrial.getValue());
                }
                pointUserTasksService.updateById(pointUserTasks);
            }
        } else {
            // 否则为国家级则最高级别则无需查询，直接更新布点方案状态
            PointUserTasks pointUserTasks = pointUserTasksService.getBaseMapper().selectOne(Wrappers.<PointUserTasks>query()
                    .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                    .eq(PointUserTasks.DELETED, 0));
            if (query.getAuditOpinion().equals(FeedbackStatusEnum.back_perfect.name())) {
                deployPointStatus = PointStatusEnum.back_perfect.getValue();
                pointUserTasks.setDeployPointStatus(PointStatusEnum.back_perfect.getValue());
            } else if (query.getAuditOpinion().equals(FeedbackStatusEnum.back_retrial.name())) {
                deployPointStatus = PointStatusEnum.back_retrial.getValue();
                pointUserTasks.setDeployPointStatus(PointStatusEnum.back_retrial.getValue());
            }
            pointUserTasksService.updateById(pointUserTasks);
        }

        //先逻辑删除当前用户在质控任务下对应专家组的审核信息再新增
        auditOpinionService.remove(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, byId.getId())
                .eq(AuditOpinion.QUALITY_SPECIALIST_ID, byId.getQualitySpecialistId())
                .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.collect.name())
                .eq(AuditOpinion.SPECIALIST_USER, details.getId()));

        AuditOpinion auditOpinion = new AuditOpinion();
        BeanUtils.copyProperties(query, auditOpinion);
        auditOpinion.setOpinionFileId(query.getFileId());
        auditOpinion.setQualitySpecialistId(byId.getQualitySpecialistId());
        auditOpinion.setSpecialistUser(details.getId());
        auditOpinion.setBlockWorkStageId(byId.getBlockWorkStageId());
        auditOpinion.setAuditType(AuditOpinionTypeEnum.collect.name());
        auditOpinionService.save(auditOpinion);

        Organization organization = organizationService.getById(details.getOrgId());
        // 将文件信息记录到json中
        Map<String, Object> hashMap = new HashMap<String, Object>() {{
            put("opinionFile", uploadFileVO);
            put("auditOpinion", query.getAuditOpinion());
            put("opinionDesc", query.getOpinionDesc());
        }};

        pointTasksRecordService.save(new PointTasksRecord(byId.getBlockWorkStageId(), details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.AUDIT_TASK.getDesc(), OperateTypeEnum.POINT_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                JSONObject.toJSONString(hashMap),deployPointStatus,auditStatus
        ));

        String serviceOrgId = "";
        String serviceOrgName = "";
        // 获取创建布点任务的人员所在的单位
        SurveyTasks idAndType = surveyTasksService.
                getByBlockWorkStageIdAndType(byId.getBlockWorkStageId(), OrganizationBizTypeEnum.LAYOUT.getValue());
        if (idAndType != null) {

            Organization organizationA = organizationService
                    .getById(userService.getById(idAndType.getCreateBy()).getOrgId());
            serviceOrgName = organizationA.getName();
            serviceOrgId = organizationA.getId();
        }
        pointAuditRecordService.save(
                new PointAuditRecord(byId.getBlockWorkStageId(),
                        details.getOrgId(), details.getId(),
                        organization.getName(), details.getNickName(),
                        OperateTypeEnum.POINT_CONTROL_TASK.getDesc(),
                        ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                        serviceOrgId, serviceOrgName,
                        OperateItemsEnum.AUDIT_TASK.getDesc(),
                        JSONObject.toJSONString(hashMap))
        );
    }

    @Override
    public OpinionSummaryVO look(String qualityControlTasksId) {
        QualityControlTasks byId = getById(qualityControlTasksId);
        if (Objects.isNull(byId)) {
            throw new BizRuntimeException("质控任务不存在");
        }

        List<AuditOpinion> auditOpinions = auditOpinionService.getBaseMapper().selectList(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.QUALITY_CONTROL_TASKS_ID, byId.getId())
                .eq(AuditOpinion.QUALITY_SPECIALIST_ID, byId.getQualitySpecialistId()).eq(AuditOpinion.DELETED, 0));

        Specialist specialist = specialistService.getById(byId.getQualitySpecialistId());
        if (Objects.isNull(specialist)) {
            throw new BizRuntimeException("专家组信息错误");
        }

        OpinionSummaryVO vo = new OpinionSummaryVO();
        vo.setSpecialistName(specialist.getGroupName());
        List<OpinionSummaryVO.SpecialistUserAuditInfo> infoList = new ArrayList<>();
        auditOpinions.stream().forEach(t -> {
            SpecialistUser specialistUser = specialistUserService.getBaseMapper().selectOne(Wrappers.<SpecialistUser>query()
                    .eq(SpecialistUser.SPECIALIST_ID, t.getQualitySpecialistId())
                    .eq(SpecialistUser.USER_ID, t.getSpecialistUser())
                    .eq(SpecialistUser.DELETED, 0));
            OpinionSummaryVO.SpecialistUserAuditInfo info = new OpinionSummaryVO.SpecialistUserAuditInfo();
            BeanUtils.copyProperties(specialistUser, info);
            info.setNatureDesc(SpecialistNatureEnum.getDescByValue(specialistUser.getNature()));
            info.setSpecialistIdentityDesc(SpecialistIdentityEnum.getDescByValue(specialistUser.getSpecialistIdentity()));
            info.setOrg(organizationService.getDetail(String.valueOf(specialistUser.getOrgId())));
            info.setUser(userService.getUserDetail(specialistUser.getUserId()));

            BeanUtils.copyProperties(t, info);
            info.setAuditOpinionDesc(FeedbackStatusEnum.valueOf(t.getAuditOpinion()).getValue());

            AppFile appFile = fileService.getById(t.getOpinionFileId());
            if (appFile != null) {
                info.setOpinionFile(appFile.getPath());
            }

            infoList.add(info);
        });
        vo.setAuditInfos(infoList);

        UserPrincipalDetails details = UserUtils.getOrThrow();
        AuditOpinion auditOpinion = auditOpinionService.getBaseMapper().selectOne(Wrappers.<AuditOpinion>query()
                .eq(AuditOpinion.BLOCK_WORK_STAGE_ID, byId.getBlockWorkStageId())
                .eq(AuditOpinion.SPECIALIST_USER, details.getId())
                .eq(AuditOpinion.AUDIT_TYPE, AuditOpinionTypeEnum.collect.name())
                .eq(AuditOpinion.DELETED, 0));
        if (!Objects.isNull(auditOpinion)) {
            AuditOpinionVO auditOpinionVO = new AuditOpinionVO();
            BeanUtils.copyProperties(auditOpinion, auditOpinionVO);
            auditOpinionVO.setAuditOpinionDesc(FeedbackStatusEnum.valueOf(auditOpinion.getAuditOpinion()).getValue());

            AppFile appFile = fileService.getById(auditOpinion.getOpinionFileId());
            if (appFile != null) {
                auditOpinionVO.setOpinionFile(appFile.getPath());
            }
            vo.setAuditOpinionVO(auditOpinionVO);
        }
        return vo;
    }

    /**
     * 布点质控单位任务-选择布点质控单位-分页
     *
     * @param qualityControlOrgList
     * @return
     */
    @Override
    public IPage<QualityControlOrgResultVO> orgList(QualityControlOrgList qualityControlOrgList) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        // 当前用户单位
        Organization organization = organizationService.getById(details.getOrgId());
        List<String> bizTypes = organizationBizTypeService.getBizTypes(organization.getId());

        // 服务级别 如果是当前用户属于技术管理单位找到上级单位的服务级别
        String serviceLevel = "";
        if (bizTypes.contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            serviceLevel = organization.getServiceLevel();
            qualityControlOrgList.setOwnerId(details.getOrgId());
        } else if (bizTypes.contains(OrganizationBizTypeEnum.TECHNOLOGY_MANAGEMENT.getValue())) {
            if (StringUtils.isEmpty(qualityControlOrgList.getOwnerId())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "归属单位id不能为空");
            }
            Organization ownerOrganization = organizationService.getById(qualityControlOrgList.getOwnerId());
            serviceLevel = ownerOrganization.getServiceLevel();
        }
        qualityControlOrgList.setServiceLevel(serviceLevel);
        String finalServiceLevel = serviceLevel;
        return organizationService.qualityControlOrgPage(qualityControlOrgList).convert(org -> {
            QualityControlOrgResultVO resultVO = new QualityControlOrgResultVO();
            BeanCopyUtils.copyNonNullProperties(org, resultVO);
            Organization ownerOrganizationA = organizationService.getById(qualityControlOrgList.getOwnerId());
            if (ownerOrganizationA != null) {
                resultVO.setOwnerOrgName(ownerOrganizationA.getName());
            }
            resultVO.setServiceLevel(finalServiceLevel);
            UserDTO organizationAdmin = userService.getOrganizationAdmin(org.getId());
            if (organizationAdmin != null) {
                resultVO.setPrincipal(organizationAdmin.getId());
                resultVO.setPrincipalName(organizationAdmin.getNickName());
                resultVO.setPrincipalPhone(organizationAdmin.getMobile());
            }
            // 分配任务总数
            Long totalCount = getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                    .eq(QualityControlTasks::getQualityOrgId, details.getOrgId())
                    .eq(QualityControlTasks::getPointQualityOrgId, org.getId())
                    .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
            );
            // 未完成任务数
            Long unCompleteCount = getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                    .eq(QualityControlTasks::getQualityOrgId, details.getOrgId())
                    .eq(QualityControlTasks::getPointQualityOrgId, org.getId())
                    .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                    .in(QualityControlTasks::getAuditStatus, unComplateStatus)
            );
            // 最晚任务期限
            List<QualityControlTasks> list = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                    .eq(QualityControlTasks.QUALITY_ORG_ID, details.getOrgId())
                    .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, org.getId())
                    .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                    .orderByDesc(QualityControlTasks.DEADLINE)
                    .last("limit 1")
            );
            if (list != null && !list.isEmpty()) {
                resultVO.setDeadLine(list.get(0).getDeadline() + "");
            }
            resultVO.setTotalCount(totalCount);
            resultVO.setUnCompleteCount(unCompleteCount);
            return resultVO;
        });
    }

    /**
     * 布点质控单位任务-撤回单位任务-布点质控单位信息
     *
     * @param qualityControlTasksId
     * @return
     */
    @Override
    public QualityControlOrgResultVO orgInfo(String qualityControlTasksId) {
        String userOrgId = UserUtils.getUserOrgId();
        QualityControlTasks byId = getById(qualityControlTasksId);
        if (byId == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到该布点质控任务");
        }
        if (!SurveyTaskStatusEnum.ALLOCATED.name().equals(byId.getDistributeStatus())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前状态为" + byId.getDistributeStatus() + ",不可撤回");
        }
        // 布点质控单位id
        String pointQualityOrgId = byId.getPointQualityOrgId();
        Organization pointQualityOrg = organizationService.getById(pointQualityOrgId);
        QualityControlOrgResultVO resultVO = new QualityControlOrgResultVO();
        BeanCopyUtils.copyNonNullProperties(pointQualityOrg, resultVO);
        resultVO.setId(pointQualityOrgId);

        UserDTO organizationAdmin = userService.getOrganizationAdmin(pointQualityOrgId);
        if (organizationAdmin != null) {
            resultVO.setPrincipal(organizationAdmin.getOrgId());
            resultVO.setPrincipalPhone(organizationAdmin.getMobile());
            resultVO.setPrincipalName(organizationAdmin.getNickName());
        }
        // 分配任务总数
        Long totalCount = getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                .eq(QualityControlTasks::getQualityOrgId, userOrgId)
                .eq(QualityControlTasks::getPointQualityOrgId, pointQualityOrgId)
                .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
        );
        // 未完成任务数
        Long unCompleteCount = getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                .eq(QualityControlTasks::getQualityOrgId, userOrgId)
                .eq(QualityControlTasks::getPointQualityOrgId, pointQualityOrgId)
                .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                .in(QualityControlTasks::getAuditStatus, unComplateStatus)
        );
        // 最晚任务期限
        List<QualityControlTasks> list = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                .eq(QualityControlTasks.QUALITY_ORG_ID, userOrgId)
                .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, pointQualityOrgId)
                .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                .orderByDesc(QualityControlTasks.DEADLINE)
                .last("limit 1")
        );
        if (list != null && !list.isEmpty()) {
            resultVO.setDeadLine(list.get(0).getDeadline() + "");
        }
        resultVO.setTotalCount(totalCount);
        resultVO.setUnCompleteCount(unCompleteCount);
        return resultVO;
    }

    /**
     * @return
     */
    @Override
    public IPage<SpecialistResultVO> pointSpecialistTaskPage(PointSpecialistPageQuery query) {
        return specialistMapper.pointSpecialistTaskPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(specialist -> {
                    SpecialistResultVO resultVO = new SpecialistResultVO();
                    BeanCopyUtils.copyNonNullProperties(specialist, resultVO);
                    // 分配任务总数
                    Long totalCount = getBaseMapper().selectCount(
                            Wrappers.<QualityControlTasks>lambdaQuery()
                                    .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                                    .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                    );
                    // 未完成任务数
                    Long unCompleteCount = getBaseMapper().selectCount(
                            Wrappers.<QualityControlTasks>lambdaQuery()
                                    .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                                    .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .in(QualityControlTasks::getAuditStatus, unComplateStatus)
                    );
                    // 最晚任务期限
                    List<QualityControlTasks> list = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.QUALITY_SPECIALIST_ID, specialist.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .orderByDesc(QualityControlTasks.DEADLINE)
                            .last("limit 1")
                    );
                    if (list != null && !list.isEmpty()) {
                        resultVO.setDeadLine(list.get(0).getDeadline() + "");
                    }
                    resultVO.setUnCompleteCount(unCompleteCount);
                    resultVO.setTotalCount(totalCount);
                    resultVO.setStatus(specialist.getStatus());
                    resultVO.setStatusDesc(SpecialistStatusEnum.getDescByName(specialist.getStatus()));
                    return resultVO;
                });
    }

    /**
     * 布点质控专家组任务-已选布点质控专家组信息
     *
     * @param qualityControlTasksId
     * @return
     */
    @Override
    public SpecialistResultVO selectSpecialist(String qualityControlTasksId) {
        QualityControlTasks byId = getById(qualityControlTasksId);
        if (byId == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到该布点质控任务");
        }
        Specialist specialist = specialistService.getById(byId.getQualitySpecialistId());
        if (specialist == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到该布点质控任务的专家组信息");
        }
        SpecialistResultVO resultVO = new SpecialistResultVO();
        BeanCopyUtils.copyNonNullProperties(specialist, resultVO);
        // 分配任务总数
        Long totalCount = getBaseMapper().selectCount(
                Wrappers.<QualityControlTasks>lambdaQuery()
                        .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                        .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
        );
        // 未完成任务数
        Long unCompleteCount = getBaseMapper().selectCount(
                Wrappers.<QualityControlTasks>lambdaQuery()
                        .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                        .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                        .in(QualityControlTasks::getAuditStatus, unComplateStatus)
        );
        // 最晚任务期限
        List<QualityControlTasks> list = getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                .eq(QualityControlTasks.QUALITY_SPECIALIST_ID, specialist.getId())
                .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                .orderByDesc(QualityControlTasks.DEADLINE)
                .last("limit 1")
        );
        if (list != null && !list.isEmpty()) {
            resultVO.setDeadLine(list.get(0).getDeadline() + "");
        }
        resultVO.setUnCompleteCount(unCompleteCount);
        resultVO.setTotalCount(totalCount);
        return resultVO;
    }

    /**
     * 布点质控专家组任务-待分配布点质控任务
     *
     * @param query
     * @return
     */
    @Override
    public List<QualityControlWorkStageVO> waitQualityControlTaskList(WaitQualityControlTaskQuery query) {
        List<QualityControlWorkStageVO> list = new ArrayList<>();
        for (String qualityControlTasksId : query.getQualityControlTasksIds()) {
            QualityControlTasks byId = getById(qualityControlTasksId);
            if (byId == null) {
                continue;
            }
            String workStageId = byId.getBlockWorkStageId();
            QualityControlWorkStageVO distributeVO = new QualityControlWorkStageVO();
            distributeVO.setQualityType(byId.getQualityType());
            distributeVO.setQualityTypeDesc(ServiceLevelEnum.getValueByName(byId.getQualityType()));
            distributeVO.setQualityOrg(organizationService.getById(byId.getQualityOrgId()));
            // 查询地块工作阶段
            BlockWorkStage blockWorkStage = blockWorkStageService.getById(workStageId);
            if (blockWorkStage != null) {
                // 查询地块
                Block block = blockService.getById(blockWorkStage.getBlockId());
                Optional.ofNullable(block).ifPresent(a -> {
                    distributeVO.setBlockName(a.getName());
                    distributeVO.setCode(a.getCode());
                    distributeVO.setEnterprise(enterpriseService.getDetailById(a.getEnterpriseId()));
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
     * 采样过程点位退回-列表分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<QualityControlSpecialistTaskResultVO> backPage(QualityControlTasksQuery query) {
        String userOrgId = UserUtils.getUserOrgId();
        query.setPointQualityOrgId(userOrgId);
        query.setQuailtyBack(true);
        IPage<BlockVO> blockIPage = blockService.specialistPage(query);
        AtomicReference<String> blockService = new AtomicReference<>("");
        return blockIPage.convert(blockVO -> {
            QualityControlSpecialistTaskResultVO resultVO = new QualityControlSpecialistTaskResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);

            // 地块管理单位
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                OrganizationDTO blockOrg = organizationService.getDetail(resultVO.getProject().getOrgId());
                blockService.set(blockOrg.getServiceLevel());
                resultVO.setOrganization(blockOrg);
            }

            // 技术管理单位
            List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(resultVO.getProject().getOrgId(),
                    blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
            if (orgIdList.size() > 0) {
                resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
            }

            // 总数
            Long totalCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                    );
            // 已退回数量
            Long yfpCount = getBaseMapper()
                    .selectCount(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                            .eq(QualityControlTasks.AUDIT_STATUS, PlanAuditStatusEnum.BACK_MAINTAIN.getValue())
                    );
            yfpCount = yfpCount == null ? 0 : yfpCount;
            resultVO.setTaskPlan(yfpCount + "/" + totalCount);
            // 设置颜色
            if (yfpCount == 0) {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.RED.name());
            } else {
                resultVO.setTaskPlanColor(TaskPlanColorEnum.ORANGE.name());
            }
            List<BlockWorkStageDTO> workStageList = blockVO.getWorkStageList();
            List<QualityControlSpecialistBlockWorkStageDTO> resultList = new ArrayList<>();
            if (workStageList != null) {
                for (BlockWorkStageDTO workStageDTO : workStageList) {
                    // 地块管理单位（或技术管理单位）分配的同级的布点质控单位
                    List<QualityControlTasks> qualityControlTasksA = getBaseMapper()
                            .selectList(Wrappers.<QualityControlTasks>query()
                                    .eq(QualityControlTasks.BLOCK_ID, blockVO.getId())
                                    .eq(QualityControlTasks.POINT_QUALITY_ORG_ID, userOrgId)
                                    .eq(QualityControlTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .eq(QualityControlTasks.QUALITY_TYPE, blockService.get())
                                    .in(QualityControlTasks.AUDIT_STATUS, Arrays.asList(
                                            PlanAuditStatusEnum.CROSS.getValue(), PlanAuditStatusEnum.STAY_AUDIT.getValue(),
                                            PlanAuditStatusEnum.STAY_COLLECT.getValue(), PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
                                            PlanAuditStatusEnum.RETRIAL_REVIEW.getValue(), PlanAuditStatusEnum.BACK_MAINTAIN.getValue()
                                    ))
                            );
                    if (qualityControlTasksA == null || qualityControlTasksA.isEmpty()) {
                        continue;
                    }

                    List<SurveyTasks> surveyTaskList = surveyTasksService.list(
                            Wrappers.<SurveyTasks>query()
                                    .eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId())
                                    .in(SurveyTasks.TYPE, OrganizationBizTypeEnum.LEAD.getValue()
                                            , OrganizationBizTypeEnum.LAYOUT.getValue())
                    );

                    // 任务分配单位
                    List<TasksDTO> tasksDTOS = surveyTaskList.stream().map(task -> {
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
                    }).collect(Collectors.toList());

                    // 查询各样点类型数量
                    PointCountDTO pointCountDTO = pointService.groupByPointType(workStageDTO.getId());

                    // 查询布点任务信息
                    PointUserTasks pointUserTasks = pointUserTasksService.getOne(Wrappers.<PointUserTasks>query()
                            .eq(PointUserTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));

                    for (QualityControlTasks task : qualityControlTasksA) {
                        QualityControlSpecialistBlockWorkStageDTO stageDTO = new QualityControlSpecialistBlockWorkStageDTO();
                        BeanCopyUtils.copyNonNullProperties(workStageDTO, stageDTO);
                        stageDTO.setQualityControlTasksId(task.getId());
                        stageDTO.setTasksList(tasksDTOS);
                        stageDTO.setSoilCount(pointCountDTO.getSoilCount());
                        stageDTO.setWaterCount(pointCountDTO.getWaterCount());
                        stageDTO.setSoleWaterCount(pointCountDTO.getSoleWaterCount());
                        stageDTO.setDeployPointStatus(pointUserTasks.getDeployPointStatus());
                        stageDTO.setQualityType(task.getQualityType());
                        stageDTO.setQualityTypeDesc(ServiceLevelEnum.getValueByName(task.getQualityType()));
                        stageDTO.setQualityOrg(organizationService.getById(task.getQualityOrgId()));
                        stageDTO.setSpecialist(specialistService.getById(task.getQualitySpecialistId()));
                        stageDTO.setAuditStatus(task.getAuditStatus());
                        stageDTO.setAuthChangeTime(task.getAuthChangeTime());
                        resultList.add(stageDTO);
                    }
                }
            }
            resultVO.setWorkStageList(resultList);
            return resultVO;
        });
    }

    /**
     * 采样过程点位退回-授权调整
     *
     * @param cmd
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pointBackCreate(QualityControlBackCmd cmd) {
        String qualityControlTasksId = cmd.getQualityControlTasksId();
        String qualitySpecialistId = cmd.getQualitySpecialistId();

        QualityControlTasks byId = getById(qualityControlTasksId);
        if (byId == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "无法找到该质控任务");
        }
        String blockWorkStageId = byId.getBlockWorkStageId();
        int value = getBaseMapper().selectCount(
                Wrappers.<QualityControlTasks>lambdaQuery()
                        .eq(QualityControlTasks::getBlockWorkStageId, blockWorkStageId)
                        .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.ALLOCATED.name())
        ).intValue();
        if (value > 1) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该地块工作阶段下存在多级指控，不可退回维护");
        }
        if (StringUtils.isNotEmpty(qualitySpecialistId)) {
            byId.setQualitySpecialistId(qualitySpecialistId);
        }
        byId.setAuditStatus(PlanAuditStatusEnum.BACK_MAINTAIN.getValue());
        byId.setDistributeSpecialistStatus(SurveyTaskStatusEnum.ALLOCATED.name());
        updateById(byId);

        // 更新布点方案状态
        pointUserTasksService.update(
                Wrappers.<PointUserTasks>lambdaUpdate()
                        .set(PointUserTasks::getDeployPointStatus, PointStatusEnum.back_maintain.getValue())
                        .eq(PointUserTasks::getBlockWorkStageId, blockWorkStageId)
        );
    }
}
