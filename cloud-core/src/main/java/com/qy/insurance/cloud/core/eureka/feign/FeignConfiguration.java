package com.qy.insurance.cloud.core.eureka.feign;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/27 13:34
 * @version: 1.0.0
 */
@Configuration
public class FeignConfiguration {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.HEADERS;
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default();
    }

    @Bean
    public Request.Options requestOptions(ConfigurableEnvironment env){
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 6000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 3000);
        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "feign.hystrix.enabled", matchIfMissing = true)
    public Feign.Builder feignHystrixBuilder(HystrixCommandProperties.Setter setter) {
        HystrixFeign.Builder builder = HystrixFeign.builder();
        CustomSetterFactory customSetterFactory = new CustomSetterFactory(setter);
        builder.setterFactory(customSetterFactory);
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public HystrixCommandProperties.Setter customSetter(){
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withCircuitBreakerEnabled(true)
                .withExecutionTimeoutEnabled(false);
        return setter;
    }

    static class CustomSetterFactory implements SetterFactory{

        private final HystrixCommandProperties.Setter setter;

        public CustomSetterFactory(HystrixCommandProperties.Setter setter) {
            this.setter = setter;
        }

        /**
         * Fork from SetterFactory.Default class
         * @param target
         * @param method
         * @return
         */
        @Override
        public HystrixCommand.Setter create(Target<?> target, Method method) {
            String groupKey = target.name();
            String commandKey = Feign.configKey(target.type(), method);
            return HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                    .andCommandPropertiesDefaults(setter);
        }
    }
}
