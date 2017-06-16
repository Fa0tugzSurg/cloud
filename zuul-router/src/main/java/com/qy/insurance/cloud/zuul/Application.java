package com.qy.insurance.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/20 11:16
 * @version: 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@RestController
public class Application {


    @RequestMapping("/home")
    public String home(){
        return "success";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
