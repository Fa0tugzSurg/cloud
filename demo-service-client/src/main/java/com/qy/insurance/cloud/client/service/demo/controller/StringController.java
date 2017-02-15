package com.qy.insurance.cloud.client.service.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/15 18:41
 * @version: 1.0.0
 */
@RestController
@RequestMapping("service/")
public class StringController {
    @RequestMapping(path = "concat", method = RequestMethod.GET)
    public String concat(@RequestParam String a, @RequestParam String b) {
        return a.concat(b);
    }
}
