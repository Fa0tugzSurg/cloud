package com.qy.insurance.cloud.core.eureka;

import com.netflix.discovery.converters.wrappers.CodecWrappers;
import com.netflix.discovery.converters.wrappers.DecoderWrapper;
import com.netflix.discovery.converters.wrappers.EncoderWrapper;
import com.netflix.discovery.provider.DiscoveryJerseyProvider;
import com.netflix.discovery.shared.MonitoredConnectionManager;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import com.netflix.discovery.shared.transport.jersey.SSLSocketFactoryAdapter;
import com.netflix.discovery.util.DiscoveryBuildInfo;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache4.config.ApacheHttpClient4Config;
import com.sun.jersey.client.apache4.config.DefaultApacheHttpClient4Config;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.TextUtils;


/**
 * @task:
 * @discrption: Fork from class EurekaJerseyClientImpl. If anything not works well, check the latest source!
 * @author: Aere
 * @date: 2017/2/17 15:27
 * @version: 1.0.0
 */
public class CustomEurekaJerseyClientBuilder {
    private boolean systemSSL;
    private String clientName;
    private int maxConnectionsPerHost;
    private int maxTotalConnections;
    private String trustStoreFileName;
    private String trustStorePassword;
    private String userAgent;
    private String proxyUserName;
    private String proxyPassword;
    private String proxyHost;
    private String proxyPort;
    private int connectionTimeout;
    private int readTimeout;
    private int connectionIdleTimeout;
    private EncoderWrapper encoderWrapper;
    private DecoderWrapper decoderWrapper;

    public CustomEurekaJerseyClientBuilder withClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withConnectionIdleTimeout(int connectionIdleTimeout) {
        this.connectionIdleTimeout = connectionIdleTimeout;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withMaxConnectionsPerHost(int maxConnectionsPerHost) {
        this.maxConnectionsPerHost = maxConnectionsPerHost;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withProxy(String proxyHost, String proxyPort, String user, String password) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUserName = user;
        this.proxyPassword = password;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withSystemSSLConfiguration() {
        this.systemSSL = true;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withTrustStoreFile(String trustStoreFileName, String trustStorePassword) {
        this.trustStoreFileName = trustStoreFileName;
        this.trustStorePassword = trustStorePassword;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withEncoder(String encoderName) {
        return this.withEncoderWrapper(CodecWrappers.getEncoder(encoderName));
    }

    public CustomEurekaJerseyClientBuilder withEncoderWrapper(EncoderWrapper encoderWrapper) {
        this.encoderWrapper = encoderWrapper;
        return this;
    }

    public CustomEurekaJerseyClientBuilder withDecoder(String decoderName, String clientDataAccept) {
        return this.withDecoderWrapper(CodecWrappers.resolveDecoder(decoderName, clientDataAccept));
    }

    public CustomEurekaJerseyClientBuilder withDecoderWrapper(DecoderWrapper decoderWrapper) {
        this.decoderWrapper = decoderWrapper;
        return this;
    }

    public EurekaJerseyClient build() {
        MyDefaultApacheHttpClient4Config config = new MyDefaultApacheHttpClient4Config();
        try {
            return new EurekaJerseyClientImpl(connectionTimeout, readTimeout, connectionIdleTimeout, config);
        } catch (Throwable e) {
            throw new RuntimeException("Cannot create Jersey client ", e);
        }
    }

    class MyDefaultApacheHttpClient4Config extends DefaultApacheHttpClient4Config {

        private static final String PROTOCOL = "https";
        private static final String PROTOCOL_SCHEME = "SSL";
        private static final int HTTPS_PORT = 443;
        private static final String KEYSTORE_TYPE = "JKS";

        MyDefaultApacheHttpClient4Config() {
            MonitoredConnectionManager cm;

            if (systemSSL) {
                cm = createSystemSslCM();
            } else {
                cm = createDefaultSslCM();
            }

            if (proxyHost != null) {
                addProxyConfiguration(cm);
            }

            DiscoveryJerseyProvider discoveryJerseyProvider = new DiscoveryJerseyProvider(encoderWrapper, decoderWrapper);
            getSingletons().add(discoveryJerseyProvider);

            // Common properties to all clients
            cm.setDefaultMaxPerRoute(maxConnectionsPerHost);
            cm.setMaxTotal(maxTotalConnections);
            getProperties().put(ApacheHttpClient4Config.PROPERTY_CONNECTION_MANAGER, cm);

            String fullUserAgentName = (userAgent == null ? clientName : userAgent) + "/v" + DiscoveryBuildInfo.buildVersion();
            getProperties().put(CoreProtocolPNames.USER_AGENT, fullUserAgentName);

            // To pin a client to specific server in case redirect happens, we handle redirects directly
            // (see DiscoveryClient.makeRemoteCall methods).
            getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, Boolean.FALSE);
            getProperties().put(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
        }

        private void addProxyConfiguration(MonitoredConnectionManager cm) {
            if (proxyUserName != null && proxyPassword != null) {
                getProperties().put(ApacheHttpClient4Config.PROPERTY_PROXY_USERNAME, proxyUserName);
                getProperties().put(ApacheHttpClient4Config.PROPERTY_PROXY_PASSWORD, proxyPassword);
            } else {
                // Due to bug in apache client, user name/password must always be set.
                // Otherwise proxy configuration is ignored.
                getProperties().put(ApacheHttpClient4Config.PROPERTY_PROXY_USERNAME, "guest");
                getProperties().put(ApacheHttpClient4Config.PROPERTY_PROXY_PASSWORD, "guest");
            }
            getProperties().put(DefaultApacheHttpClient4Config.PROPERTY_PROXY_URI, "http://" + proxyHost + ":" + proxyPort);
        }

        private MonitoredConnectionManager createSystemSslCM() {
            MonitoredConnectionManager cm;
            X509HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory systemSocketFactory = new SSLConnectionSocketFactory(
                    (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault(),
                    split(System.getProperty("https.protocols")),
                    split(System.getProperty("https.cipherSuites")),
                    hostnameVerifier);
            SSLSocketFactory sslSocketFactory = new SSLSocketFactoryAdapter(systemSocketFactory);
            SchemeRegistry sslSchemeRegistry = new SchemeRegistry();
            sslSchemeRegistry.register(new Scheme(PROTOCOL, HTTPS_PORT, sslSocketFactory));
            cm = new MonitoredConnectionManager(clientName, sslSchemeRegistry);
            return cm;
        }

        /**
         * @see SchemeRegistryFactory#createDefault()
         */
        private MonitoredConnectionManager createDefaultSslCM() {
            final SchemeRegistry registry = new SchemeRegistry();
            registry.register(
                    new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            registry.register(
                    new Scheme("https", 443, new SSLSocketFactoryAdapter(SSLConnectionSocketFactory.getSocketFactory())));
            return new MonitoredConnectionManager(clientName, registry);
        }

        private String[] split(final String s) {
            if (TextUtils.isBlank(s)) {
                return null;
            }
            return s.split(" *, *");
        }
    }

}
