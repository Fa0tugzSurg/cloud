package com.qy.insurance.cloud.server.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.eureka.EurekaServerConfig;
import com.netflix.eureka.cluster.PeerEurekaNode;
import com.netflix.eureka.cluster.PeerEurekaNodes;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.netflix.eureka.resources.ServerCodecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
public class SSLConfig {

    @Autowired
    private PeerEurekaNodes peerEurekaNodes;

    @Autowired
    private PeerAwareInstanceRegistry registry;
    @Autowired
    private EurekaServerConfig serverConfig;
    @Autowired
    private EurekaClientConfig clientConfig;
    @Autowired
    private ServerCodecs serverCodecs;
    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    private Set<String> peerEurekaNodeUrls;


    @PostConstruct
    @RefreshScope
    public void initialAction() throws NoSuchFieldException, IllegalAccessException {
        peerEurekaNodeUrls = new HashSet<>(resolvePeerUrls());
        start();
    }

    private void switchClientInPeerEurekaNodes() throws NoSuchFieldException, IllegalAccessException {
        List<PeerEurekaNode> newPeerEurekaNodes = new ArrayList<>();
        for (PeerEurekaNode node : this.peerEurekaNodes.getPeerEurekaNodes()) {
            Field registry = node.getClass().getDeclaredField("registry");
            registry.setAccessible(true);
            Field targetHost = node.getClass().getDeclaredField("targetHost");
            targetHost.setAccessible(true);
            Field serviceUrl = node.getClass().getDeclaredField("serviceUrl");
            serviceUrl.setAccessible(true);
            Field config = node.getClass().getDeclaredField("config");
            config.setAccessible(true);
            PeerEurekaNode peerEurekaNode = new PeerEurekaNode((PeerAwareInstanceRegistry) registry.get(node),
                    (String) targetHost.get(node),
                    (String) serviceUrl.get(node),
                    CustomPeerEurekaNodes.createReplicationClient(serverConfig, serverCodecs, (String) serviceUrl.get(node)),
                    (EurekaServerConfig) config.get(node));
            newPeerEurekaNodes.add(peerEurekaNode);
        }
        Field peerEurekaNodes = this.peerEurekaNodes.getClass().getDeclaredField("peerEurekaNodes");
        peerEurekaNodes.setAccessible(true);
        peerEurekaNodes.set(this.peerEurekaNodes, newPeerEurekaNodes);

        this.peerEurekaNodeUrls = new HashSet<>(resolvePeerUrls());
        Field peerEurekaNodeUrls = this.peerEurekaNodes.getClass().getDeclaredField("peerEurekaNodeUrls");
        peerEurekaNodeUrls.setAccessible(true);
        peerEurekaNodeUrls.set(this.peerEurekaNodes, new HashSet<>(resolvePeerUrls()));
    }

    @Bean
//    @Primary
    @Autowired
    public PeerEurekaNodes peerEurekaNodes(PeerAwareInstanceRegistry registry,
                                           EurekaServerConfig serverConfig,
                                           EurekaClientConfig clientConfig,
                                           ServerCodecs serverCodecs,
                                           ApplicationInfoManager applicationInfoManager) {
        return new CustomPeerEurekaNodes(registry, serverConfig,
                clientConfig, serverCodecs, applicationInfoManager);
    }

    public void start() {
        ScheduledExecutorService taskExecutor = Executors.newSingleThreadScheduledExecutor(
                r -> {
                    Thread thread = new Thread(r, "Eureka-PeerNode Custom Client Updater");
                    thread.setDaemon(true);
                    return thread;
                }
        );
        try {
            if (!updatePeerEurekaNodes(resolvePeerUrls()).isEmpty()) {
                switchClientInPeerEurekaNodes();
            }
            Runnable peersUpdateTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!updatePeerEurekaNodes(resolvePeerUrls()).isEmpty()) {
                            switchClientInPeerEurekaNodes();
                        }
                    } catch (Throwable e) {
                        log.error("Cannot update the replica Nodes", e);
                    }

                }
            };
            taskExecutor.scheduleWithFixedDelay(
                    peersUpdateTask,
                    serverConfig.getPeerEurekaNodesUpdateIntervalMs() > 1000 ? 1000 : serverConfig.getPeerEurekaNodesUpdateIntervalMs(),
                    serverConfig.getPeerEurekaNodesUpdateIntervalMs() > 1000 ? 1000 : serverConfig.getPeerEurekaNodesUpdateIntervalMs(),
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        for (PeerEurekaNode node : peerEurekaNodes.getPeerEurekaNodes()) {
            log.info("Replica node URL:  " + node.getServiceUrl());
        }
    }

    private Set<String> updatePeerEurekaNodes(List<String> newPeerUrls) {
        if (newPeerUrls.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> toAdd = new HashSet<>(newPeerUrls);
        toAdd.removeAll(peerEurekaNodeUrls);
        return toAdd;
    }

    private List<String> resolvePeerUrls() {
        return peerEurekaNodes.getPeerNodesView().stream()
                .map(PeerEurekaNode::getServiceUrl)
                .collect(Collectors.toList());
    }

}
