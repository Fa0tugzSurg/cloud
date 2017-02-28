package com.qy.insurance.cloud.client.business.demo.config;

import com.qy.insurance.cloud.client.business.demo.zipkin.RibbonZipkinSpanReporter;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/28 16:15
 * @version: 1.0.0
 */
@Configuration
public class ZipkinConfig {

    @Bean
    @Primary
    public ZipkinSpanReporter zipkinSpanReporter(SpanMetricReporter spanMetricReporter,
                                                 ZipkinProperties zipkin,
                                                 LoadBalancerClient client){
        return new RibbonZipkinSpanReporter(zipkin.getBaseUrl(), zipkin.getFlushInterval(),
                zipkin.getCompression().isEnabled(), spanMetricReporter,client);
    }

}
