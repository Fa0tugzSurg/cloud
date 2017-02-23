package com.qy.insurance.cloud.server.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.cluster.PeerEurekaNodes;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.resources.ServerCodecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/16 15:07
 * @version: 1.0.0
 */
@Configuration
@Import({com.qy.insurance.cloud.core.security.ssl.DefaultSslConfig.class
        , com.qy.insurance.cloud.core.security.ssl.EurekaSslConfig.class})
@Slf4j
@EnableEurekaServer
public class SSLConfig {

    @Bean
    @Primary
    @RefreshScope
    @Autowired
    public PeerEurekaNodes peerEurekaNodes(PeerAwareInstanceRegistry registry,
                                           EurekaServerConfig serverConfig,
                                           EurekaClientConfig clientConfig,
                                           ServerCodecs serverCodecs,
                                           ApplicationInfoManager applicationInfoManager) {
        return new CustomPeerEurekaNodes(registry, serverConfig,
                clientConfig, serverCodecs, applicationInfoManager);
    }

}
