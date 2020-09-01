package com.orion.http.ok;

import com.orion.lang.DefaultX509TrustManager;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;

/**
 * Mock Client
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 10:33
 */
public class MockClient {

    static {
        ClientInstance.init(new MockClientConfig().logInterceptor());
        ClientInstance.initSsl(new MockClientConfig()
                .sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault())
                .trustManager(DefaultX509TrustManager.DEFAULT_X509_TRUST_MANAGER)
                .logInterceptor());
    }

    /**
     * 重新配置client
     *
     * @param mockClientConfig config
     * @return reload client
     */
    public static OkHttpClient reloadClient(MockClientConfig mockClientConfig) {
        ClientInstance.init(mockClientConfig);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param mockClientConfig config
     * @return reload ssl client
     */
    public static OkHttpClient reloadSslClient(MockClientConfig mockClientConfig) {
        ClientInstance.initSsl(mockClientConfig);
        return ClientInstance.sslClient;
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
            ClientInstance.initSsl(null);
            client = ClientInstance.sslClient;
        }
        return client;
    }

    /**
     * 设置 Client
     *
     * @param client Client
     * @return this client
     */
    public static OkHttpClient setClient(OkHttpClient client) {
        ClientInstance.client = client;
        return client;
    }

    /**
     * 设置 sslClient
     *
     * @param sslClient sslClient
     * @return this ssl client
     */
    public static OkHttpClient setSslClient(OkHttpClient sslClient) {
        ClientInstance.sslClient = sslClient;
        return sslClient;
    }

    private static class ClientInstance {

        private static OkHttpClient client;
        private static OkHttpClient sslClient;

        private ClientInstance() {
        }

        private static void init(MockClientConfig mockClientConfig) {
            init(mockClientConfig, false);
        }

        private static void initSsl(MockClientConfig mockClientConfig) {
            init(mockClientConfig, true);
        }

        private static void init(MockClientConfig mockClientConfig, boolean ssl) {
            if (mockClientConfig == null) {
                if (ssl) {
                    ClientInstance.sslClient = new OkHttpClient();
                } else {
                    ClientInstance.client = new OkHttpClient();
                }
            } else {
                if (ssl) {
                    ClientInstance.sslClient = mockClientConfig.createClient(true);
                } else {
                    ClientInstance.client = mockClientConfig.createClient(false);
                }
            }
        }
    }

}
