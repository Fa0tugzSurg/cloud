package com.qy.insurance.cloud.zuul.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/16 15:07
 * @version: 1.0.0
 */
@Configuration
@Import({com.qy.insurance.cloud.core.security.ssl.DefaultSslConfig.class
        , com.qy.insurance.cloud.core.security.ssl.EurekaSslConfig.class})

public class SSLConfig {

}
