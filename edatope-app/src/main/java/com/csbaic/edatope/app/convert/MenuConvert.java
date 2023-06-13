package com.csbaic.edatope.app.convert;

import com.csbaic.edatope.app.entity.Menu;
import com.csbaic.edatope.app.model.command.CreateMenuCmd;
import com.csbaic.edatope.app.model.command.UpdateMenuCmd;
import com.csbaic.edatope.app.model.dto.MenuDTO;
import com.csbaic.edatope.common.utils.BeanCopyUtils;
import com.csbaic.edatope.app.model.dto.MenuMetaDTO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class MenuConvert {

    @Autowired
    private PermissionConvert permissionConvert;

    /**
     * 转换成vo
     * @param menu
     * @return
     */
    public MenuDTO convertToDTO(Menu menu){
        MenuDTO menuDTO = new MenuDTO();
        BeanUtils.copyProperties(menu, menuDTO);
        //子菜单
        if (CollectionUtils.isNotEmpty(menu.getChildren())) {
            menuDTO.setChildren(
                    menu.getChildren().stream().map(this::convertToDTO).collect(Collectors.toList())
            );
        } else {
            menuDTO.setChildren(new ArrayList<>());
        }
        if (menu.getPermission() != null) {
            menuDTO.setPermission(
                    permissionConvert.convertToViewObject(menu.getPermission())
            );
        }
        if (menu.getParent() != null) {
            menuDTO.setParent(convertToDTO(menu.getParent()));
        }

        MenuMetaDTO menuMetaDTO = new MenuMetaDTO();
        menuMetaDTO.setIcon(menu.getIcon());
        menuMetaDTO.setTitle(menu.getName());
        menuDTO.setMeta(menuMetaDTO);
        return menuDTO;
    }

    /**
     * 转换成实体
     *
     * @param cmd
     * @return
     */
    public Menu convertToEntity(CreateMenuCmd cmd) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(cmd, menu);
        return menu;
    }

    /**
     * 更新菜单
     * @param menu
     * @param cmd
     * @return
     */
    public  void convertUpdateMenuCommandToEntity(Menu menu, UpdateMenuCmd cmd){
        BeanCopyUtils.copyNonNullProperties(cmd, menu);
    }
}
