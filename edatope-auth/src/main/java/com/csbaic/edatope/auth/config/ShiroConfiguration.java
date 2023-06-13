package com.csbaic.edatope.auth.config;

import com.csbaic.edatope.auth.accesstoken.AccessTokenReposition;
import com.csbaic.edatope.auth.accesstoken.AccessTokenService;
import com.csbaic.edatope.auth.accesstoken.impl.JWTAccessTokenServiceImpl;
import com.csbaic.edatope.auth.accesstoken.impl.MemAccessTokenReposition;
import com.csbaic.edatope.auth.api.ApiPrincipalDetailsService;
import com.csbaic.edatope.auth.filter.BearerTokenAuthenticationFilter;
import com.csbaic.edatope.auth.filter.MatchApiAuthorizationFilter;
import com.csbaic.edatope.auth.principal.PrincipalDetailsService;
import com.csbaic.edatope.auth.principal.PrincipalStatusChecker;
import com.csbaic.edatope.auth.realm.PrincipalDetailsRealm;
import com.csbaic.edatope.auth.subject.StatelessDefaultSubjectFactory;
import com.nimbusds.jose.JWSAlgorithm;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.KeyGenerator;
import javax.servlet.Filter;
import java.security.NoSuchAlgorithmException;

@Configuration
public class ShiroConfiguration {

    /**
     * 解决跨域问题
     *
     * @return
     */
    @Bean(name = "corsFilter")
    public Filter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 4
        return new CorsFilter(source);
    }

    /***
     * token认证
     * @return
     */
    @Bean(name = "bearerTokenAuthc")
    public Filter bearerTokenAuthenticationFilter() {
        return new BearerTokenAuthenticationFilter();
    }


    /**
     * API鉴权
     *
     * @param apiPrincipalDetailsService
     * @return
     */
    @Bean(name = "apiAuthz")
    public Filter matchApiAuthorizationFilter(ApiPrincipalDetailsService apiPrincipalDetailsService) {
        return new MatchApiAuthorizationFilter(apiPrincipalDetailsService);
    }


    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        chainDefinition.addPathDefinition("/api/**", "corsFilter,bearerTokenAuthc,apiAuthz");
        chainDefinition.addPathDefinition("/**", "corsFilter");
        return chainDefinition;
    }


    /**
     * 无状态subject工厂
     *
     * @return
     */
    @Bean
    public SubjectFactory subjectFactory() {
        return new StatelessDefaultSubjectFactory();
    }

    /**
     * subjectDao no session
     *
     * @return
     */
    @Bean
    public SubjectDAO subjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator((s) -> false);
        return subjectDAO;
    }

    @Bean
    public AccessTokenService accessTokenService() {
        try {
            return new JWTAccessTokenServiceImpl(new String(KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded()), JWSAlgorithm.HS256.getName(), null);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @ConditionalOnMissingBean(AccessTokenReposition.class)
    @Bean
    public AccessTokenReposition accessTokenReposition(){
        return new MemAccessTokenReposition();
    }

    @Bean
    @ConditionalOnMissingBean
    public Realm principalDetailsRealm(PrincipalDetailsService principalDetailsService, PrincipalStatusChecker checker ){
        PrincipalDetailsRealm principalDetailsRealm =  new PrincipalDetailsRealm(principalDetailsService,checker);
        principalDetailsRealm.setAuthenticationTokenClass(BearerToken.class);
        return principalDetailsRealm;
    }

}
