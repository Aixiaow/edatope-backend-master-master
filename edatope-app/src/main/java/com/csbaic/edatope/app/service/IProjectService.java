package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateProjectCmd;
import com.csbaic.edatope.app.model.command.DeleteProjectCmd;
import com.csbaic.edatope.app.model.command.UpdateProjectCmd;
import com.csbaic.edatope.app.model.dto.ProjectDTO;
import com.csbaic.edatope.app.model.dto.ProjectInfoDTO;
import com.csbaic.edatope.app.model.query.ProjectQuery;
import com.csbaic.edatope.dict.model.dto.AreaDTO;
import com.csbaic.edatope.dict.model.dto.DictDTO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-19
 */
public interface IProjectService extends IService<Project> {

    void create(CreateProjectCmd cmd);

    void update(UpdateProjectCmd cmd);

    void delete(DeleteProjectCmd cmd);

    IPage<ProjectDTO> page(ProjectQuery query);

    IPage<ProjectInfoDTO> listProjectInfo(ProjectQuery query);

    List<ProjectDTO> listByName(String name);

    /**
     * 获取可用的区域列表
     *
     * @return
     */
    List<DictDTO> getProjectCity(String areaCode);


    ProjectDTO getProjectById(String id);

    /**
     *  通过名称获取项目
     * @param name
     * @return
     */
    ProjectDTO getProjectByName(String name);
}
