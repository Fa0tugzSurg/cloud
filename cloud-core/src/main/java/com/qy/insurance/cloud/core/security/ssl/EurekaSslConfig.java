package com.qy.insurance.cloud.core.security.ssl;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.converters.wrappers.CodecWrappers;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.qy.insurance.cloud.core.eureka.CustomEurekaJerseyClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @task:
 * @discrption: Use a customer DiscoveryClientOptionalArgs to override spring cloud default behavior
 * @author: Aere
 * @date: 2017/2/17 16:48
 * @version: 1.0.0
 */
@Configuration
@Slf4j
public class EurekaSslConfig {

    @Value("${eureka.client.service-url.defaultZone}")
    private String defaultZone;

    @Autowired
    private EurekaClientConfig config;

    @Autowired
    private DefaultSslConfig defaultSslConfig;

    @Bean
    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs(){
        if(!defaultSslConfig.isFinish()){
            log.warn("Default SSLContext might not have been updated! Please check!");
        }

        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();

        CustomEurekaJerseyClientBuilder clientBuilder = new CustomEurekaJerseyClientBuilder()
                .withClientName("DiscoveryClient-HTTPClient-Custom")
                .withUserAgent("Java-EurekaClient")
                .withConnectionTimeout(config.getEurekaServerConnectTimeoutSeconds() * 1000)
                .withReadTimeout(config.getEurekaServerReadTimeoutSeconds() * 1000)
                .withMaxConnectionsPerHost(config.getEurekaServerTotalConnectionsPerHost())
                .withMaxTotalConnections(config.getEurekaServerTotalConnections())
                .withConnectionIdleTimeout(config.getEurekaConnectionIdleTimeoutSeconds() * 1000)
                .withEncoderWrapper(CodecWrappers.getEncoder(config.getEncoderName()))
                .withDecoderWrapper(CodecWrappers.resolveDecoder(config.getDecoderName(), config.getClientDataAccept()));
        if (defaultZone.startsWith("https://")) {
            clientBuilder.withSystemSSLConfiguration();
        }

        EurekaJerseyClient jerseyClient = clientBuilder.build();
        args.setEurekaJerseyClient(jerseyClient);//Provide custom EurekaJerseyClient to override default one
        return args;
    }
}
