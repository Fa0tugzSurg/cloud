package com.qy.insurance.cloud.client.business.demo.controller;

import com.qy.insurance.cloud.client.business.demo.service.cloud.MathCompute;
import com.qy.insurance.cloud.client.business.demo.service.cloud.StringOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/15 18:52
 * @version: 1.0.0
 */
@RestController
public class TestController {

    @Autowired
    private MathCompute mathService;

    @Autowired
    private StringOperation stringOperation;

    @RequestMapping(path = "test/add", method = RequestMethod.GET)
    public Integer add() {
        return mathService.add(1, 2);
    }

    @RequestMapping(path = "test/concat", method = RequestMethod.GET)
    public String concat() {
        return stringOperation.concat("ab", "dc");
    }
}
