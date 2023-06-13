package com.csbaic.edatope.app.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket createAdminRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(
                        Lists.newArrayList(
                                new ParameterBuilder().parameterType("header")
                                        .required(false)
                                        .description("认证token")
                                        .modelRef(new ModelRef("string"))
                                        .defaultValue("Bearer eyJhbGciOiJIUzI1NiJ9.eyJwcmluY2lwYWwiOiIxNDgwNTM4MDg1Nzc4NzYzNzc3IiwiaXNzIjoiaHR0cHM6XC9cL3d3dy5jc2JhaWMuY29tXC8iLCJzdWIiOiJjc2JhaWMiLCJwcmluY2lwYWxfdHlwZSI6IiJ9.7QWvFbWBvl2LG8JStLCsCuT8spP5oFr75YeCN0Igabk")
                                        .name("Authorization")
                                        .build()
                        )
                )
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()))
                .host("edatope-api.csbaic.com")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.csbaic.edatope"))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }




    @Bean
    SecurityScheme securityScheme() {
        return new ApiKey("bearer", "Authorization", "header");
    }

    @Bean
    SecurityContext securityContext() {
        SecurityReference securityReference = SecurityReference.builder()
                .reference("bearer")
                .scopes(new AuthorizationScope[]{})
                .build();

        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(securityReference))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("中科智汇土壤环境管理平台")
                .description("中科智汇土壤环境管理平台接口文档")
                .termsOfServiceUrl("")
                .version("2.0")
                .build();
    }
}
