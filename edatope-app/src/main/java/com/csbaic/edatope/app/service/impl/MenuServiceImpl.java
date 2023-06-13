package com.csbaic.edatope.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csbaic.edatope.auth.principal.PrincipalDetails;
import com.csbaic.edatope.auth.utils.PrincipalUtils;
import com.csbaic.edatope.common.enums.EnableStatus;
import com.csbaic.edatope.common.exception.BizRuntimeException;
import com.csbaic.edatope.common.result.ResultCode;
import com.csbaic.edatope.common.utils.CollectionStreamUtils;
import com.csbaic.edatope.common.utils.TreeListUtils;
import com.csbaic.edatope.log.annotation.OperateLog;
import com.csbaic.edatope.log.enums.OperateType;
import com.csbaic.edatope.app.convert.MenuConvert;
import com.csbaic.edatope.app.entity.Menu;
import com.csbaic.edatope.app.entity.Permission;
import com.csbaic.edatope.app.enums.MenuStatus;
import com.csbaic.edatope.app.enums.MenuType;
import com.csbaic.edatope.app.mapper.MenuMapper;
import com.csbaic.edatope.app.model.command.CreateMenuCmd;
import com.csbaic.edatope.app.model.command.DeleteMenuCmd;
import com.csbaic.edatope.app.model.command.UpdateMenuCmd;
import com.csbaic.edatope.app.model.dto.MenuDTO;
import com.csbaic.edatope.app.service.IMenuService;
import com.csbaic.edatope.app.service.IPermissionService;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuConvert convert;

    @Autowired
    private IPermissionService permissionService;

    private PermissionResolver permissionResolver = new WildcardPermissionResolver();

    @OperateLog(operateType = OperateType.CREATE)
    @Override
    public void create(CreateMenuCmd cmd) {
        if (!Strings.isNullOrEmpty(cmd.getPermissionId())) {
            Permission permission = permissionService.getById(cmd.getPermissionId());
            if (permission == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到权限：" + cmd.getPermissionId());
            }
        }

        if (cmd.getPid() != null) {
            Menu parent = getById(cmd.getPid());
            if (parent == null) {
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级菜单：" + cmd.getPermissionId());
            }

            if (Objects.equals(parent.getType(), MenuType.BUTTON.name())) {
                throw BizRuntimeException.from(ResultCode.ERROR, "按钮类型的菜单，不能添加子菜单");
            }
        }

        Menu menu = convert.convertToEntity(cmd);
        save(menu);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<MenuDTO> listMenu() {
        List<Menu> menuList = list(
                Wrappers.<Menu>query().orderByAsc(Menu.SORT)
        );

        List<Menu> returnList = new ArrayList<>();
        //转换成树形
        TreeListUtils.tree(menuList, Menu::getId, Menu::getPid, (parent, child) -> {
            if (parent == null) {
                returnList.add(child);
                return;
            }

            if (parent.getChildren() == null) {
                parent.setChildren(new ArrayList<>());
            }
            parent.getChildren().add(child);
        });
        return returnList.stream()
                .map(convert::convertToDTO)
                .collect(Collectors.toList());
    }

    @OperateLog(operateType = OperateType.UPDATE)
    @Override
    public void updateMenu(UpdateMenuCmd cmd) {
        Menu menu = getById(cmd.getId());
        if(menu == null){
            throw BizRuntimeException.from(ResultCode.ERROR, "菜单不存在：" + cmd.getId());
        }

        if(cmd.getPid() != null){
            Menu parent = getById(cmd.getPid());
            if(parent == null){
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到上级菜单：" + cmd.getPermissionId());
            }
        }

        if (Objects.equals(cmd.getType(), MenuType.BUTTON.name())) {
            long childCount = count(
                    Wrappers.<Menu>query().eq(Menu.PID, cmd.getId())
            );
            if(childCount > 0){
                throw BizRuntimeException.from(ResultCode.ERROR, MessageFormat.format("{0} 菜单有子菜单不能更新为按钮类型", menu.getName()));
            }
        }

        if (!Strings.isNullOrEmpty(cmd.getPermissionId())) {
            Permission permission = permissionService.getById(cmd.getPermissionId());
            if(permission == null){
                throw BizRuntimeException.from(ResultCode.ERROR, "没有找到权限：" + cmd.getPermissionId());
            }
        }


        convert.convertUpdateMenuCommandToEntity(menu, cmd);
        updateById(menu);
    }


    @OperateLog(operateType = OperateType.DELETE)
    @Transactional
    @Override
    public void deleteMenu(DeleteMenuCmd cmd) {
        Menu menu = getById(cmd.getId());
        if (menu == null) {
            return;
        }

        List<Menu> menuList = list(Wrappers.<Menu>query().eq(Menu.PID, cmd.getId()));
        if (CollectionUtils.isNotEmpty(menuList)) {
            throw BizRuntimeException.from(ResultCode.ERROR, "请先删除子菜单");
        }


        removeById(menu.getId());
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public MenuDTO getMenuById(String id) {
        Menu menu = getById(id);
        if (menu == null) {
            throw BizRuntimeException.from(ResultCode.ERROR, "没有找到菜单 ：" + id);
        }

        if (!Strings.isNullOrEmpty(menu.getPermissionId())) {
            menu.setPermission(
                    permissionService.getById(menu.getPermissionId())
            );
        }

        if (!Strings.isNullOrEmpty(menu.getPid())) {
            Menu parent = getById(menu.getPid());
            menu.setParent(parent);
        }

        List<Menu> children = list(
                Wrappers.<Menu>query().in(Menu.PID, id)
        );
        menu.setChildren(children);
        return convert.convertToDTO(menu);
    }

    @OperateLog(operateType = OperateType.QUERY)
    @Override
    public List<MenuDTO> listUserMenu() {
        PrincipalDetails details = PrincipalUtils.getOrThrow();

        List<MenuDTO> menuList = list(
                Wrappers.<Menu>query().eq(Menu.TYPE, MenuType.ROUTE.name())
                        .in(Menu.STATUS, MenuStatus.ENABLED.name(), MenuStatus.DISABLED.name())
                        .orderByAsc(Menu.SORT)
        ).stream().map(convert::convertToDTO).collect(Collectors.toList());


        List<String> permissionIds = new ArrayList<>();
        menuList.forEach(menu -> {
            if (!Strings.isNullOrEmpty(menu.getPermissionId())) {
                permissionIds.add(menu.getPermissionId());
            }
        });
        Map<String, Permission> permissionMap = CollectionStreamUtils.toMap(
                CollectionUtils.isNotEmpty(permissionIds) ? permissionService.listByIds(permissionIds) : new ArrayList<>(),
                Permission::getId
        );

        Set<String> showMenuIds = menuList.stream().map(MenuDTO::getId).collect(Collectors.toSet());
        //保存不需要权限和已有权限的菜单
        menuList = menuList.stream()
                .filter(menu -> {
                    //保留没有设置权限的菜单
                    if (Strings.isNullOrEmpty(menu.getPermissionId())) {
                        return true;
                    }

                    //判断上级菜单是否存在
                    if (StringUtils.isNotBlank(menu.getPid()) && !showMenuIds.contains(menu.getId())) {
                        //上级菜单不存在，移除，使下级菜单也不显示
                        showMenuIds.remove(menu.getId());
                        return false;
                    }

                    Permission permission = permissionMap.get(menu.getPermissionId());
                    boolean available = Objects.equals(permission.getStatus(), EnableStatus.ENABLED.name());
                    if (!available) {
                        showMenuIds.remove(menu.getId());
                        return false;
                    }

                    //权限可用判断是否有权限
                    org.apache.shiro.authz.Permission menuPermission = permissionResolver.resolvePermission(permission.getCode());
                    if (CollectionUtils.isNotEmpty(details.getStringPermissions())) {
                        for (String p : details.getStringPermissions()) {
                            org.apache.shiro.authz.Permission userPerm = permissionResolver.resolvePermission(p);
                            if (menuPermission.implies(userPerm)) {
                                return true;
                            }
                        }
                    }

                    return false;
                }).collect(Collectors.toList());


        TreeListUtils.tree(menuList, MenuDTO::getId, MenuDTO::getPid, (parent, menu) -> {
            if (parent == null) {
                return;
            }

            parent.getChildren().add(menu);
        });

        //只保留有关联权限或者有子菜单的菜单
        List<MenuDTO> filterMenuList = menuList.stream()
                .filter((Predicate<MenuDTO>) input -> StringUtils.isNotBlank(input.getPermissionId()) || CollectionUtils.isNotEmpty(input.getChildren()))
                .collect(Collectors.toList());

        //清空子菜单，重新计算层级
        filterMenuList.forEach(menuDTO -> menuDTO.setChildren(new ArrayList<>()));
        List<MenuDTO> root = new ArrayList<>();
        TreeListUtils.tree(filterMenuList, MenuDTO::getId, MenuDTO::getPid, (parent, menu) -> {
            if (parent == null) {
                root.add(menu);
                return;
            }
            parent.getChildren().add(menu);
        });

        return root;
    }

}
