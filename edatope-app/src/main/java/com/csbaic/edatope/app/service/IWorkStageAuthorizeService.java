package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.WorkStageAuthorize;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.command.UpdateWorkStageAuthorizeCmd;
import com.csbaic.edatope.app.model.dto.TechOrganizationAuthorizeDTO;
import com.csbaic.edatope.app.model.dto.WorkStageAuthorizeDTO;
import com.csbaic.edatope.app.model.query.WorkStageAuthorizeQuery;

import java.util.List;

/**
 * <p>
 * 工作阶段授权表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-04-01
 */
public interface IWorkStageAuthorizeService extends IService<WorkStageAuthorize> {

    public void create(CreateWorkStageAuthorizeCmd cmd);

    public IPage<WorkStageAuthorizeDTO> listPage(WorkStageAuthorizeQuery query);

    public WorkStageAuthorizeDTO getAuthorizeDetail(String id);

    public void update(UpdateWorkStageAuthorizeCmd cmd);

    public void delete(String id);

}
