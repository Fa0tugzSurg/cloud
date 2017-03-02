package com.qy.insurance.cloud.core.annotation;

import com.qy.insurance.cloud.core.eureka.feign.FeignConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/2 15:31
 * @version: 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignConfiguration.class)
public @interface EnableFeignCustom {
}
