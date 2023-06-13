package com.csbaic.edatope.app.mapper;

import com.csbaic.edatope.app.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2021-12-15
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesByUserId(@Param("userId") String userId);
}
