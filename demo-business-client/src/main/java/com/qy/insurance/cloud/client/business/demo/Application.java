package com.qy.insurance.cloud.client.business.demo;

import com.qy.insurance.cloud.client.business.demo.config.FeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/15 18:43
 * @version: 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.qy.insurance.cloud.client.business.demo.service.cloud",
        defaultConfiguration = FeignConfiguration.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
