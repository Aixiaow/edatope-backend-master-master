package com.csbaic.edatope.app.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.csbaic.edatope.app.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csbaic.edatope.app.model.query.UserQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author bage
 * @since 2022-01-03
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据登陆用户名或者手机号获取用户
     *
     * @param text
     * @return
     */
    User getByUsernameOrMobile(String text);


    /**
     * 用户详情
     *
     * @param userId
     * @return
     */
    User getUserDetail(String userId);

    /**
     * 用户查询
     *
     * @param query
     * @return
     */
    IPage<User> listUser(IPage<User> page, @Param("query") UserQuery query);
}
