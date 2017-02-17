package com.qy.insurance.cloud.core.security.ssl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @task:
 * @discrption: Override the default system level SslContext and HostnameVerifier
 * @author: Aere
 * @date: 2017/2/16 15:07
 * @version: 1.0.0
 */
@Configuration
public class DefaultSslConfig {

    @Value("${server.ssl.key-store}")
    private String ksPath;
    @Value("${server.ssl.key-store-password}")
    private String ksPassword;
    @Value("${server.ssl.key-password}")
    private String keyPassword;

    private PublicKey publicKey;

    private boolean finish;

    @PostConstruct
    public void initial() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException,
            CertificateException, UnrecoverableKeyException, IOException {
        publicKey = getPublicKey();
        trustManagerExtend();
        HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, sslSession) -> true);
        finish = true;
    }

    private void trustManagerExtend() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {


        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // Using null here initialises the TMF with the default trust store.
        tmf.init((KeyStore) null);

        // Get hold of the default trust manager
        X509TrustManager x509Tm = null;
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                x509Tm = (X509TrustManager) tm;
                break;
            }
        }

        // Wrap it in your own class.
        final X509TrustManager finalTm = x509Tm;
        X509TrustManager customTm = new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                if (finalTm != null) {
                    return finalTm.getAcceptedIssuers();
                }
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                for (X509Certificate certificate : chain) {
                    try {
                        certificate.verify(publicKey);
                    } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | SignatureException e) {
                        continue;
                    }
                    return;
                }
                if (finalTm != null) {
                    finalTm.checkServerTrusted(chain, authType);
                }
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                if (finalTm != null) {
                    finalTm.checkClientTrusted(chain, authType);
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{customTm}, new SecureRandom());

        // You don't have to set this as the default context,
        // it depends on the library you're using.
        SSLContext.setDefault(sslContext);
//        SSLContexts.
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    private PublicKey getPublicKey()
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        InputStream readStream = null;
        if (ksPath.toLowerCase().startsWith("classpath")) { // Resolve Keystore in classpath
            String path = ksPath.substring(ksPath.indexOf(':') + 1);
            readStream = new ClassPathResource(path).getInputStream();
        }
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(readStream, ksPassword.toCharArray());
            Certificate certificate = ks.getCertificate("selfsigned");// This is the alias name when you created the jks
            return certificate.getPublicKey();
        } finally {
            if (readStream != null) {
                readStream.close();
            }
        }
    }

    public boolean isFinish() {
        return finish;
    }
}
