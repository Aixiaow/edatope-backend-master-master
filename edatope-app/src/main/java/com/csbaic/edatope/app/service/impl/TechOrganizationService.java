package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.convert.OrganizationConvert;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.TechOrganizationRelevance;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.enums.OrganizationBizTypeEnum;
import com.csbaic.edatope.app.enums.OrganizationStatus;
import com.csbaic.edatope.app.enums.RolePropertyEnum;
import com.csbaic.edatope.app.enums.RoleTypeEnum;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.RoleDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationDTO;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.OrgListAllQuery;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.TechOrganizationQuery;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 技术单位管理
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class TechOrganizationService {


    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private OrganizationConvert convert;

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IOrganizationBizTypeService organizationBizTypeService;

    @Autowired
    private ITechOrganizationRelevanceService techOrganizationRelevanceService;


    @Transactional
    public void create(CreateTechOrganizationCmd cmd) {
        CreateOrganizationCmd organizationCmd = cmd.getOrganization();
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息");
        }
        Organization myOrganization = organizationService.getById(details.getOrgId());
        if (Objects.isNull(myOrganization)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户单位信息错误");
        }

        Organization exists = organizationService.getOne(Wrappers.<Organization>query().eq(Organization.CODE, organizationCmd.getCode()));
        if (exists != null) {
//            throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + organizationCmd.getCode() + " 已存在");
            TechOrganizationRelevance techOrganizationRelevance = techOrganizationRelevanceService.getBaseMapper().selectOne(Wrappers.<TechOrganizationRelevance>query()
                    .eq(TechOrganizationRelevance.ORG_ID, exists.getId())
                    .eq(TechOrganizationRelevance.OWNER_ID, myOrganization.getId()));
            if (Objects.isNull(techOrganizationRelevance)) {
                techOrganizationRelevance = new TechOrganizationRelevance();
                techOrganizationRelevance.setOrgId(exists.getId());
                techOrganizationRelevance.setOwnerId(myOrganization.getId());
                techOrganizationRelevance.setServiceLevel(myOrganization.getServiceLevel());
                techOrganizationRelevanceService.save(techOrganizationRelevance);
            }
        } else {
            Organization organization = new Organization();
            BeanCopyUtils.copyNonNullProperties(organizationCmd, organization);
            organization.setStatus(OrganizationStatus.NORMAL.name());


            if (myOrganization != null) {
                //复制业务类型
                // 此处废弃pid，serviceLevel 05月08日
                organization.setServiceLevel(StringUtils.isNotEmpty(organizationCmd.getServiceLevel()) ? organizationCmd.getServiceLevel() : myOrganization.getServiceLevel());
                organization.setPid(myOrganization.getId());
            }


            organizationService.save(organization);
            //设置单位的业务类型
            organizationBizTypeService.setBizTypes(organization.getId(), organizationCmd.getBizType());
            CreateTechOrganizationAdminCmd adminCmd = cmd.getAdmin();
            CreateUserCmd userCmd = new CreateUserCmd();
            BeanCopyUtils.copyNonNullProperties(adminCmd, userCmd);
            userCmd.setOrgId(organization.getId());
            userService.create(userCmd);

            TechOrganizationRelevance techOrganizationRelevance = new TechOrganizationRelevance();
            techOrganizationRelevance.setOrgId(organization.getId());
            techOrganizationRelevance.setOwnerId(myOrganization.getId());
            techOrganizationRelevance.setServiceLevel(myOrganization.getServiceLevel());
            techOrganizationRelevanceService.save(techOrganizationRelevance);
        }
    }


    /**
     * 技术单位管理
     *
     * @param query
     * @return
     */
    public IPage<TechOrganizationDTO> listPage(TechOrganizationQuery query) {

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取技术单位");
        }

        OrganizationQuery organizationQuery = new OrganizationQuery();
        BeanCopyUtils.copyNonNullProperties(query, organizationQuery);
        organizationQuery.setPid(details.getOrgId());

        if (Objects.equals(query.getBizType(), OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            return Page.of(query.getPageIndex(), 0);
        } else if (StringUtils.isNotEmpty(query.getBizType())) {
            organizationQuery.setBizType(Lists.newArrayList(query.getBizType()));
        } else {
            //只查技术单位
            organizationQuery.setBizType(
                    OrganizationBizTypeEnum.getTechOrganizationList()
                            .stream()
                            .map(OrganizationBizTypeEnum::getValue)
                            .collect(Collectors.toList())
            );
        }
        //只查询业务单位
        //查询管理没
        return organizationService.listPage(organizationQuery).convert(organizationDTO -> {
            TechOrganizationDTO techOrganizationDTO = new TechOrganizationDTO();
            techOrganizationDTO.setOrganization(organizationDTO);
            techOrganizationDTO.setAdmin(userService.getOrganizationAdmin(organizationDTO.getId()));
            return techOrganizationDTO;
        });
    }


    /**
     * 技术单位管理列表
     *
     * @param
     * @return
     */
    public List<OrganizationDTO> listAll() {

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取技术单位");
        }

        OrganizationQuery query = new OrganizationQuery();
        query.setPid(details.getOrgId());
        query.setStatus(OrganizationStatus.NORMAL.toString());
        //只查技术单位
        query.setBizType(
                OrganizationBizTypeEnum.getTechOrganizationList()
                        .stream()
                        .map(OrganizationBizTypeEnum::getValue)
                        .collect(Collectors.toList())
        );
        //查询管理没
        return organizationService.list(query);
    }

    /**
     * 根据业务类型查询单位列表
     *
     * @param
     * @return
     */
    public List<OrganizationDTO> listOrgAll(OrgListAllQuery query) {

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

        return organizationService.list(orgQuery);
    }

    /**
     * 花名册用户列表
     *
     * @param organizationId
     * @return
     */
    public List<UserDTO> listOrganizationUser(String organizationId) {
        return userService.listAllUserByOrgId(organizationId);
    }

    /**
     * 技术单位详情
     *
     * @return
     */
    public TechOrganizationDTO getTechOrganizationDetail(String orgId) {
        Organization organization = organizationService.getById(orgId);
        if (organization == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到单位：" + orgId);
        }

        UserDTO userDTO = userService.getOrganizationAdmin(orgId);
        TechOrganizationDTO techOrganizationDTO = new TechOrganizationDTO();
        techOrganizationDTO.setOrganization(convert.convertDTO(organization));
        techOrganizationDTO.setAdmin(userDTO);
        return techOrganizationDTO;
    }

    @Transactional
    public void update(UpdateTechOrganizationCmd organizationCmd) {
        Organization organization = organizationService.getById(organizationCmd.getOrganization().getId());
        if (organization == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到单位：" + organizationCmd.getOrganization().getId());
        }

        User user = userService.getById(organizationCmd.getAdmin().getId());
        if (user == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到管理员：" + organizationCmd.getAdmin().getId());
        }

        BeanCopyUtils.copyNonNullProperties(organizationCmd.getOrganization(), organization);
        BeanCopyUtils.copyNonNullProperties(organizationCmd.getAdmin(), user);
        organizationService.updateById(organization);
        userService.updateById(user);
        //设置业务类型
        organizationBizTypeService.setBizTypes(organization.getId(), organizationCmd.getOrganization().getBizType());
        //更新角色
        if (CollectionUtils.isNotEmpty(organizationCmd.getAdmin().getRoles())) {
            roleService.appendRoleToUser(user.getId(), organizationCmd.getAdmin().getRoles());
        }

    }

    public List<RoleDTO> listOrganizationRole() {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取角色");
        }

        return roleService.listRoleByOrgId(details.getOrgId());
    }

    public List<RoleDTO> listOrganizationAdminRoleWithBizType(List<String> bizType) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位信息，无法获取角色");
        }

        List<String> roleTypeEnums = RoleTypeEnum.getRoleTypeByBizType(bizType)
                .stream()
                .map(RoleTypeEnum::getValue)
                .collect(Collectors.toList());

        return roleService.listRoleByOrgId(details.getOrgId())
                .stream()
                .filter(roleDTO -> roleTypeEnums.contains(roleDTO.getType()) && Objects.equals(roleDTO.getProperty(), RolePropertyEnum.ORG_ADMIN.getValue()))
                .collect(Collectors.toList());
    }

    public void resetPwd(ResetPasswordCmd resetPasswordCmd) {
        userService.resetPassword(resetPasswordCmd);
    }

    public void delete(DeleteOrganizationCmd deleteOrganizationCmd) {
        organizationService.delete(deleteOrganizationCmd);
    }


}
