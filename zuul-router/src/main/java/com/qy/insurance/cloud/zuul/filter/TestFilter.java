package com.qy.insurance.cloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/20 19:16
 * @version: 1.0.0
 */
@Slf4j
public class TestFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        log.info("Receive {} request {} from {} ", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        return null;
    }
}
