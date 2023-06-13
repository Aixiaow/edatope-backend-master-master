package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Specialist;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateSpecialistCmd;
import com.csbaic.edatope.app.model.dto.SpecialistPageResultVO;
import com.csbaic.edatope.app.model.dto.SpecialistUserVO;
import com.csbaic.edatope.app.model.dto.UserSelectResultVO;
import com.csbaic.edatope.app.model.query.SpecialistQuery;
import com.csbaic.edatope.app.model.query.SpecialistUserQuery;
import com.csbaic.edatope.app.model.query.UserListQuery;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 专家组表 服务类
 * </p>
 *
 * @author bnt
 * @since 2022-05-01
 */
public interface ISpecialistService extends IService<Specialist> {
    /**
     * 新建或维护专家组
     *
     * @param specialist
     */
    void create(CreateSpecialistCmd specialist);

    /**
     * 停用专家组
     *
     * @param id
     */
    void stop(String id, String status);

    /**
     * 删除专家组
     *
     * @param id
     */
    void delete(String id);

    /**
     * 布点质控专家组-列表-分页
     *
     * @param blockQuery
     * @return
     */
    IPage<SpecialistPageResultVO> page(SpecialistQuery blockQuery);

    /**
     * 选择选择质控专家列表-分页
     *
     * @return
     */
    IPage<UserSelectResultVO> userList(UserListQuery userListQuery);

    /**
     * 查看专家组
     *
     * @param id
     * @return
     */
    List<SpecialistUserVO> viewDetail(String id);

    /**
     * 布点质控专家-列表-分页
     *
     * @param query
     * @return
     */
    IPage<SpecialistUserVO> specialistUserPage(SpecialistUserQuery query);
}
