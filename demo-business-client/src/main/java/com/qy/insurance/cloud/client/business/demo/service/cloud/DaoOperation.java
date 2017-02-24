package com.qy.insurance.cloud.client.business.demo.service.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/24 14:51
 * @version: 1.0.0
 */
@FeignClient(name = "demo-service-dao")
public interface DaoOperation {
    @RequestMapping("dao/insert/success")
    boolean insertSuccess();

    @RequestMapping("dao/insert/fail")
    boolean insertFail();

}
