package com.qy.insurance.cloud.client.security.demo.controller;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/24 18:15
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/ok")
    @PreAuthorize("hasAuthority('a')")
    public String ok(){
        return "OK!";
    }

    @GetMapping("/fail")
    @PreAuthorize("hasAuthority('c')")
    public String fail(){
        return "FAIL!";
    }

    @GetMapping("/e")
    @PreAuthorize("hasAuthority('e')")
    public String queryE(){
        return "拥有权限E可查询此接口数据!!!";
    }

    @GetMapping("/list")
    @PostFilter("hasAuthority(filterObject.charAt(0))")
    public List<String> list(){
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("alpha");
        list.add("beat");
        list.add("control");
        list.add("demo");
        list.add("element");
        return list;
    }
}
