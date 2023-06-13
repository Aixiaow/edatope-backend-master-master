package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csbaic.edatope.app.enums.OrganizationStatus;
import com.csbaic.edatope.app.model.command.OrganizationAdminCmd;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.enums.UserStatus;
import com.csbaic.edatope.app.model.command.CreateUserCmd;
import com.csbaic.edatope.app.model.dto.UserDTO;
import com.csbaic.edatope.app.model.query.UserQuery;
import com.csbaic.edatope.app.service.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class OrganizationUserService {

    @Autowired
    private IRoleService roleService;
    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizationBizTypeService organizationBizTypeService;


    @OperateLog(remark = "#{operatorName} 添加用户成功 ")
    @Transactional
    public void create(CreateUserCmd cmd) {
        User exists = userService.getByUsernameOrMobile(cmd.getUsername());
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该用户名已经被使用");
        }
        exists = userService.getByUsernameOrMobile(cmd.getMobile());
        if (exists != null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "该手机号已经被使用");
        }

        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }

        Organization organization = organizationService.getById(details.getOrgId());
        if (organization == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "用户所在单位不存在");
        }

        User user = new User();
        BeanCopyUtils.copyNonNullProperties(cmd, user);
        String salt = UUID.randomUUID().toString();
        String encoded = passwordEncoder.encode(cmd.getMobile().substring(3) + salt);
        user.setPasswordSalt(salt);
        user.setPassword(encoded);
        user.setLocked(Boolean.FALSE);
        user.setStatus(UserStatus.NORMAL.name());
        user.setOrgId(organization.getId());
        userService.save(user);
        roleService.assignRoleToUser(user.getId(), cmd.getRoles());
    }


    public IPage<UserDTO> listUserPage(UserQuery query) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        //只查询本单位的用户
        if (StringUtils.isNotEmpty(details.getOrgId())) {
            query.setOrgIds(Lists.newArrayList(details.getOrgId()));
        }

        return userService.listUserPage(query);
    }

    @Transactional
    public void createAdminOrg(OrganizationAdminCmd cmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        Organization organization;
        if (StringUtils.isNotEmpty(cmd.getId())) {
            //有id更新以有的单位
            organization = organizationService.getById(cmd.getId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到单位：" + cmd.getName());
            }
        } else {
            organization = new Organization();
            QueryWrapper<Organization> queryWrapper = Wrappers.query();
            queryWrapper.eq(Organization.NAME, cmd.getName());
            if (organizationService.count(queryWrapper) > 0) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位：" + cmd.getName() + "已存在");
            }

            if (StringUtils.isNotEmpty(cmd.getCode()) && !Objects.equals(organization.getCode(), cmd.getCode())) {
                Organization exists = organizationService.getOne(Wrappers.<Organization>query().eq(Organization.CODE, cmd.getCode()));
                if (exists != null) {
                    throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + cmd.getCode() + " 已存在");
                }
            }
            organization.setStatus(OrganizationStatus.NORMAL.name());
            organization.setPid(details.getOrgId());
        }

        BeanCopyUtils.copyNonNullProperties(cmd, organization);
        organizationService.saveOrUpdate(organization);
        //设置业务类型
        if (CollectionUtils.isNotEmpty(cmd.getBizType())) {
            organizationBizTypeService.setBizTypes(organization.getId(), cmd.getBizType());
        }

        User user = new User();
        if (StringUtils.isNotEmpty(cmd.getUserAdminCmd().getId())) {
            //有id更新以有的单位
            user = userService.getById(cmd.getUserAdminCmd().getId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到用户：" + cmd.getUserAdminCmd().getUsername());
            }
        }

        BeanCopyUtils.copyNonNullProperties(cmd.getUserAdminCmd(), user);
        if (StringUtils.isEmpty(cmd.getUserAdminCmd().getId())) {
            User exists = userService.getByUsernameOrMobile(cmd.getUserAdminCmd().getUsername());
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该用户名已经被使用");
            }
            exists = userService.getByUsernameOrMobile(cmd.getUserAdminCmd().getMobile());
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "该手机号已经被使用");
            }
            String salt = UUID.randomUUID().toString();
            String encoded = passwordEncoder.encode(cmd.getUserAdminCmd().getMobile().substring(3) + salt);
            user.setPasswordSalt(salt);
            user.setPassword(encoded);
            user.setLocked(Boolean.FALSE);
            user.setStatus(UserStatus.NORMAL.name());
            user.setOrgId(organization.getId());
            userService.save(user);
        } else {
            userService.updateById(user);
        }
        roleService.assignRoleToUser(user.getId(), cmd.getUserAdminCmd().getRoles());
    }

}
