package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.WorkStage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateOrgAuthorizeCmd;
import com.csbaic.edatope.app.model.command.CreateStageCmd;
import com.csbaic.edatope.app.model.command.UpdateTechOrganizationCmd;
import com.csbaic.edatope.app.model.dto.TechOrganizationDTO;
import com.csbaic.edatope.app.model.dto.WorkStageDTO;
import com.csbaic.edatope.app.model.query.TechOrganizationQuery;
import com.csbaic.edatope.app.model.query.WorkStageQuery;

import java.util.List;

/**
 * <p>
 * 工作阶段表 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-31
 */
public interface IWorkStageService extends IService<WorkStage> {

    void create(CreateStageCmd cmd);

    public IPage<WorkStageDTO> findListPage(WorkStageQuery query);

    public List<WorkStageDTO> findAll();

    WorkStageDTO getWorkStageDetail(String id);

    public void update(WorkStageDTO dto);

    public void delete(String id);
}
