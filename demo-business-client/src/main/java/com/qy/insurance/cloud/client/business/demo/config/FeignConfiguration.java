package com.qy.insurance.cloud.client.business.demo.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/21 10:58
 * @version: 1.0.0
 */
@Configuration
@Import(com.qy.insurance.cloud.core.eureka.feign.FeignConfiguration.class)
public class FeignConfiguration {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }

}
