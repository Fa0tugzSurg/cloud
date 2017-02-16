package com.qy.insurance.cloud.client.service.demo.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/16 15:07
 * @version: 1.0.0
 */
@Configuration
public class SSLConfig {

    @PostConstruct
    public void Initial(){
        HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, sslSession) -> hostname.equals("localhost"));
    }

}
