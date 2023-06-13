package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.TechOrganizationAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.entity.TechOrganizationAuthorizeCity;
import com.csbaic.edatope.app.model.command.CreateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.DeleteOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.UpdateAuthorizeStatusCmd;
import com.csbaic.edatope.app.model.command.UpdateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.TechOrgAuthDTO;
import com.csbaic.edatope.app.model.dto.TechOrganizationAuthorizeDTO;
import com.csbaic.edatope.common.dto.PageQuery;
import com.csbaic.edatope.dict.model.dto.DictDTO;

import java.util.List;

/**
 * <p>
 * 技术单位授权表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-05
 */
public interface ITechOrganizationAuthorizeService extends IService<TechOrganizationAuthorize> {


    /**
     * 授权
     * @param cmd
     */
    void create(CreateOrgAuthorizeCmd cmd);

    /**
     * 获取授权的技术单位
     * @param
     * @return
     */
    List<TechOrganizationAuthorizeDTO> listAll( );


    /**
     * 更新授权状态
     * @param cmd
     */
    void updateAuthorizeStatus(UpdateAuthorizeStatusCmd cmd);

    /**
     * 更新授权
     * @param cmd
     */
    void update(UpdateOrgAuthorizeCmd cmd);

    /**
     * 删除授权
     * @param cmd
     */
    void delete(DeleteOrgAuthorizeCmd cmd);

    DictDTO listAreaByOrgId();

    /**
     * 获取当前技术管理单位被授权单位列表
     * @return
     */
    List<TechOrgAuthDTO> getTechOrgAuthList();

    /**
     * 根据归属单位和技术单位
     * @param ownerId
     * @param orgId
     * @return
     */
    TechOrganizationAuthorize getAuthorizeCityByOwnerIdAndOrgId(String ownerId, String orgId);

    /**
     * 根据授权区域和授权单位查询技术单位
     * @param ownerId
     * @return
     */
    List<String> getOrgIdByCityAndOwnId(String ownerId, String provinceCode, String cityCode, String districtCode);
}
