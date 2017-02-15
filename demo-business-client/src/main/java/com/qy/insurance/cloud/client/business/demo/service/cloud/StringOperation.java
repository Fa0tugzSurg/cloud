package com.qy.insurance.cloud.client.business.demo.service.cloud;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/15 18:54
 * @version: 1.0.0
 */
@FeignClient(name = "${client.name.demo-service-client}")
@RequestMapping("service")
public interface StringOperation {
    @RequestMapping(path = "concat", method = RequestMethod.GET)
    String concat(@RequestParam("a") String a, @RequestParam("b") String b);
}
