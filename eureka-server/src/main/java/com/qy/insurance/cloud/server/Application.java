package com.qy.insurance.cloud.server;

import com.qy.insurance.cloud.server.config.SSLConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Import;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/15 18:22
 * @version: 1.0.0
 */
@SpringBootApplication
@Import(SSLConfig.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
