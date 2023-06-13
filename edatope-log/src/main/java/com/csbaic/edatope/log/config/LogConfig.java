package com.csbaic.edatope.log.config;

import com.csbaic.edatope.log.context.LogContextFilter;
import com.csbaic.edatope.log.context.OperateLogAspect;
import com.csbaic.edatope.log.context.OperateLogHelper;
import com.csbaic.edatope.log.service.OperateLogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class LogConfig implements WebMvcConfigurer {


    /**
     * 操作日志切面
     *
     * @param handler
     * @return
     */
    @Bean
    public OperateLogAspect operateLogAspect(OperateLogHandler handler) {
        return new OperateLogAspect(handler);
    }

    @Bean
    public LogContextFilter logContextFilter() {
        return new LogContextFilter();
    }
}

