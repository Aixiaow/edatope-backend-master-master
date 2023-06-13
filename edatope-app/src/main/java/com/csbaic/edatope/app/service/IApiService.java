package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.Api;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.CreateApiCmd;
import com.csbaic.edatope.app.model.command.DeleteApiCmd;
import com.csbaic.edatope.app.model.command.UpdateApiCmd;
import com.csbaic.edatope.app.model.dto.ApiDTO;

import java.util.List;

/**
 * <p>
 * 系统接口表 服务类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface IApiService extends IService<Api> {



    /**
     * 获取匹配成功的接口
     * @param path
     * @return
     */
    Api getMatchedApi(String path);

    /**
     * 创建接口
     * @param cmd
     */
    void create(CreateApiCmd cmd);

    /**
     * 获取api详情
     * @param id
     * @return
     */
    ApiDTO getApiDetail(String id);

    /**
     * 批量更新
     *
     * @param cmds
     */
    void update(UpdateApiCmd cmds);

    /**
     * 获取接口列表
     *
     * @return
     */
    List<ApiDTO> listApi();

    /**
     * 批量删除
     *
     * @param cmds
     */
    void delete(DeleteApiCmd cmds);
 
}
