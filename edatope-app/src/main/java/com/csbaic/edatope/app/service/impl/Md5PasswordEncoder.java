package com.csbaic.edatope.app.service.impl;

import com.csbaic.edatope.app.service.PasswordEncoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String password) {
        return DigestUtils.md5Hex(password);
    }

    @Override
    public Boolean match(String originPassword, String encodedPassword) {
        return Objects.equals(encode(originPassword), encodedPassword);
    }
}
