package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.OrganizationBizType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统组织机构业务类型表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-07
 */
public interface IOrganizationBizTypeService extends IService<OrganizationBizType> {

    /**
     * 设置单位的业务类型
     *
     * @param orgId
     * @param bizTypes
     */
    void setBizTypes(String orgId, List<String> bizTypes);

    /**
     * 获取单位的业务类型
     *
     * @param orgId
     * @return
     */
    List<String> getBizTypes(String orgId);

    void removeBizTypes(String orgId);
}
