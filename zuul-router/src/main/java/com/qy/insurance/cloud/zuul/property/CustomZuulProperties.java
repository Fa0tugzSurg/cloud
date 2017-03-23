package com.qy.insurance.cloud.zuul.property;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/3/21 15:00
 * @version: 1.0.0
 */
@Data
@Component
@ConfigurationProperties("zuul.filter")
public class CustomZuulProperties {
    /**
     * Map of route names to properties.
     */
    private Map<String, ZuulRoute> routes = new LinkedHashMap<>();

    @Data
    @NoArgsConstructor
    public static class ZuulRoute {
        /**
         * Whether or not should this filter enabled
         */
        private boolean enable = true;
        /**
         * The order of this filter should be used
         */
        private int order = 0;
        /**
         * To classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
         * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
         * We also support a "static" type for static responses see  StaticResponseFilter.
         * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
         */
        private String type = "pre";

        /**
         * Determine that path patterns this zuul-filter should match and filter
         */
        private List<String> paths;

        /**
         * Determine what roles must contains in token, or request will be declined
         */
        private List<String> roles;
    }
}
