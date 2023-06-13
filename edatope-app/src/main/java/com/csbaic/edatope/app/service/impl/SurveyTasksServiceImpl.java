package com.csbaic.edatope.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.entity.*;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.mapper.SurveyTasksMapper;
import com.csbaic.edatope.app.model.command.DeleteSurveyTasksCmd;
import com.csbaic.edatope.app.model.command.SurveyTasksCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.OrgListAllQuery;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 * 调查任务分配 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-04-18
 */
@Service
public class SurveyTasksServiceImpl extends ServiceImpl<SurveyTasksMapper, SurveyTasks> implements ISurveyTasksService {

    @Autowired
    private ISurveyTasksRecordService surveyTasksRecordService;

    @Autowired
    private IBlockWorkStageService blockWorkStageService;

    @Autowired
    private IBlockService blockService;

    @Autowired
    private IWorkStageService workStageService;

    @Autowired
    private IUserService userService;

    @Autowired
    @Lazy
    private IOrganizationService organizationService;

    @Autowired
    private ITechOrganizationAuthorizeService techOrganizationAuthorizeService;

    @Autowired
    private ITechOrganizationAuthorizeCityService techOrganizationAuthorizeCityService;

    @Autowired
    private IPointTasksRecordService pointTasksRecordService;

    @Autowired
    @Lazy
    private IDictService dictService;

    @Autowired
    @Lazy
    private IQualityControlTasksService qualityControlTasksService;

    @Override
    @Transactional
    public void create(SurveyTasksCmd cmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();

        Organization organization = organizationService.getById(details.getOrgId());

        BlockWorkStage blockWorkStage = blockWorkStageService.getById(cmd.getBlockWorkStageId());
        if (Objects.isNull(blockWorkStage)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到地块工作阶段信息");
        }

//        remove(Wrappers.<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, blockWorkStage.getId()));
        cmd.getTasksList().stream().forEach(t -> {
            SurveyTasks tasks = new SurveyTasks();
            if (StringUtils.isNotEmpty(t.getId())) {
                tasks = getById(t.getId());
            }
            BeanUtils.copyProperties(t, tasks);
            tasks.setBlockWorkStageId(cmd.getBlockWorkStageId());
            tasks.setStatus(SurveyTaskStatusEnum.ALLOCATED.name());
            tasks.setBlockId(blockWorkStage.getBlockId());
            if (StringUtils.isNotEmpty(t.getId())) {
                updateById(tasks);
            } else {
                save(tasks);
            }

            if (OrganizationBizTypeEnum.LAYOUT_QUALITY.getValue().equals(t.getType())) {
                QualityControlTasks qualityControl = qualityControlTasksService.getOne(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getBlockWorkStageId, cmd.getBlockWorkStageId())
                                .eq(QualityControlTasks::getQualityType, organization.getServiceLevel())
                                .eq(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.RECALL.name())
                );
                QualityControlTasks qualityControlTasks = new QualityControlTasks();
                if (qualityControl != null) {
                    qualityControlTasks = qualityControl;
                }
                qualityControlTasks.setBlockId(blockWorkStage.getBlockId());
                qualityControlTasks.setBlockWorkStageId(cmd.getBlockWorkStageId());
                qualityControlTasks.setPointQualityOrgId(t.getOrgId());
                qualityControlTasks.setPrincipal(t.getPrincipal());
                qualityControlTasks.setPrincipalPhone(t.getPrincipalPhone());
                qualityControlTasks.setQualityOrgId(details.getOrgId());
                qualityControlTasks.setQualityType(organization.getServiceLevel());
                qualityControlTasks.setDeadline(t.getDeadline());
                qualityControlTasks.setDistributeStatus(SurveyTaskStatusEnum.ALLOCATED.name());
                qualityControlTasks.setAuditStatus(PlanAuditStatusEnum.CROSS.getValue());
                if (qualityControl != null) {
                    qualityControlTasksService.updateById(qualityControlTasks);
                } else {
                    qualityControlTasksService.save(qualityControlTasks);
                }
            }
        });

        blockWorkStage.setEntrustWay(cmd.getEntrustWay());
        blockWorkStageService.updateById(blockWorkStage);

        SurveyTasksRecord tasksRecord = new SurveyTasksRecord();
        tasksRecord.setBlockWorkStageId(cmd.getBlockWorkStageId());
        tasksRecord.setEntrustWay(cmd.getEntrustWay());
        tasksRecord.setOrgId(details.getOrgId());
        tasksRecord.setUserId(details.getId());
//        tasksRecord.setBusinessOrg(tasks.getOrgId());
//        tasksRecord.setType(tasks.getType());
        tasksRecord.setStatus(SurveyTaskRecordTypeEnum.ALLOT.name());
        surveyTasksRecordService.save(tasksRecord);

        Map<String, String> hashMap = new HashMap<String, String>() {{
            put("entrustWay", dictService.getDictByValue("entrust_way", cmd.getEntrustWay()).getName());
        }};

        // 记录操作记录
        pointTasksRecordService.save(new PointTasksRecord(cmd.getBlockWorkStageId(),
                details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.RECALL_TASK.getDesc(), OperateTypeEnum.SURVEY_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                JSONObject.toJSONString(hashMap),null,null
        ));
    }

    @Override
    @Transactional
    public void delete(DeleteSurveyTasksCmd cmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        Organization organization = organizationService.getById(details.getOrgId());

        SurveyTasks tasks = getById(cmd.getId());
        if (tasks == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到分配任务");
        }

        BlockWorkStage byId = blockWorkStageService.getById(tasks.getBlockWorkStageId());
        SurveyTasksRecord tasksRecord = new SurveyTasksRecord();
        tasksRecord.setBlockWorkStageId(tasks.getBlockWorkStageId());
        tasksRecord.setEntrustWay(byId.getEntrustWay());
        tasksRecord.setOrgId(details.getOrgId());
        tasksRecord.setUserId(details.getId());
        tasksRecord.setBusinessOrg(tasks.getOrgId());
        tasksRecord.setType(tasks.getType());
        tasksRecord.setStatus(SurveyTaskRecordTypeEnum.RECALL.name());
        surveyTasksRecordService.save(tasksRecord);


        tasks.setStatus(SurveyTaskStatusEnum.RECALL.name());
        tasks.setOrgId("");
        tasks.setPrincipal("");
        tasks.setPrincipalPhone("");
//        tasks.setDeadline(null);
        updateById(tasks);
//        removeById(tasks);

        Map<String, String> hashMap = new HashMap<String, String>() {{
            put("entrustWay", dictService.getDictByValue("entrust_way", tasksRecord.getEntrustWay()).getName());
        }};

        if (OrganizationBizTypeEnum.LAYOUT_QUALITY.getValue().equals(tasks.getType())) {
            qualityControlTasksService.update(
                    Wrappers.<QualityControlTasks>lambdaUpdate()
                            .set(QualityControlTasks::getDistributeStatus, SurveyTaskStatusEnum.RECALL.name())
                            .set(QualityControlTasks::getDeadline, null)
                            .set(QualityControlTasks::getPrincipal, null)
                            .set(QualityControlTasks::getPrincipalPhone, null)
                            .eq(QualityControlTasks::getBlockWorkStageId, tasks.getBlockWorkStageId())
                            .eq(QualityControlTasks::getPointQualityOrgId, tasks.getOrgId())
                            .eq(QualityControlTasks::getPointQualityOrgId, tasks.getOrgId())
                            .eq(QualityControlTasks::getQualityOrgId, details.getOrgId())
                            .eq(QualityControlTasks::getQualityType, organization.getServiceLevel())
            );
        }

        // 记录操作记录
        pointTasksRecordService.save(new PointTasksRecord(tasks.getBlockWorkStageId(),
                details.getOrgId(), details.getId(),
                organization.getName(), details.getNickName(),
                OperateItemsEnum.DISTRIBUTE_TASK.getDesc(), OperateTypeEnum.SURVEY_TASK.getDesc(),
                ServiceLevelEnum.getValueByName(organization.getServiceLevel()),
                JSONObject.toJSONString(hashMap),null,null
        ));
    }

    @Override
    public SurveyTasksAllotRecordDTO listRecord(String id) {
        SurveyTasksAllotRecordDTO dto = new SurveyTasksAllotRecordDTO();

        BlockWorkStage blockWorkStage = blockWorkStageService.getById(id);
        if (Objects.isNull(blockWorkStage)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到相关信息");
        }
        dto.setBlockStageName(blockWorkStage.getName());
        dto.setDeadline(blockWorkStage.getDeadline());

        BlockVO blockVO = blockService.getDetailById(blockWorkStage.getBlockId());
        dto.setBlockId(blockVO.getId());
        dto.setCode(blockVO.getCode());
        dto.setName(blockVO.getName());
        dto.setProject(blockVO.getProject());
        dto.setEnterprise(blockVO.getEnterprise());

        WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
        dto.setWorkStageId(workStage.getId());
        dto.setWorkStageName(workStage.getName());

        dto.setRecordList(surveyTasksRecordService.getList(blockWorkStage.getId()));
        return dto;
    }

    @Override
    public SurveyTasksAllotRecordDTO get(String id) {
        SurveyTasksAllotRecordDTO dto = new SurveyTasksAllotRecordDTO();

        BlockWorkStage blockWorkStage = blockWorkStageService.getById(id);
        if (Objects.isNull(blockWorkStage)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到相关信息");
        }
        dto.setBlockStageName(blockWorkStage.getName());
        dto.setDeadline(blockWorkStage.getDeadline());

        BlockVO blockVO = blockService.getDetailById(blockWorkStage.getBlockId());
        dto.setBlockId(blockVO.getId());
        dto.setCode(blockVO.getCode());
        dto.setName(blockVO.getName());
        dto.setProject(blockVO.getProject());
        dto.setEnterprise(blockVO.getEnterprise());

        WorkStage workStage = workStageService.getById(blockWorkStage.getWorkStageId());
        dto.setWorkStageId(workStage.getId());
        dto.setWorkStageName(workStage.getName());

        List<SurveyTasks> surveyTasks = getBaseMapper().selectList(Wrappers.<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, blockWorkStage.getId()).eq(SurveyTasks.DELETED, 0));
        dto.setTasksList(surveyTasks.stream().map(t -> {
            TasksDTO tasksDTO = new TasksDTO();
            BeanCopyUtils.copyNonNullProperties(t, tasksDTO);
            if (StringUtils.isNotEmpty(t.getOrgId())) {
                tasksDTO.setOrgId(organizationService.getDetail(t.getOrgId()));
            }
            if (StringUtils.isNotEmpty(t.getPrincipal())) {
                UserDTO userDetail = userService.getUserDetail(t.getPrincipal());
                tasksDTO.setPrincipal(userDetail.getId());
                tasksDTO.setPrincipalName(userDetail.getUsername());
                tasksDTO.setPrincipalPhone(userDetail.getMobile());
            }
            return tasksDTO;
        }).collect(Collectors.toList()));

        return dto;
    }

    @Override
    public IPage<BlockWorkStageQueryResultVO> listPage(BlockQuery query) {
        IPage<BlockVO> page = blockService.listPage(query);
        return page.convert(blockVO -> {
            BlockWorkStageQueryResultVO resultVO = new BlockWorkStageQueryResultVO();
            BeanCopyUtils.copyNonNullProperties(blockVO, resultVO);
            if (resultVO.getProject() != null && resultVO.getProject().getOrgId() != null) {
                resultVO.setOrganization(organizationService.getDetail(resultVO.getProject().getOrgId()));
            }


            List<String> orgIdList = techOrganizationAuthorizeService.getOrgIdByCityAndOwnId(resultVO.getProject().getOrgId(),
                    blockVO.getProvinceCode(), blockVO.getCityCode(), blockVO.getDistrictCode());
            if (orgIdList.size() > 0) {
                resultVO.setTechnicalOrg(organizationService.getDetail(orgIdList.get(0)));
            }

            /*List<TechOrganizationAuthorizeCity> authorizeCityList = techOrganizationAuthorizeCityService.list(Wrappers.<TechOrganizationAuthorizeCity>query()
                    .eq(TechOrganizationAuthorizeCity.PROVINCE_CODE, blockVO.getProvinceCode())
                    .eq(TechOrganizationAuthorizeCity.CITY_CODE, blockVO.getCityCode())
                    .eq(TechOrganizationAuthorizeCity.DISTRICT_CODE, blockVO.getDistrictCode())
                    .eq(TechOrganizationAuthorizeCity.DELETED, 0)
            );
            if (authorizeCityList.size() > 0) {
                TechOrganizationAuthorize authorize = techOrganizationAuthorizeService.getById(authorizeCityList.get(0).getAuthorizeId());
                resultVO.setTechnicalOrg(organizationService.getDetail(authorize.getOrgId()));
            }*/

            List<BlockWorkStageDTO> workStageList = resultVO.getWorkStageList();
            Integer aLong = getBaseMapper().selectMaps(Wrappers.<SurveyTasks>query()
                    .select("count(1) count").eq(SurveyTasks.BLOCK_ID, blockVO.getId())
                    .eq(SurveyTasks.STATUS, SurveyTaskStatusEnum.ALLOCATED.name()).eq(SurveyTasks.DELETED, 0)
                    .groupBy(SurveyTasks.BLOCK_WORK_STAGE_ID)
            ).size();
            resultVO.setTaskPlan(aLong + "/" + workStageList.size());
            workStageList.stream().forEach(workStageDTO -> {
                List<SurveyTasks> tt = getBaseMapper().selectList(Wrappers.<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()));
                int size = tt.stream().filter(item -> Objects.equals(item.getBlockWorkStageId(), SurveyTaskStatusEnum.RECALL)).collect(Collectors.toList()).size();
                if (tt.size() < 0) {
                    // 没有分配记录则是待分配
                    workStageDTO.setTaskStatus(SurveyTaskStatusEnum.NOT_ALLOT.name());
                    workStageDTO.setTaskStatusDesc(SurveyTaskStatusEnum.NOT_ALLOT.getValue());
                } else if (tt.size() > 0 && size > 0) {
                    // 有分配记录，并且有撤回的记录
                    workStageDTO.setTaskStatus(SurveyTaskStatusEnum.RECALL.name());
                    workStageDTO.setTaskStatusDesc(SurveyTaskStatusEnum.RECALL.getValue());
                } else {
                    workStageDTO.setTaskStatus(SurveyTaskStatusEnum.ALLOCATED.name());
                    workStageDTO.setTaskStatusDesc(SurveyTaskStatusEnum.ALLOCATED.getValue());
                }

                List<SurveyTasks> surveyTasks = getBaseMapper().selectList(Wrappers.<SurveyTasks>query().eq(SurveyTasks.BLOCK_WORK_STAGE_ID, workStageDTO.getId()).eq(SurveyTasks.DELETED, 0));
                workStageDTO.setTasksList(surveyTasks.stream().map(task -> {
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
            });
            return resultVO;
        });
    }

    @Override
    public List<TechOrganizationUserDTO> listOrgAll(OrgListAllQuery query) {

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取技术单位");
        }

        Organization organization = organizationService.getById(details.getOrgId());

        OrganizationQuery orgQuery = new OrganizationQuery();
        if (StringUtils.isEmpty(organization.getPid())) {
            orgQuery.setPid(details.getOrgId());
        } else {
            orgQuery.setPid(organization.getPid());
        }

        orgQuery.setStatus(OrganizationStatus.NORMAL.toString());
        orgQuery.setBizType(Lists.newArrayList(query.getBizType()));


        List<TechOrganizationUserDTO> list = new ArrayList<>();
        List<OrganizationDTO> orgList = organizationService.list(orgQuery);
        orgList.stream().forEach(t -> {
            TechOrganizationUserDTO orgUserDTO = new TechOrganizationUserDTO();
            orgUserDTO.setOrganization(t);

            //TODO 目前就只查询管理员，防止后续有其他多个用户，用户信息返回集合
            UserDTO userDTO = userService.getOrganizationAdmin(t.getId());
            List<UserDTO> admin = new ArrayList<>();
            admin.add(userDTO);
            orgUserDTO.setAdmin(admin);

            list.add(orgUserDTO);
        });
        return list;
    }

    @Override
    public SurveyTasks getByBlockWorkStageIdAndType(String blockWorkStageId, String type) {
        return getBaseMapper().selectOne(Wrappers.<SurveyTasks>lambdaQuery()
                .eq(SurveyTasks::getBlockWorkStageId, blockWorkStageId)
                .eq(SurveyTasks::getType, type));
    }
}
