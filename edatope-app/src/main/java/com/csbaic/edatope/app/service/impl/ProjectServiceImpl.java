package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.entity.Project;
import com.csbaic.edatope.app.entity.TechOrganizationAuthorize;
import com.csbaic.edatope.app.enums.OrganizationBizTypeEnum;
import com.csbaic.edatope.app.enums.ServiceLevelEnum;
import com.csbaic.edatope.app.mapper.ProjectMapper;
import com.csbaic.edatope.app.model.command.CreateProjectCmd;
import com.csbaic.edatope.app.model.command.DeleteProjectCmd;
import com.csbaic.edatope.app.model.command.UpdateProjectCmd;
import com.csbaic.edatope.app.model.dto.*;
import com.csbaic.edatope.app.model.query.ProjectQuery;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.service.impl.AreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-19
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    private IDictService dictService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IUserService userService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ITechOrganizationAuthorizeService techOrganizationAuthorizeService;
    @Autowired
    private IBlockService blockService;

    @Override
    public void create(CreateProjectCmd cmd) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        Project project = new Project();
        BeanCopyUtils.copyNonNullProperties(cmd, project);
//        OrganizationDTO organizationDTO = organizationService.getGovernmentOrg(cmd.getProvinceCode(), cmd.getCityCode(), cmd.getDistrictCode());
        project.setOrgId(details.getOrgId());
        save(project);
    }

    @Override
    public void update(UpdateProjectCmd cmd) {
        Project project = getById(cmd.getId());
        if (project == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到项目");
        }


        BeanCopyUtils.copyNonNullProperties(cmd, project);
        updateById(project);
    }

    @Override
    public void delete(DeleteProjectCmd cmd) {
        Project project = getById(cmd.getId());
        if (project == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到项目");
        }

        removeById(cmd.getId());
    }


    @Override
    public List<ProjectDTO> listByName(String name) {
        QueryWrapper<Project> wrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.like(Project.NAME, name);
        }

        return list(wrapper)
                .stream().map(ProjectServiceImpl::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<ProjectDTO> page(ProjectQuery query) {
        QueryWrapper<Project> wrapper = Wrappers.query();
        if (StringUtils.isNotEmpty(query.getName())) {
            wrapper.like(Project.NAME, query.getName());
        }
        if (StringUtils.isNotEmpty(query.getProvinceCode())) {
            wrapper.eq(Project.PROVINCE_CODE, query.getProvinceCode());
        }
        if (StringUtils.isNotEmpty(query.getCityCode())) {
            wrapper.eq(Project.CITY_CODE, query.getCityCode());
        }
        if (StringUtils.isNotEmpty(query.getDistrictCode())) {
            wrapper.eq(Project.DISTRICT_CODE, query.getDistrictCode());
        }
        if (query.getBeginDate() != null) {
            wrapper.gt(Project.BEGIN_DATE, query.getBeginDate());
        }
        if (query.getEndDate() != null) {
            wrapper.lt(Project.END_DATE, query.getEndDate());
        }

        if (StringUtils.isNotEmpty(query.getOrderBy())) {
            if (StringUtils.isNotEmpty(query.getOrderType())) {
                wrapper.orderBy(true, Objects.equals("asc", query.getOrderType()), query.getOrderBy());
            } else {
                wrapper.orderByDesc(query.getOrderBy());
            }
        } else {
            wrapper.orderByDesc(Project.CREATE_TIME);
        }

        return page(new Page<>(query.getPageIndex(), query.getPageSize()), wrapper)
                .convert(ProjectServiceImpl::convertToDTO);
    }

    @Override
    public IPage<ProjectInfoDTO> listProjectInfo(ProjectQuery query) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        OrganizationDTO organizationDTO = organizationService.getDetail(details.getOrgId());
        QueryWrapper<Project> wrapper = Wrappers.query();

        if (organizationDTO.getBizType().contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            if (StringUtils.isNotEmpty(query.getProvinceCode())) {
                wrapper.eq(Project.PROVINCE_CODE, organizationDTO.getProvinceCode());
            }
            if (StringUtils.isNotEmpty(query.getCityCode())) {
                wrapper.eq(Project.CITY_CODE, organizationDTO.getCityCode());
            }
            if (StringUtils.isNotEmpty(query.getDistrictCode())) {
                wrapper.eq(Project.DISTRICT_CODE, organizationDTO.getDistrictCode());
            }
        }else{
            List<TechOrganizationAuthorize> authorizeList = techOrganizationAuthorizeService.list(
                    Wrappers.<TechOrganizationAuthorize>query().eq(TechOrganizationAuthorize.ORG_ID, organizationDTO.getId())
            );

            //不是行政管理单位，没有被授权
            if (CollectionUtils.isEmpty(authorizeList)) {
                return new Page<>();
            }

            Set<String> areaCode = authorizeList.stream().map(TechOrganizationAuthorize::getAreaCode).collect(Collectors.toSet());
            Set<String> splitAreaCode = new HashSet<>();
            for(String area: areaCode){
                splitAreaCode.addAll(org.springframework.util.StringUtils.commaDelimitedListToSet(area));
            }
            List<AreaDTO> areaDTOS = splitAreaCode
                    .stream()
                    .map(dictService::getFlatAreaDetailByCode)
                    .collect(Collectors.toList());

            wrapper.or(new Consumer<QueryWrapper<Project>>() {
                @Override
                public void accept(QueryWrapper<Project> projectQueryWrapper) {
                    for(AreaDTO areaDTO: areaDTOS){
                        if (StringUtils.isNotEmpty(areaDTO.getProvinceCode())) {
                            wrapper.eq(Project.PROVINCE_CODE, areaDTO.getProvinceCode());
                        }
                        if (StringUtils.isNotEmpty(areaDTO.getCityCode())) {
                            wrapper.eq(Project.CITY_CODE, areaDTO.getCityCode());
                        }
                        if (StringUtils.isNotEmpty(areaDTO.getDistrictCode())) {
                            wrapper.eq(Project.DISTRICT_CODE, areaDTO.getDistrictCode());
                        }
                    }
                }
            });
        }
        if (StringUtils.isNotEmpty(query.getName())) {
            wrapper.like(Project.NAME, query.getName());
        }

        if (query.getBeginDate() != null) {
            wrapper.gt(Project.BEGIN_DATE, query.getBeginDate());
        }
        if (query.getEndDate() != null) {
            wrapper.lt(Project.END_DATE, query.getEndDate());
        }
        wrapper.orderByDesc(Project.CREATE_TIME);

        return page(new Page<>(query.getPageIndex(), query.getPageSize()), wrapper)
                .convert(this::convertToProjectInfo);
    }

    @Override
    public List<DictDTO> getProjectCity(String areaCode) {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId())) {
            return null;
        }

        OrganizationDTO organization = organizationService.getDetail(details.getOrgId());
        UserDTO user = userService.getUserDetail(details.getId());
        //判断单位是否是行政管理单位
        if (organization.getBizType().contains(OrganizationBizTypeEnum.GOVERNMENT.getValue())) {
            //是行政管理单位，根据

            if (Objects.equals(organization.getServiceLevel(), ServiceLevelEnum.COUNTRY_LEVEL.toString())) {
                //国家级，取所有的省份
                return areaService.findAreaDetailByCode(areaCode, areaService::listAll);
            } else if (Objects.equals(organization.getServiceLevel(), ServiceLevelEnum.PROVINCE_LEVEL.toString())) {
                return areaService.findAreaDetailByCode(areaCode, () -> areaService.listByAreaCode(organization.getProvinceCode()));
            } else if (Objects.equals(organization.getServiceLevel(), ServiceLevelEnum.CITY_LEVEL.toString())) {
                return areaService.findAreaDetailByCode(areaCode, () -> areaService.listByAreaCode(organization.getCityCode()));
            } else {
                //区县级
                return areaService.findAreaDetailByCode(areaCode, () -> areaService.listByAreaCode(organization.getDistrictCode()));
            }
        } else {
            //技术单位
            List<TechOrganizationAuthorize> authorizeList = techOrganizationAuthorizeService.list(
                    Wrappers.<TechOrganizationAuthorize>query().eq(TechOrganizationAuthorize.ORG_ID, organization.getId())
            );
            Set<String> areaCodes = new HashSet<>();
            for (TechOrganizationAuthorize authorize : authorizeList) {
                Set<String> areaList = org.springframework.util.StringUtils.commaDelimitedListToSet(authorize.getAreaCode());
                areaCodes.addAll(areaList);
            }

            return areaService.findAreaDetailByCode(areaCode, () -> areaService.listAreaInAreaCode(areaCodes));
        }
    }


    @Override
    public ProjectDTO getProjectById(String id) {
        Project project = getById(id);
        if (project == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到项目");
        }


        return convertToDTO(project);
    }

    @Override
    public ProjectDTO getProjectByName(String name) {
        Project project = getOne(
                Wrappers.<Project>query()
                .eq(Project.NAME, name)
        );

        return project != null ? convertToDTO(project) : null;
    }



    public static ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        BeanCopyUtils.copyNonNullProperties(project, projectDTO);
        return projectDTO;
    }

    public   ProjectInfoDTO convertToProjectInfo(Project project){
        ProjectInfoDTO projectInfoDTO = new ProjectInfoDTO();
        BeanCopyUtils.copyNonNullProperties(project, projectInfoDTO);
        Set<String> enterpriseSet = new HashSet<>();
        List<BlockVO> blocks = blockService.listBlockByProjectId(project.getId());
        blocks.forEach(new Consumer<BlockVO>() {
            @Override
            public void accept(BlockVO blockVO) {
                enterpriseSet.add(blockVO.getEnterprise().getCode());
                projectInfoDTO.setBlockCount(projectInfoDTO.getBlockCount()+ 1);
                projectInfoDTO.setWorkStageCount(projectInfoDTO.getWorkStageCount() + blockVO.getWorkStageCount());
            }
        });
        projectInfoDTO.setEnterpriseCount(enterpriseSet.size());
        return projectInfoDTO;
    }
}
