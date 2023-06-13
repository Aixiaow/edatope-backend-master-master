package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.QualityControlTasks;
import com.csbaic.edatope.app.entity.Specialist;
import com.csbaic.edatope.app.entity.SpecialistUser;
import com.csbaic.edatope.app.enums.*;
import com.csbaic.edatope.app.mapper.SpecialistMapper;
import com.csbaic.edatope.app.model.command.CreateSpecialistCmd;
import com.csbaic.edatope.app.model.dto.SpecialistPageResultVO;
import com.csbaic.edatope.app.model.dto.SpecialistUserVO;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.dto.UserSelectResultVO;
import com.csbaic.edatope.app.model.query.SpecialistQuery;
import com.csbaic.edatope.app.model.query.SpecialistUserQuery;
import com.csbaic.edatope.app.model.query.UserListQuery;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.app.utils.UserUtils;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 专家组表 服务实现类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
@Service
public class SpecialistServiceImpl extends ServiceImpl<SpecialistMapper, Specialist> implements ISpecialistService {

    @Autowired
    private ISpecialistUserService specialistUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IQualityControlTasksService qualityControlTasksService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizationService organizationService;

    /**
     * 布点质控人员角色id
     */
    private static final String pointQualityUserRoleId = "1522127071303344129";

    private static final List<String> unComplateStatus = Arrays.asList(PlanAuditStatusEnum.CROSS.getValue()
            , PlanAuditStatusEnum.STAY_AUDIT.getValue(), PlanAuditStatusEnum.STAY_COLLECT.getValue(),
            PlanAuditStatusEnum.BACK_PERFECT.getValue(), PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
            PlanAuditStatusEnum.BACK_RETRIAL.getValue(), PlanAuditStatusEnum.RETRIAL_REVIEW.getValue(),
            PlanAuditStatusEnum.BACK_MAINTAIN.getValue());

    /**
     * 新建或维护专家组
     *
     * @param specialist
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSpecialistCmd specialist) {
        List<CreateSpecialistCmd.CreateSpecialistPerson> personList = specialist.getSpecialist();
        // 唯一性校验：（专家组名称唯一）&&（组长人选不同或至少一个组员不同），此时才可视为不同的专家组，否则不可重复组建。
        String groupName = specialist.getGroupName();
        String id = specialist.getId();
        if (personList.size() < 3) {
            throw BizRuntimeException.from(ResultCode.ERROR, "至少应有三个专家！");
        }

        List<CreateSpecialistCmd.CreateSpecialistPerson> groupLeaderList = personList.stream()
                .filter(a -> a.getNature().equals(SpecialistNatureEnum.GROUP_LEADER.getValue()))
                .collect(Collectors.toList());
        if (groupLeaderList.size() != 1) {
            throw BizRuntimeException.from(ResultCode.ERROR, "应有且仅有一个组长！");
        }

        List<CreateSpecialistCmd.CreateSpecialistPerson> collect = personList.stream().sorted(new Comparator<CreateSpecialistCmd.CreateSpecialistPerson>() {
            @Override
            public int compare(CreateSpecialistCmd.CreateSpecialistPerson o1, CreateSpecialistCmd.CreateSpecialistPerson o2) {
                return o1.getUserId().compareTo(o2.getUserId());
            }
        }).collect(Collectors.toList());

        StringBuilder userIdStr = new StringBuilder();
        collect.forEach(a -> {
            String userId = a.getUserId();
            userIdStr.append(userId);
            String nature = a.getNature();
            String specialistIdentity = a.getSpecialistIdentity();
            if (SpecialistIdentityEnum.OUTSIDE_EXPERTS.getValue().equals(specialistIdentity)
                    && SpecialistNatureEnum.GROUP_LEADER.getValue().equals(nature)) {
                throw BizRuntimeException.from(ResultCode.ERROR, "外聘专家不允许担任组长！");
            }
        });
        String encode = passwordEncoder.encode(userIdStr.toString());

        if (id != null) {
            Specialist byId = getById(id);
            if (byId == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该专家组不存在");
            }
            if (byId.getStatus().equals(SpecialistStatusEnum.DISABLE.name())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该专家组已停用");
            }

            // 一旦专家组分配了质控任务，则不可再进行“维护”
            Long count = qualityControlTasksService.getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                    .eq(QualityControlTasks::getQualitySpecialistId, id));
            if (count > 0) {
                throw BizRuntimeException.from(ResultCode.ERROR, "专家组分配了质控任务，不可进行维护!");
            }

            Long groupCount = getBaseMapper().selectCount(Wrappers.<Specialist>lambdaQuery()
                    .eq(Specialist::getGroupName, groupName).ne(Specialist::getId, id));
            if (groupCount > 0) {
                throw BizRuntimeException.from(ResultCode.ERROR, "已有相同的专家组，不可重复组建！");
            }

            String groupLeaderUserId = groupLeaderList.get(0).getUserId();
            Long groupLeaderUserCount = specialistUserService
                    .getBaseMapper()
                    .selectCount(Wrappers.<SpecialistUser>lambdaQuery()
                            .eq(SpecialistUser::getUserId, groupLeaderUserId)
                            .ne(SpecialistUser::getSpecialistId, id));
            // 如果有相同组长，则判断是否组员也相同
            if (groupLeaderUserCount > 0) {
                Long encodeCount = getBaseMapper().selectCount(Wrappers.<Specialist>lambdaQuery()
                        .eq(Specialist::getGroupUser, encode).ne(Specialist::getId, id));
                if (encodeCount > 0) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "已有相同的专家组，不可重复组建！");
                }
            }

            byId.setGroupUser(encode);
            updateById(byId);

            List<SpecialistUser> arrayList = new ArrayList<>();
            collect.forEach(b -> {
                SpecialistUser specialistUser = new SpecialistUser();
                specialistUser.setSpecialistId(id);
                BeanCopyUtils.copyNonNullProperties(b, specialistUser);
                arrayList.add(specialistUser);
            });

            specialistUserService.update(Wrappers.<SpecialistUser>lambdaUpdate()
                    .set(SpecialistUser::getDeleted, 1)
                    .eq(SpecialistUser::getSpecialistId, id));

            boolean b = specialistUserService.saveBatch(arrayList);
            if (b) {
                String groupUserName = getGroupUserName(arrayList);
                update(
                        Wrappers.<Specialist>lambdaUpdate().set(Specialist::getGroupUserName, groupUserName)
                                .eq(Specialist::getId, id)
                );
            }
        } else {
            Long groupCount = getBaseMapper().selectCount(Wrappers.<Specialist>lambdaQuery()
                    .eq(Specialist::getGroupName, groupName));
            if (groupCount > 0) {
                throw BizRuntimeException.from(ResultCode.ERROR, "已有相同的专家组，不可重复组建！");
            }

            Specialist specialistA = new Specialist();
            specialistA.setGroupName(groupName);
            specialistA.setOrgId(UserUtils.getUserOrgId());
            specialistA.setStatus(SpecialistStatusEnum.NORMAL.name());
            specialistA.setGroupUser(encode);
            save(specialistA);

            List<SpecialistUser> arrayList = new ArrayList<>();

            collect.forEach(b -> {
                SpecialistUser specialistUser = new SpecialistUser();
                specialistUser.setSpecialistId(specialistA.getId());
                BeanCopyUtils.copyNonNullProperties(b, specialistUser);
                arrayList.add(specialistUser);
            });

            String groupLeaderUserId = groupLeaderList.get(0).getUserId();
            Long groupLeaderUserCount = specialistUserService.getBaseMapper().selectCount(Wrappers.<SpecialistUser>lambdaQuery()
                    .eq(SpecialistUser::getUserId, groupLeaderUserId));
            // 如果有相同组长，则判断是否组员也相同
            if (groupLeaderUserCount > 0) {
                Long encodeCount = getBaseMapper().selectCount(Wrappers.<Specialist>lambdaQuery()
                        .eq(Specialist::getGroupUser, encode));
                if (encodeCount > 0) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "已有相同的专家组，不可重复组建！");
                }
            }
            boolean b = specialistUserService.saveBatch(arrayList);
            if (b) {
                String groupUserName = getGroupUserName(arrayList);
                update(
                        Wrappers.<Specialist>lambdaUpdate().set(Specialist::getGroupUserName, groupUserName)
                                .eq(Specialist::getId, specialistA.getId())
                );
            }
        }
    }

    private String getGroupUserName(List<SpecialistUser> list) {
//        List<SpecialistUser> list = specialistUserService.list(
//                Wrappers.<SpecialistUser>lambdaQuery()
//                        .eq(SpecialistUser::getSpecialistId, specialistId)
//        );
        String leaderUserName = "";
        String outsideUserName = "";
        String otherUserName = "";
        String result = "";
        for (SpecialistUser specialistUser : list) {
            boolean other = false;
            String userId = specialistUser.getUserId();
            String nickName = userService.getById(userId).getNickName();
            String nature = specialistUser.getNature();
            String specialistIdentity = specialistUser.getSpecialistIdentity();
            if (nature.equals(SpecialistNatureEnum.GROUP_LEADER.getValue())) {
                leaderUserName = nickName;
                other = true;
            }
            if (specialistIdentity.equals(SpecialistIdentityEnum.OUTSIDE_EXPERTS.getValue())) {
                outsideUserName = nickName;
                other = true;
            }
            if (!other) {
                otherUserName = otherUserName + nickName + "、";
            }
        }
        result = leaderUserName + "(组长)、" + otherUserName;
        if (StringUtils.isNotEmpty(outsideUserName)) {
            result = result + "、" + outsideUserName + "(外聘)";
        } else {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 停用专家组
     *
     * @param id
     */
    @Override
    public void stop(String id, String status) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(status)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "缺少参数");
        }
        if (SpecialistStatusEnum.DISABLE.name().equals(status)) {
            // 停用存在前提条件，即该专家组内不可存在处于“审核中”（待审核、完善待复核、重审待复核及待汇总）状态的质控任务
            Long count = qualityControlTasksService.getCountByStatus(id, PlanAuditStatusEnum.STAY_AUDIT.getValue(),
                    PlanAuditStatusEnum.STAY_COLLECT.getValue(), PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
                    PlanAuditStatusEnum.RETRIAL_REVIEW.getValue());
            if (count > 0) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该专家组内存在处于“审核中”状态的质控任务，不可停用");
            }
            update(Wrappers.<Specialist>lambdaUpdate()
                    .set(Specialist::getStatus, SpecialistStatusEnum.DISABLE.name())
                    .eq(Specialist::getId, id));
        } else {
            update(Wrappers.<Specialist>lambdaUpdate()
                    .set(Specialist::getStatus, SpecialistStatusEnum.NORMAL.name())
                    .eq(Specialist::getId, id));
        }
    }

    /**
     * 删除专家组
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        // 一旦专家组分配了质控任务，则不可再进行“删除”
        Long count = qualityControlTasksService.getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                .eq(QualityControlTasks::getQualitySpecialistId, id));
        if (count > 0) {
            throw BizRuntimeException.from(ResultCode.ERROR, "专家组分配了质控任务，不可进行删除!");
        }
        update(Wrappers.<Specialist>lambdaUpdate()
                .set(Specialist::getDeleted, 1)
                .eq(Specialist::getId, id));
        specialistUserService.update(Wrappers.<SpecialistUser>lambdaUpdate()
                .set(SpecialistUser::getDeleted, 1)
                .eq(SpecialistUser::getSpecialistId, id));
    }

    /**
     * 布点质控专家组-列表-分页
     *
     * @param blockQuery
     * @return
     */
    @Override
    public IPage<SpecialistPageResultVO> page(SpecialistQuery blockQuery) {
        return getBaseMapper()
                .page(new Page<>(blockQuery.getPageIndex(), blockQuery.getPageSize()), blockQuery)
                .convert(specialist -> {
                    // 分配任务总数
                    Long totalCount = qualityControlTasksService.getBaseMapper().selectCount(
                            Wrappers.<QualityControlTasks>lambdaQuery()
                                    .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                                    .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                    );
                    // 未完成任务数
                    Long unCompleteCount = qualityControlTasksService.getBaseMapper().selectCount(
                            Wrappers.<QualityControlTasks>lambdaQuery()
                                    .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId())
                                    .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                    .in(QualityControlTasks::getAuditStatus, unComplateStatus)
                    );
                    // 最晚任务期限
                    List<QualityControlTasks> list = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                            .eq(QualityControlTasks.QUALITY_SPECIALIST_ID, specialist.getId())
                            .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                            .orderByDesc(QualityControlTasks.DEADLINE)
                            .last("limit 1")
                    );
                    if (list != null && !list.isEmpty()) {
                        specialist.setDeadLine(list.get(0).getDeadline() + "");
                    }
                    specialist.setUnCompleteCount(unCompleteCount);
                    specialist.setTotalCount(totalCount);
                    specialist.setCompleteRate((totalCount == 0 ? "0" : ((totalCount - unCompleteCount) / totalCount) + "%"));
                    if (StringUtils.isNotEmpty(specialist.getGroupLeaderUserId())) {
                        specialist.setGroupLeaderName(userService.getUserDetail(specialist.getGroupLeaderUserId()));
                    }
                    // 外聘专家数
                    long outsideCount = specialistUserService.count(Wrappers.<SpecialistUser>lambdaQuery()
                            .eq(SpecialistUser::getSpecialistId, specialist.getId())
                            .eq(SpecialistUser::getSpecialistIdentity, SpecialistIdentityEnum.OUTSIDE_EXPERTS.getValue()));
                    specialist.setOutsideCount(outsideCount);
                    String groupUserName = specialist.getGroupUserName();
                    if (StringUtils.isNotEmpty(groupUserName)) {
                        specialist.setGroupUserName(groupUserName.substring(groupUserName.indexOf("、") + 1, groupUserName.length()));
                    }
                    specialist.setStatusDesc(SpecialistStatusEnum.getDescByName(specialist.getStatus()));

                    // 一旦专家组分配了质控任务，则不可再进行维护|“删除”
                    Long count = qualityControlTasksService.getBaseMapper().selectCount(Wrappers.<QualityControlTasks>lambdaQuery()
                            .eq(QualityControlTasks::getQualitySpecialistId, specialist.getId()));
                    if (count > 0) {
                        specialist.setDelete(2);
                        specialist.setDefend(2);
                    }
                    // 停用存在前提条件，即该专家组内不可存在处于“审核中”（待审核、完善待复核、重审待复核及待汇总）状态的质控任务
                    Long countA = qualityControlTasksService.getCountByStatus(specialist.getId(), PlanAuditStatusEnum.STAY_AUDIT.getValue(),
                            PlanAuditStatusEnum.STAY_COLLECT.getValue(), PlanAuditStatusEnum.PERFECT_REVIEW.getValue(),
                            PlanAuditStatusEnum.RETRIAL_REVIEW.getValue());
                    if (countA > 0) {
                        specialist.setStop(2);
                    }
                    return specialist;
                });
    }

    /**
     * 选择选择质控专家列表-分页
     *
     * @param userListQuery
     * @return
     */
    @Override
    public IPage<UserSelectResultVO> userList(UserListQuery userListQuery) {
        UserQuery userQuery = new UserQuery();
        BeanCopyUtils.copyNonNullProperties(userListQuery, userQuery);
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(userListQuery.getOrgId())) {
            userQuery.setOrgIds(new ArrayList<String>() {{
                details.getOrgId();
            }});
        } else {
            userQuery.setOrgIds(new ArrayList<String>() {{
                userListQuery.getOrgId();
            }});
        }
        userQuery.setRoles(new ArrayList<String>() {{
            add(pointQualityUserRoleId);
        }});
        IPage<UserDTO> userDTOIPage = userService.listUserPage(userQuery);
        List<SpecialistUser> specialistUserList = specialistUserService.list(
                Wrappers.<SpecialistUser>lambdaQuery()
                        .eq(SpecialistUser::getUserId, details.getId())
        );
        // 查找到所属的专家组
        List<String> specialist = specialistUserList.stream().map(SpecialistUser::getSpecialistId).collect(Collectors.toList());
        return userDTOIPage.convert(userDTO -> {
            UserSelectResultVO resultVO = new UserSelectResultVO();
            BeanCopyUtils.copyNonNullProperties(userDTO, resultVO);
            if (!specialist.isEmpty()) {
                // 分配任务总数
                Long totalCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialist)
                );
                // 未完成任务数
                Long unCompleteCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialist)
                                .in(QualityControlTasks::getAuditStatus, unComplateStatus)
                );
                // 最晚任务期限
                List<QualityControlTasks> list = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                        .in(QualityControlTasks.QUALITY_SPECIALIST_ID, specialist)
                        .orderByDesc(QualityControlTasks.DEADLINE)
                        .last("limit 1")
                );
                if (list != null && !list.isEmpty()) {
                    resultVO.setDeadLine(list.get(0).getDeadline() + "");
                }
                resultVO.setTotalCount(Integer.parseInt(totalCount + ""));
                resultVO.setUnCompleteCount(unCompleteCount);
            }
            return resultVO;
        });
    }

    /**
     * 查看专家组
     *
     * @param id
     * @return
     */
    @Override
    public List<SpecialistUserVO> viewDetail(String id) {
        List<SpecialistUser> specialistUserList = specialistUserService.list(
                Wrappers.<SpecialistUser>lambdaQuery()
                        .eq(SpecialistUser::getSpecialistId, id));
        List<SpecialistUserVO> resultList = new ArrayList<>();
        if (!specialistUserList.isEmpty()) {
            for (SpecialistUser specialistUser : specialistUserList) {
                SpecialistUserVO specialistUserVO = new SpecialistUserVO();
                BeanCopyUtils.copyNonNullProperties(specialistUser, specialistUserVO);
                specialistUserVO.setNatureDesc(SpecialistNatureEnum.getDescByValue(specialistUser.getNature()));
                specialistUserVO.setOrgId(organizationService.getById(specialistUser.getOrgId()));
                specialistUserVO.setUserInfo(userService.getUserDetail(specialistUser.getUserId()));
                specialistUserVO.setSpecialistIdentityDesc(SpecialistIdentityEnum.getDescByValue(specialistUser.getSpecialistIdentity()));

                // 查询用户所在的专家组
                List<SpecialistUser> specialistUserListA = specialistUserService.list(
                        Wrappers.<SpecialistUser>lambdaQuery()
                                .eq(SpecialistUser::getUserId, specialistUser.getUserId())
                );
                List<String> specialistIds = specialistUserListA.stream().map(a -> a.getSpecialistId()).collect(Collectors.toList());

                // 分配任务总数
                Long totalCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialistIds)
                );
                // 未完成任务数
                Long unCompleteCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialistIds)
                                .in(QualityControlTasks::getAuditStatus, unComplateStatus)
                );
                // 最晚任务期限
                List<QualityControlTasks> list = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                        .in(QualityControlTasks.QUALITY_SPECIALIST_ID, specialistIds)
                        .orderByDesc(QualityControlTasks.DEADLINE)
                        .last("limit 1")
                );
                if (list != null && !list.isEmpty()) {
                    specialistUserVO.setDeadLine(list.get(0).getDeadline() + "");
                }
                specialistUserVO.setTotalCount(Integer.parseInt(totalCount + ""));
                specialistUserVO.setUnCompleteCount(unCompleteCount);

                resultList.add(specialistUserVO);
            }

        }
        return resultList;
    }

    /**
     * 布点质控专家-列表-分页
     *
     * @param query
     * @return
     */
    @Override
    public IPage<SpecialistUserVO> specialistUserPage(SpecialistUserQuery query) {
        return specialistUserService.specialistUserPage(query).convert(specialistUser -> {
            SpecialistUserVO resultVO = new SpecialistUserVO();
            BeanCopyUtils.copyNonNullProperties(specialistUser, resultVO);
            resultVO.setNatureDesc(SpecialistNatureEnum.getDescByValue(specialistUser.getNature()));
            resultVO.setOrgId(organizationService.getById(specialistUser.getOrgId()));
            resultVO.setUserInfo(userService.getUserDetail(specialistUser.getUserId()));
            resultVO.setSpecialistIdentityDesc(SpecialistIdentityEnum.getDescByValue(specialistUser.getSpecialistIdentity()));

            List<SpecialistUser> specialistUserList = specialistUserService.list(
                    Wrappers.<SpecialistUser>lambdaQuery()
                            .eq(SpecialistUser::getUserId, specialistUser.getUserId())
            );
            // 查找到所属的专家组
            List<String> specialist = specialistUserList.stream().map(a -> a.getSpecialistId()).collect(Collectors.toList());

            long leaderCount = specialistUserList.stream().filter(a -> a.getNature().equals(SpecialistNatureEnum.GROUP_LEADER.getValue())).count();
            // 所属专家组数
            resultVO.setSpecialistCount(specialist.size());
            // 担任组长数
            resultVO.setLeaderCount(leaderCount);
            if (!specialist.isEmpty()) {
                // 分配任务总数
                Long totalCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialist)
                );
                // 未完成任务数
                Long unCompleteCount = qualityControlTasksService.getBaseMapper().selectCount(
                        Wrappers.<QualityControlTasks>lambdaQuery()
                                .eq(QualityControlTasks::getDistributeSpecialistStatus, SurveyTaskStatusEnum.ALLOCATED.name())
                                .in(QualityControlTasks::getQualitySpecialistId, specialist)
                                .in(QualityControlTasks::getAuditStatus, unComplateStatus)
                );
                // 最晚任务期限
                List<QualityControlTasks> list = qualityControlTasksService.getBaseMapper().selectList(Wrappers.<QualityControlTasks>query()
                        .eq(QualityControlTasks.DISTRIBUTE_SPECIALIST_STATUS, SurveyTaskStatusEnum.ALLOCATED.name())
                        .in(QualityControlTasks.QUALITY_SPECIALIST_ID, specialist)
                        .orderByDesc(QualityControlTasks.DEADLINE)
                        .last("limit 1")
                );
                if (list != null && !list.isEmpty()) {
                    resultVO.setDeadLine(list.get(0).getDeadline() + "");
                }
                resultVO.setTotalCount(Integer.parseInt(totalCount + ""));
                resultVO.setUnCompleteCount(unCompleteCount);
                resultVO.setCompleteRate((totalCount == 0 ? "0" : ((totalCount - unCompleteCount) / totalCount) + "%"));
            }
            return resultVO;
        });
    }
}
