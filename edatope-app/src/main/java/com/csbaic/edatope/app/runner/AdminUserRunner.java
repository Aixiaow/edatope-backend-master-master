package com.csbaic.edatope.app.runner;

import com.csbaic.edatope.app.entity.User;
import com.csbaic.edatope.app.enums.UserStatus;
import com.csbaic.edatope.app.service.IUserService;
import com.csbaic.edatope.app.service.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AdminUserRunner implements ApplicationRunner {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count =  userService.count();
        if (count > 0) {
            return;
        }

        String password = "123456";
        String salt = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode(password + salt));
        user.setPasswordSalt(salt);
        user.setAdmin(true);
        user.setStatus(UserStatus.NORMAL.name());
        user.setNeedSetPassword(true);
        userService.save(user);
        log.info("========================================");
        log.info("管理员：{}, 管理员密码：{}", user.getUsername(), password);
        log.info("========================================");
    }
}
