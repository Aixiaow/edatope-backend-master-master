package com.csbaic.edatope.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = "com.csbaic.edatope")
@MapperScan(basePackages = "com.csbaic.edatope.**.mapper")
public class EdatopeApplication {

    public static void main(String[] args){
        SpringApplication.run(EdatopeApplication.class, args);
}
}
