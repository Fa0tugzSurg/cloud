package com.qy.insurance.cloud.core.zipkin;

import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import zipkin.reporter.Callback;
import zipkin.reporter.Encoding;
import zipkin.reporter.Sender;
import zipkin.reporter.urlconnection.URLConnectionSender;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @task:
 * @discrption:
 * @author: Aere
 * @date: 2017/2/28 10:44
 * @version: 1.0.0
 */
public class RibbonURLConnectionSender implements Sender {

    private final LoadBalancerClient client;
    private final String endpoint;
    private final boolean compressionEnabled;
    private final boolean ribbonEnable;
    private final String serviceId;

    private final Map<String, URLConnectionSender> senderMap = new ConcurrentHashMap<>();

    public RibbonURLConnectionSender(LoadBalancerClient client, String endpoint, boolean compressionEnabled) {
        this.client = client;
        this.endpoint = endpoint;
        this.compressionEnabled = compressionEnabled;
        String pattern = "://";
        int i = endpoint.indexOf(pattern);
        String url = endpoint.substring(i + pattern.length());
        String name = url.substring(0, url.indexOf('/'));
        if ("localhost".equals(name) || InetAddressUtils.isIPv4Address(name) || InetAddressUtils.isIPv6Address(name)) {
            ribbonEnable = false;
            this.serviceId = "SHOULD_NEVER_MATCH_STRING";
        } else {
            ribbonEnable = true;
            this.serviceId = name;
        }
    }

    @Override
    public Encoding encoding() {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        return urlConnectionSender.encoding();
    }

    @Override
    public int messageMaxBytes() {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        return urlConnectionSender.messageMaxBytes();
    }

    @Override
    public int messageSizeInBytes(List<byte[]> encodedSpans) {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        return urlConnectionSender.messageMaxBytes();
    }

    @Override
    public void sendSpans(List<byte[]> encodedSpans, Callback callback) {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        urlConnectionSender.sendSpans(encodedSpans, callback);
    }

    @Override
    public CheckResult check() {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        return urlConnectionSender.check();
    }

    @Override
    public void close() {
        URLConnectionSender urlConnectionSender = getUrlConnectionSender();
        urlConnectionSender.close();
    }

    private URLConnectionSender getUrlConnectionSender() {
        URLConnectionSender urlConnectionSender;
        if (ribbonEnable) {
            ServiceInstance instance = client.choose(serviceId);
            String endpoint = instance.getHost() + ":" + instance.getPort();
            urlConnectionSender = senderMap.computeIfAbsent(endpoint, this::buildSender);
        } else {
            urlConnectionSender = senderMap.computeIfAbsent(this.endpoint, this::buildSender);
        }
        return urlConnectionSender;
    }

    private URLConnectionSender buildSender(String endpoint) {
        return URLConnectionSender.builder()
                .endpoint(this.endpoint.replace(serviceId, endpoint))
                .compressionEnabled(compressionEnabled)
                .build();
    }
}
