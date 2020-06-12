package com.orion.http.ok;

import com.orion.lang.DefaultX509TrustManager;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;

/**
 * Mock Client
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:33
 */
@SuppressWarnings("ALL")
public class MockClient {

    static {
        // 开启log打印
        reloadClient(new MockConfig()
                .setCallTimeout(3000)
                .setConnectTimeout(3000)
                .setReadTimeout(15000)
                .setWriteTimeout(15000)
                .logInterceptor());
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

    /**
     * 设置 Client
     *
     * @param client Client
     */
    public static void setClient(OkHttpClient client) {
        ClientInstance.client = client;
    }

    /**
     * 设置 sslClient
     *
     * @param sslClient sslClient
     */
    public static void setSslClient(OkHttpClient sslClient) {
        ClientInstance.sslClient = sslClient;
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
                        .sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault(), DefaultX509TrustManager.DEFAULT_TRUST_MANAGER);
                if (mockConfig.isLogInterceptor()) {
                    MockLoggerInterceptor mockLoggerInterceptor = new MockLoggerInterceptor();
                    client.addInterceptor(mockLoggerInterceptor);
                    sslClient.addInterceptor(mockLoggerInterceptor);
                }
                ClientInstance.client = client.build();
                ClientInstance.sslClient = sslClient.build();
            }
        }

    }

}
