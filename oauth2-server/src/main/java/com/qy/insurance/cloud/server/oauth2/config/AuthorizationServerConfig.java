package com.qy.insurance.cloud.server.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2016/11/2 18:25
 * @version: 1.0.0
 */
@Configuration
//@DependsOn("keyManager")
@EnableAuthorizationServer
@ConditionalOnMissingBean(DataSource.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String AUTH_SIGN_KEY_NAME = "insurance.cloud.auth.key";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    @Qualifier("keyManager")
//    private KeyManagementService keyManagementService;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource).passwordEncoder(new BCryptPasswordEncoder());
//                .withClient("test_client").secret("abcd@1234").scopes("read").resourceIds("test_resouce")
//                .authorizedGrantTypes("client_credentials").authorities("ROLE_TRUSTED_CLIENT")
//                .accessTokenValiditySeconds(300);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
//        keyInitialCheck();
//        String s = "SELECT property_value FROM t_sys_property_resource WHERE property_name=?";
//        String key = jdbcTemplate.queryForObject(s, String.class, AUTH_SIGN_KEY_NAME);
        String key = "O_vXUOds4wkkoFevWD3jwj0PamwhawbEjVXups37XMwnEwdg6X5s1HxTpLxr2esku-n94KVbk8BAI4Tr-BiumA";
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(key);
        return converter;
    }

//    private void keyInitialCheck() {
//        String sqlCount = "SELECT count(0) FROM t_sys_property_resource WHERE property_name=?";
//        Integer cnt = jdbcTemplate.queryForObject(sqlCount, Integer.class, AUTH_SIGN_KEY_NAME);
//        if (cnt == 0) {
//            String key = keyManagementService.getTokenSignKey();
//            String sqlInsert = "INSERT INTO t_sys_property_resource (property_name,property_value) VALUES (?,?)";
//            jdbcTemplate.update(sqlInsert, AUTH_SIGN_KEY_NAME, key);
//        }
//    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setAccessTokenValiditySeconds(120);
        return defaultTokenServices;
    }

}
