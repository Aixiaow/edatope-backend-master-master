package com.csbaic.edatope.app.service;

public interface PasswordEncoder {

    /**
     * 密码编码
     * @return
     */
    String encode(String password);

    /**
     * 判断密码是否匹配
     * @param originPassword
     * @param encodedPassword
     * @return
     */
    Boolean match(String originPassword, String encodedPassword);
}
