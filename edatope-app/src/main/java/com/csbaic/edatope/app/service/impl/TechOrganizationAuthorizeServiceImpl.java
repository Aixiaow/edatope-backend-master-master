package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.app.convert.RoleConvert;
import com.csbaic.edatope.app.entity.Organization;
import com.csbaic.edatope.app.entity.TechOrganizationAuthorize;
import com.csbaic.edatope.app.entity.TechOrganizationAuthorizeCity;
import com.csbaic.edatope.app.enums.ServiceLevelEnum;
import com.csbaic.edatope.app.mapper.TechOrganizationAuthorizeMapper;
import com.csbaic.edatope.app.model.command.CreateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.UpdateAuthorizeStatusCmd;
import com.csbaic.edatope.app.model.command.UpdateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.dto.TechOrgAuthDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationAuthorizeDTO;
import com.csbaic.edatope.app.service.*;
import com.csbaic.edatope.app.utils.UserUtils;
import com.csbaic.edatope.auth.principal.UserPrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.common.utils.StringSplitUtils;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;
import com.csbaic.edatope.dict.service.IDictService;
import com.csbaic.edatope.dict.service.impl.AreaService;
import com.csbaic.edatope.dict.utils.AreaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 技术单位授权表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-03-05
 */
@Service
public class TechOrganizationAuthorizeServiceImpl extends ServiceImpl<TechOrganizationAuthorizeMapper, TechOrganizationAuthorize> implements ITechOrganizationAuthorizeService {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IDictService dictService;

    @Autowired
    private RoleConvert roleConvert;

    @Autowired
    private ITechOrganizationAuthorizeCityService authorizeCityService;
    @Autowired
    private AreaService areaService;

    @Override
    @Transactional
    public void create(CreateOrgAuthorizeCmd cmd) {
        String orgId = UserUtils.getUserOrgId();
        if (StringUtils.isEmpty(orgId)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前用户没有单位，无法授权");
        }

        TechOrganizationAuthorize authorizeInfo = getOne(
                Wrappers.<TechOrganizationAuthorize>query()
                        .eq(TechOrganizationAuthorize.ORG_ID, cmd.getOrgId())
                        .eq(TechOrganizationAuthorize.OWNER_ID, orgId)
        );
        if(authorizeInfo != null){
            throw BizRuntimeException.from(ResultCode.ERROR, "该单位已经授权，请不要重复授权");
        }

        TechOrganizationAuthorize authorize = new TechOrganizationAuthorize();
        authorize.setEnabled(true);
        authorize.setOrgId(cmd.getOrgId());
        authorize.setUserId(cmd.getUserId());
        authorize.setRoleId(cmd.getRoleId());
        authorize.setOwnerId(orgId);
        authorize.setAreaCode(StringSplitUtils.join(",", cmd.getAreaCodeList()));

        /*if(StringUtils.isNotEmpty(cmd.getRoleId())){
            roleService.appendRoleToUser(cmd.getUserId(), Lists.newArrayList(cmd.getRoleId()));
        }*/

        save(authorize);
        saveAuthorizeCity(authorize.getId(), cmd.getAreaCodeList());
    }

    @Override
    public List<TechOrganizationAuthorizeDTO> listAll( ) {
        String orgId = UserUtils.getUserOrgId();
        QueryWrapper<TechOrganizationAuthorize> wrapper = Wrappers.<TechOrganizationAuthorize>query().eq(TechOrganizationAuthorize.OWNER_ID, orgId);
        List<TechOrganizationAuthorize> authorizeList = list(wrapper);
        return authorizeList.stream().map(this::convert).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateAuthorizeStatus(UpdateAuthorizeStatusCmd cmd) {
        TechOrganizationAuthorize authorize = getById(cmd.getId());
        if(authorize == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到授权信息");
        }
        authorize.setEnabled(cmd.getEnabled());
        updateById(authorize);
    }

    @Override
    public void update(UpdateOrgAuthorizeCmd cmd) {
        TechOrganizationAuthorize authorize = getById(cmd.getId());
        if(authorize == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到授权信息");
        }

        BeanCopyUtils.copyNonNullProperties(cmd, authorize);
        if (CollectionUtils.isNotEmpty(cmd.getAreaCodeList())) {
            authorize.setAreaCode(StringSplitUtils.join(",", cmd.getAreaCodeList()));
        }
        if (StringUtils.isNotEmpty(cmd.getRoleId()) && !Objects.equals(cmd.getRoleId(), authorize.getRoleId())) {
            authorize.setRoleId(cmd.getRoleId());
        }
        updateById(authorize);
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(cmd.getAreaCodeList())) {
            saveAuthorizeCity(authorize.getId(), cmd.getAreaCodeList());
        }
    }

    @Override
    @Transactional
    public void delete(DeleteOrgAuthorizeCmd cmd) {
        TechOrganizationAuthorize authorize = getById(cmd.getId());
        if(authorize == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到授权信息");
        }

        removeById(cmd.getId());
    }

    @Override
    public DictDTO listAreaByOrgId() {
        UserPrincipalDetails details = (UserPrincipalDetails) PrincipalUtils.getOrThrow();
        if (StringUtils.isEmpty(details.getOrgId()) && !details.getAdmin()) {
            throw BizRuntimeException.from(ResultCode.ERROR, "当前登陆用户不属于任何单位");
        }
        OrganizationDTO organizationDTO = organizationService.getDetail(details.getOrgId());
        String code = null;
        if (organizationDTO.getServiceLevel().equals(ServiceLevelEnum.COUNTRY_LEVEL)) {
            code = organizationDTO.getProvinceCode();
        } else if (organizationDTO.getServiceLevel().equals(ServiceLevelEnum.CITY_LEVEL)) {
            code = organizationDTO.getCityCode();
        } else if (organizationDTO.getServiceLevel().equals(ServiceLevelEnum.DISTRICT_LEVEL)) {
            code = organizationDTO.getDistrictCode();
        }

        return dictService.getAreaDetailByCode(code);
    }

    /**
     * 获取当前技术管理单位被授权单位列表
     *
     * @return
     */
    @Override
    public List<TechOrgAuthDTO> getTechOrgAuthList() {
        String orgId = UserUtils.getUserOrgId();
        QueryWrapper<TechOrganizationAuthorize> wrapper = Wrappers.<TechOrganizationAuthorize>query()
                .eq(TechOrganizationAuthorize.ORG_ID, orgId).eq(TechOrganizationAuthorize.ENABLED,1);
        List<TechOrganizationAuthorize> authorizeList = list(wrapper);
        return authorizeList.stream().map(this::convertTechOrgAuthDTO).collect(Collectors.toList());
    }

    /**
     * 根据归属单位和技术单位
     *
     * @param ownerId
     * @param orgId
     * @return
     */
    @Override
    public TechOrganizationAuthorize getAuthorizeCityByOwnerIdAndOrgId(String ownerId, String orgId) {
        QueryWrapper<TechOrganizationAuthorize> wrapper = Wrappers.<TechOrganizationAuthorize>query()
                .eq(TechOrganizationAuthorize.ORG_ID, orgId)
                .eq(TechOrganizationAuthorize.OWNER_ID, ownerId)
                .eq(TechOrganizationAuthorize.ENABLED, 1);
        return getBaseMapper().selectOne(wrapper);
    }

    /**
     * 根据授权区域和授权单位查询技术单位
     *
     * @param ownerId
     * @return
     */
    @Override
    public List<String> getOrgIdByCityAndOwnId(String ownerId, String provinceCode, String cityCode, String districtCode) {
        return getBaseMapper().getOrgIdByCityAndOwnId(ownerId,provinceCode,cityCode,districtCode);
    }


    public void saveAuthorizeCity(String authorizeId, Set<String> codes) {
        authorizeCityService.remove(
                Wrappers.<TechOrganizationAuthorizeCity>query().eq(TechOrganizationAuthorizeCity.AUTHORIZE_ID, authorizeId)
        );
        List<AreaDTO> areas = new ArrayList<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(codes)) {
            for (String area : codes) {
                //添加选择的区域
                areas.add(dictService.getFlatAreaDetailByCode(area));
                //如果选择的是省，或者市，按省、市添加下级区域
                if (AreaUtils.isProvince(area)) {
                    areas.addAll(
                            areaService.listAreaByPrefix(
                                    area.substring(0, 2)
                            )
                    );
                } else if (AreaUtils.isCity(area)) {
                    areas.addAll(
                            areaService.listAreaByPrefix(
                                    area.substring(0, 4)
                            )
                    );
                }
            }
        }


        List<TechOrganizationAuthorizeCity> cities = new ArrayList<>();
        areas.forEach(new Consumer<AreaDTO>() {
            @Override
            public void accept(AreaDTO areaDTO) {
                TechOrganizationAuthorizeCity city = new TechOrganizationAuthorizeCity();
                BeanCopyUtils.copyNonNullProperties(areaDTO, city);
                city.setAuthorizeId(authorizeId);
                cities.add(city);
            }
        });
        authorizeCityService.saveBatch(cities);
    }

    /**
     * 转换
     *
     * @param authorize
     * @return
     */
    private TechOrganizationAuthorizeDTO convert(TechOrganizationAuthorize authorize) {
        TechOrganizationAuthorizeDTO dto = new TechOrganizationAuthorizeDTO();
        dto.setEnabled(authorize.getEnabled());
        dto.setAresCodeList(StringSplitUtils.splitToSet(",", authorize.getAreaCode()));
        dto.setOrganization(organizationService.getDetail(authorize.getOrgId()));
        dto.setUser(userService.getUserDetail(authorize.getUserId()));
        if (StringUtils.isNotEmpty(authorize.getRoleId())) {
            dto.setRoleDTO(roleConvert.convertToViewObject(roleService.getById(authorize.getRoleId())));
        }
        dto.setAreaList(new ArrayList<>());
        dto.getAresCodeList().forEach(s -> {
            AreaDTO areaDTO = dictService.getFlatAreaDetailByCode(s);
            if(areaDTO != null){
                dto.getAreaList().add(areaDTO);
            }
        });
        dto.setId(authorize.getId());
        return dto;
    }

    private TechOrgAuthDTO convertTechOrgAuthDTO(TechOrganizationAuthorize authorize){
        TechOrgAuthDTO techOrgAuthDTO = new TechOrgAuthDTO();
        Organization organization = organizationService.getById(authorize.getOwnerId());
        techOrgAuthDTO.setName(organization.getName());
        techOrgAuthDTO.setId(organization.getId());
        return techOrgAuthDTO;
    }
}
