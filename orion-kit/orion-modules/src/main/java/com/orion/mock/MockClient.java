package com.orion.mock;

import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Mock Client
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:33
 */
public class MockClient {

    static {
        // 开启log打印
        reloadClient(new MockConfig().setCallTimeout(60000).setConnectTimeout(60000).setReadTimeout(60000).setWriteTimeout(60000).logInterceptor());
    }

    /**
     * 重新配置client
     *
     * @param mockConfig client
     */
    public static void reloadClient(MockConfig mockConfig) {
        ClientInstance.init(mockConfig);
    }

    /**
     * 获取 Client
     *
     * @return Client
     */
    public static OkHttpClient getClient() {
        OkHttpClient client = ClientInstance.client;
        if (client == null) {
            ClientInstance.init(null);
            client = ClientInstance.client;
        }
        return client;
    }

    /**
     * 获取 SSLClient
     *
     * @return Client
     */
    public static OkHttpClient getSslClient() {
        OkHttpClient client = ClientInstance.sslClient;
        if (client == null) {
            ClientInstance.init(null);
            client = ClientInstance.sslClient;
        }
        return client;
    }

    private static class ClientInstance {

        private static OkHttpClient client;
        private static OkHttpClient sslClient;

        private ClientInstance() {
        }

        private static void init(MockConfig mockConfig) {
            if (mockConfig == null) {
                ClientInstance.client = new OkHttpClient();
                ClientInstance.sslClient = new OkHttpClient();
            } else {
                OkHttpClient.Builder client = new OkHttpClient.Builder()
                        .callTimeout(mockConfig.getCallTimeout(), TimeUnit.SECONDS)
                        .connectTimeout(mockConfig.getConnectTimeout(), TimeUnit.SECONDS)
                        .readTimeout(mockConfig.getReadTimeout(), TimeUnit.SECONDS)
                        .writeTimeout(mockConfig.getWriteTimeout(), TimeUnit.SECONDS);
                OkHttpClient.Builder sslClient = new OkHttpClient.Builder()
                        .callTimeout(mockConfig.getCallTimeout(), TimeUnit.SECONDS)
                        .connectTimeout(mockConfig.getConnectTimeout(), TimeUnit.SECONDS)
                        .readTimeout(mockConfig.getReadTimeout(), TimeUnit.SECONDS)
                        .writeTimeout(mockConfig.getWriteTimeout(), TimeUnit.SECONDS)
                        .sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault(), DEFAULT_MANAGER);
                if (mockConfig.isLogInterceptor()) {
                    MockLoggerInterceptor mockLoggerInterceptor = new MockLoggerInterceptor();
                    client.addInterceptor(mockLoggerInterceptor);
                    sslClient.addInterceptor(mockLoggerInterceptor);
                }
                ClientInstance.client = client.build();
                ClientInstance.sslClient = sslClient.build();
            }
        }

        private static final X509TrustManager DEFAULT_MANAGER = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

    }

}
