package com.qy.insurance.cloud.server.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;
import zipkin.server.brave.BraveConfiguration;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/27 15:30
 * @version: 1.0.0
 */
@SpringBootApplication(exclude = {BraveConfiguration.class})
@EnableDiscoveryClient
@EnableZipkinServer
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
