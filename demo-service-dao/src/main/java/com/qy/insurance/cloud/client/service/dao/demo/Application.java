package com.qy.insurance.cloud.client.service.dao.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/23 11:55
 * @version: 1.0.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
