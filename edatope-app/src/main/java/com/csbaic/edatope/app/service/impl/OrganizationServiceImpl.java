package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.csbaic.edatope.app.enums.OrganizationBizTypeEnum;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.OrganizationAdminUserDTO;
import com.csbaic.edatope.app.model.query.OrganizationAdminUserQuery;
import com.csbaic.edatope.app.model.query.QualityControlOrgList;
import com.csbaic.edatope.app.service.IOrganizationBizTypeService;
import com.csbaic.edatope.app.convert.OrganizationConvert;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.enums.OrganizationStatus;
import com.csbaic.edatope.app.mapper.OrganizationMapper;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.service.IOrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统组织机构表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements IOrganizationService {

    @Autowired
    private OrganizationConvert organizationConvert;

    @Autowired
    private IDictService dictService;
    @Autowired
    private IOrganizationBizTypeService organizationBizTypeService;

    @Override
    public void create(CreateOrganizationCmd cmd) {

        if (StringUtils.isNotEmpty(cmd.getCode())) {
            Organization exists = getOne(Wrappers.<Organization>query().eq(Organization.CODE, cmd.getCode()));
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + cmd.getCode() + " 已存在");
            }
        }
        Organization organization = new Organization();
        BeanCopyUtils.copyNonNullProperties(cmd, organization);
        organization.setStatus(OrganizationStatus.NORMAL.name());
        save(organization);

        if (CollectionUtils.isNotEmpty(cmd.getBizType())) {
            organizationBizTypeService.setBizTypes(organization.getId(), cmd.getBizType());
        }
    }

    @Override
    public void update(UpdateOrganizationCmd cmd) {
        Organization organization = getById(cmd.getId());


        if (StringUtils.isNotEmpty(cmd.getCode()) && !Objects.equals(organization.getCode(), cmd.getCode())) {
            Organization exists = getOne(Wrappers.<Organization>query().eq(Organization.CODE, cmd.getCode()));
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + cmd.getCode() + " 已存在");
            }
        }

        BeanCopyUtils.copyNonNullProperties(cmd, organization);
        organization.setStatus(OrganizationStatus.NORMAL.name());
        saveOrUpdate(organization);
        organizationBizTypeService.setBizTypes(organization.getId(), cmd.getBizType());
    }

    @Override
    @Transactional
    public void submit(SubmitOrganizationCmd cmd) {
        Organization organization;
        if (StringUtils.isNotEmpty(cmd.getId())) {
            //有id更新以有的单位
            organization = getById(cmd.getId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到单位：" + cmd.getName());
            }
        } else {
            organization = new Organization();
        }

        if (StringUtils.isNotEmpty(cmd.getCode()) && !Objects.equals(organization.getCode(), cmd.getCode())) {
            Organization exists = getOne(Wrappers.<Organization>query().eq(Organization.CODE, cmd.getCode()));
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + cmd.getCode() + " 已存在");
            }
        }

        BeanCopyUtils.copyNonNullProperties(cmd, organization);
        if (Objects.isNull(organization.getStatus())) {
            organization.setStatus(OrganizationStatus.NORMAL.name());
        }

        saveOrUpdate(organization);

        //设置业务类型
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(cmd.getBizType())) {
            organizationBizTypeService.setBizTypes(organization.getId(), cmd.getBizType());
        }
    }

    @Override
    public void save(SaveOrganizationCmd cmd) {
        Organization organization;
        if (StringUtils.isNotEmpty(cmd.getId())) {
            //有id更新以有的单位
            organization = getById(cmd.getId());
            if (organization == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到单位：" + cmd.getName());
            }
        } else {
            //之前已经有保存过的记录直接使用，否则新建一个
            PrincipalDetails details = PrincipalUtils.getOrThrow();
            organization =
                    getOne(
                            Wrappers.<Organization>query()
                                    .eq(Organization.CREATE_BY, details.getId())
                                    .eq(Organization.STATUS, OrganizationStatus.TEMPORARY.name())
                    );

            //没有已保存的单位 ， 创建一个新的
            if (organization == null) {
                organization = new Organization();
            }
        }

        if (StringUtils.isNotEmpty(cmd.getCode()) && !Objects.equals(organization.getCode(), cmd.getCode())) {
            Organization exists = getOne(Wrappers.<Organization>query().eq(Organization.CODE, cmd.getCode()));
            if (exists != null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "单位编号：" + cmd.getCode() + " 已存在");
            }
        }

        BeanCopyUtils.copyNonNullProperties(cmd, organization);
        organization.setStatus(OrganizationStatus.TEMPORARY.name());
        saveOrUpdate(organization);
    }

    @Override
    public OrganizationDTO getSavedOrganization() {
        PrincipalDetails details = PrincipalUtils.getOrThrow();
        Organization organization = getOne(
                Wrappers.<Organization>query()
                        .eq(Organization.CREATE_BY, details.getId())
                        .eq(Organization.STATUS, OrganizationStatus.TEMPORARY.name())
        );
        return organizationConvert.convertDTO(organization);
    }

    @Override
    public OrganizationDTO getDetail(String id) {
        Organization organization = getBaseMapper().getDetail(id);
        return organizationConvert.convertDTO(organization);
    }

    @Override
    public IPage<OrganizationDTO> listPage(OrganizationQuery query) {
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), query)
                .convert(organizationConvert::convertDTO);
    }

    @Override
    public List<OrganizationDTO> list(OrganizationQuery query) {
        return getBaseMapper().list(query)
                .stream()
                .map(organizationConvert::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationDTO> listAll() {
        return list(
                Wrappers.<Organization>query().eq(Organization.STATUS, OrganizationStatus.NORMAL.name())
        ).stream().map(organizationConvert::convertDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrganizationDTO> listTechOrgAll() {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.setPid(details.getOrgId());
        organizationQuery.setStatus(OrganizationStatus.NORMAL.toString());
        //只查技术单位
        organizationQuery.setBizType(
                OrganizationBizTypeEnum.getTechOrganizationList()
                        .stream()
                        .map(OrganizationBizTypeEnum::getValue)
                        .collect(Collectors.toList())
        );
        return list(organizationQuery);
    }

    @Override
    public void delete(DeleteOrganizationCmd cmd) {
        removeById(cmd.getId());
        organizationBizTypeService.removeBizTypes(cmd.getId());
    }

    @Override
    public void deleteOrg(DeleteOrganizationCmd cmd) {
        // TODO 判断该单位是否参与了阶段任务（至少一个） 提示“单位【XXXXXX】已参与了【N】个任务，无法删除！”
        removeById(cmd.getId());
    }


    @Override
    public IPage<OrganizationAdminUserDTO> listOrganPage(OrganizationAdminUserQuery query) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        OrganizationQuery organizationQuery = new OrganizationQuery();
        BeanCopyUtils.copyNonNullProperties(query, organizationQuery);
        organizationQuery.setPid(details.getOrgId());
        //只差行政单位
        if (StringUtils.isNotEmpty(query.getBizType())) {
            organizationQuery.setBizType(Lists.newArrayList(query.getBizType()));
        } else {
            organizationQuery.setBizType(Lists.newArrayList(OrganizationBizTypeEnum.GOVERNMENT.getValue()));
        }
        return getBaseMapper().listPage(new Page<>(query.getPageIndex(), query.getPageSize()), organizationQuery)
                .convert(this::convertAdmin);
    }

    @Override
    public List<OrganizationDTO> listGovernmentOrgAll() {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        OrganizationQuery organizationQuery = new OrganizationQuery();
        organizationQuery.setPid(details.getOrgId());
        organizationQuery.setStatus(OrganizationStatus.NORMAL.toString());
        //只查行政管理单位
        organizationQuery.setBizType(Lists.newArrayList(OrganizationBizTypeEnum.GOVERNMENT.getValue()));
        return list(organizationQuery);
    }

    @Override
    public OrganizationAdminUserDTO getOrgDetail(String orgId) {
        Organization organization = getBaseMapper().getDetail(orgId);
        return convertAdmin(organization);
    }

    /**
     * 获取辖区内的行政管理单位
     * @param provinceCode
     * @param cityCode
     * @param districtCode
     * @return
     */
    public OrganizationDTO getGovernmentOrg(String provinceCode, String cityCode, String districtCode){
        OrganizationQuery query = new OrganizationQuery();
        query.setBizType(Lists.newArrayList(OrganizationBizTypeEnum.GOVERNMENT.getValue()));
        if (StringUtils.isNotEmpty(provinceCode)) {
            query.setProvinceCode(provinceCode);
        }
        if (StringUtils.isNotEmpty(cityCode)) {
            query.setCityCode(cityCode);
        }
        if (StringUtils.isNotEmpty(districtCode)) {
            query.setDistrictCode(districtCode);
        }
        List<OrganizationDTO> organizationDTOS = list(query);
        if (CollectionUtils.isEmpty(organizationDTOS)) {
            //找上级市的
            query.setDistrictCode("");
            organizationDTOS = list(query).stream().filter(organizationDTO -> StringUtils.isEmpty(organizationDTO.getDistrictCode())).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(organizationDTOS)) {
            //找上级省的
            query.setCityCode("");
            organizationDTOS = list(query).stream().filter(organizationDTO -> StringUtils.isEmpty(organizationDTO.getDistrictCode()) && StringUtils.isEmpty(organizationDTO.getCityCode()))
                    .collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(organizationDTOS)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到该辖区的行政管理单位");
        }
        if(organizationDTOS.size() != 1){
            throw BizRuntimeException.from(ResultCode.ERROR, "该辖区有多个行政管理单位，无法创建");
        }

        return organizationDTOS.get(0);
    }

    /**
     * 查询下级的行政管理单位
     *
     * @param orgId
     * @return
     */
    @Override
    public List<String> getGovernmentOrgId(String orgId) {
        OrganizationQuery query = new OrganizationQuery();
        query.setBizType(Lists.newArrayList(OrganizationBizTypeEnum.GOVERNMENT.getValue()));
        query.setPid(orgId);
        return getBaseMapper().list(query).stream().map(a -> a.getId()).collect(Collectors.toList());
    }

    /**
     * 根据条件查询布点质控单位
     *
     * @param query
     * @return
     */
    @Override
    public IPage<Organization> qualityControlOrgPage(QualityControlOrgList query) {
        return baseMapper.qualityControlOrgPage(new Page<>(query.getPageIndex(), query.getPageSize()), query);
    }

    /**
     * 转成成dto
     *
     * @param organization
     * @return
     */
    public static OrganizationDTO convert(Organization organization) {
        if (organization == null) {
            return null;
        }

        OrganizationDTO dto = new OrganizationDTO();
        BeanCopyUtils.copyNonNullProperties(organization, dto);
        return dto;
    }

    /**
     * 转成成dto
     *
     * @param organization
     * @return
     */
    public OrganizationAdminUserDTO convertAdmin(Organization organization) {
        if (organization == null) {
            return null;
        }

        OrganizationAdminUserDTO dto = new OrganizationAdminUserDTO();
        BeanCopyUtils.copyNonNullProperties(organization, dto);
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(organization.getBizType())) {
            dto.setBizType(
                    organization.getBizType().stream().map(OrganizationBizType::getBizType).collect(Collectors.toList())
            );
            dto.setBizTypeDesc(new ArrayList<>());

            Map<String, DictDTO> dictMap = dictService.getDictValueMap("organization_type");
            dto.getBizType().forEach(s -> {
                DictDTO dictDTO = dictMap.get(s);
                if (dictDTO != null) {
                    dto.getBizTypeDesc().add(dictDTO.getName());
                }
            });
        }
        return dto;
    }
}
