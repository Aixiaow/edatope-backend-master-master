package com.csbaic.edatope.app.service.impl;

import com.csbaic.edatope.app.entity.UserRole;
import com.csbaic.edatope.app.mapper.UserRoleMapper;
import com.csbaic.edatope.app.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色与用户关联表 服务实现类
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
