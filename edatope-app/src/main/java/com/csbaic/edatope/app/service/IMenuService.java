package com.csbaic.edatope.app.service;

import com.csbaic.edatope.app.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.csbaic.edatope.app.model.command.DeleteMenuCmd;
import com.csbaic.edatope.app.model.command.CreateMenuCmd;
import com.csbaic.edatope.app.model.command.UpdateMenuCmd;
import com.csbaic.edatope.app.model.dto.MenuDTO;

import java.util.List;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 创建菜单
     *
     * @param cmd
     */
    void create(CreateMenuCmd cmd);

    /**
     * 获取所有的菜单
     * @return
     */
    List<MenuDTO> listMenu();


    /**
     * 批量更新菜单
     * @param cmd
     */
    void updateMenu(UpdateMenuCmd cmd);

    /**
     * 批量删除
     * @param cmd
     */
    void deleteMenu(DeleteMenuCmd cmd);

    /**
     * 获取后台导航菜单
     *
     * @return
     */
    List<MenuDTO> listUserMenu();

    /**
     * 获取菜单详情
     * @param id
     * @return
     */
    MenuDTO getMenuById(String id);


}
