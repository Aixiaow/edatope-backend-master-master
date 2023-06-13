package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.*;
import com.csbaic.edatope.app.model.dto.OrganizationAdminUserDTO;
import com.csbaic.edatope.app.model.dto.OrganizationDTO;
import com.csbaic.edatope.app.model.query.OrganizationAdminUserQuery;
import com.csbaic.edatope.app.model.query.OrganizationQuery;
import com.csbaic.edatope.app.model.query.QualityControlOrgList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统组织机构表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-01-24
 */
public interface IOrganizationService extends IService<Organization> {

    /**
     * 创建单位
     *
     * @param cmd
     */
    void create(CreateOrganizationCmd cmd);

    /**
     * 更新
     *
     * @param cmd
     */
    void update(UpdateOrganizationCmd cmd);

    /**
     * 提交单位
     *
     * @param cmd
     */
    void submit(SubmitOrganizationCmd cmd);

    /**
     * 保存单位
     *
     * @param cmd
     */
    void save(SaveOrganizationCmd cmd);


    /**
     * 单位查询
     *
     * @return
     */
    IPage<OrganizationDTO> listPage(OrganizationQuery query);

    /**
     * 单位查询
     *
     * @return
     */
    List<OrganizationDTO> list(OrganizationQuery query);


    /**
     * 获取所有的单位
     *
     * @return
     */
    List<OrganizationDTO> listAll();

    List<OrganizationDTO> listTechOrgAll();

    /**
     * 获取用户保存的单位
     *
     * @return
     */
    OrganizationDTO getSavedOrganization();


    /**
     * 获取用户保存的单位
     *
     * @return
     */
    OrganizationDTO getDetail(String id);

    /**
     * 删除单位
     *
     * @param cmd
     */
    void delete(DeleteOrganizationCmd cmd);

    void deleteOrg(DeleteOrganizationCmd cmd);

    IPage<OrganizationAdminUserDTO> listOrganPage(OrganizationAdminUserQuery query);

    public List<OrganizationDTO> listGovernmentOrgAll();

    OrganizationAdminUserDTO getOrgDetail(String orgId);

    OrganizationDTO getGovernmentOrg(String provinceCode, String cityCode, String districtCode);

    /**
     * 查询下级的行政管理单位
     * @param orgId
     * @return
     */
    List<String> getGovernmentOrgId(String orgId);

    /**
     * 根据条件查询布点质控单位
     * @param query
     * @return
     */
    IPage<Organization> qualityControlOrgPage(QualityControlOrgList query);
}
