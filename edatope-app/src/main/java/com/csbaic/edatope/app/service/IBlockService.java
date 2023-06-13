package com.csbaic.edatope.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.Block;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateBlockCmd;
import com.csbaic.edatope.app.model.command.DeleteBlockCmd;
import com.csbaic.edatope.app.model.command.UpdateBlockCmd;
import com.csbaic.edatope.app.model.dto.BlockImportCheckResult;
import com.csbaic.edatope.app.model.dto.BlockVO;
import com.csbaic.edatope.app.model.dto.EnterpriseVO;
import com.csbaic.edatope.app.model.dto.PointUserTasksResultVO;
import com.csbaic.edatope.app.model.query.BlockQuery;
import com.csbaic.edatope.app.model.query.EnterpriseQuery;
import com.csbaic.edatope.app.model.query.PointUserBlockQuery;
import com.csbaic.edatope.app.model.query.QualityControlTasksQuery;
import com.csbaic.edatope.common.result.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 地块 服务类
 * </p>
 *
 * @author bage
 * @since 2022-03-26
 */
public interface IBlockService extends IService<Block> {

    /**
     * 创建地址
     *
     * @param cmd
     */
    void create(CreateBlockCmd cmd);

    /**
     * 更新地块
     *
     * @param cmd
     */
    void update(UpdateBlockCmd cmd);

    /**
     * 删除
     *
     * @param cmd
     */
    void delete(DeleteBlockCmd cmd);

    /**
     * 地块查询
     *
     * @param blockQuery
     * @return
     */
    IPage<BlockVO> listPage(BlockQuery blockQuery);

    /**
     * 布点人员地块查询
     *
     * @param blockQuery
     * @return
     */
    IPage<BlockVO> pointUserListPage(PointUserBlockQuery blockQuery);

    /**
     * 布点人员地块查询
     *
     * @param blockQuery
     * @return
     */
    IPage<BlockVO> pointUserTaskListPage(PointUserBlockQuery blockQuery);

    /**
     * 地块查询
     *
     * @param blockQuery
     * @return
     */
    List<BlockVO> list(BlockQuery blockQuery);


    List<BlockVO> listByEnterpriseId(String  enterpriseId);

    List<BlockVO> listBlockByProjectId(String projectId);
    /**
     * 地块详情
     * @param id
     * @return
     */
    BlockVO getDetailById(String id);
    /**
     * 查询企业
     *
     * @param query
     * @return
     */
    List<EnterpriseVO> listEnterpriseByName(EnterpriseQuery query);


    IPage<BlockVO> qualityControlTaskPage(QualityControlTasksQuery query);

    IPage<BlockVO> sendBackPage(QualityControlTasksQuery query);

    /**
     *  布点质控专家组查询列表
     * @param query
     * @return
     */
    IPage<BlockVO> specialistPage(QualityControlTasksQuery query);
}
