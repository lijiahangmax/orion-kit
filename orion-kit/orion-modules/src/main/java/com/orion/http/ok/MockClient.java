package com.orion.http.ok;

import com.orion.lang.DefaultX509TrustManager;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;
import java.net.InetSocketAddress;
import java.net.Proxy;
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
        ClientInstance.init(new MockConfig().logInterceptor());
        ClientInstance.initSsl(new MockConfig().logInterceptor());
    }

    /**
     * 重新配置client
     *
     * @param mockConfig config
     * @return reload client
     */
    public static OkHttpClient reloadClient(MockConfig mockConfig) {
        ClientInstance.init(mockConfig);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param mockConfig config
     * @return reload ssl client
     */
    public static OkHttpClient reloadSslClient(MockConfig mockConfig) {
        ClientInstance.initSsl(mockConfig);
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

        public static void main(String[] args) {
            System.out.println(Mocks.get("http://www.hao123.com/").getBodyString());
        }

        private static void init(MockConfig mockConfig) {
            init(mockConfig, false);
        }

        private static void initSsl(MockConfig mockConfig) {
            init(mockConfig, true);
        }

        private static void init(MockConfig mockConfig, boolean ssl) {
            if (mockConfig == null) {
                if (ssl) {
                    ClientInstance.sslClient = new OkHttpClient();
                } else {
                    ClientInstance.client = new OkHttpClient();
                }
            } else {
                OkHttpClient.Builder client = new OkHttpClient.Builder()
                        .callTimeout(mockConfig.getCallTimeout(), TimeUnit.MILLISECONDS)
                        .connectTimeout(mockConfig.getConnectTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(mockConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                        .writeTimeout(mockConfig.getWriteTimeout(), TimeUnit.MILLISECONDS);
                if (mockConfig.isLogInterceptor()) {
                    client.addInterceptor(new MockLoggerInterceptor());
                }
                String proxyHost = mockConfig.getProxyHost();
                if (proxyHost != null) {
                    client.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, mockConfig.getProxyPort())));
                }
                if (ssl) {
                    client.sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault(), DefaultX509TrustManager.DEFAULT_TRUST_MANAGER);
                    ClientInstance.sslClient = client.build();
                } else {
                    ClientInstance.client = client.build();
                }
            }
        }
    }

}
