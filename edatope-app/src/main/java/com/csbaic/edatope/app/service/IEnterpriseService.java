package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.Enterprise;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.dto.EnterpriseVO;
import com.csbaic.edatope.app.model.query.EnterpriseQuery;

import java.util.List;

/**
 * <p>
 * 被调查的企业 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
public interface IEnterpriseService extends IService<Enterprise> {

    List<EnterpriseVO> listByName(EnterpriseQuery query);

    EnterpriseVO getDetailById(String id);
}
