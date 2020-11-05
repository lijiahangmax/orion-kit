package com.orion.http.ok;

import com.orion.lang.DefaultX509TrustManager;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;

/**
 * OkHttp Client
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 10:33
 */
public class OkClient {

    static {
        ClientInstance.init(new OkClientConfig().logInterceptor());
        ClientInstance.initSsl(new OkClientConfig()
                .sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault())
                .trustManager(DefaultX509TrustManager.DEFAULT_X509_TRUST_MANAGER)
                .connectionSpecs(ConnectionSpec.COMPATIBLE_TLS)
                .logInterceptor());
    }

    /**
     * 重新配置client
     *
     * @param okClientConfig config
     * @return reload client
     */
    public static OkHttpClient reloadClient(OkClientConfig okClientConfig) {
        ClientInstance.init(okClientConfig);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param okClientConfig config
     * @return reload ssl client
     */
    public static OkHttpClient reloadSslClient(OkClientConfig okClientConfig) {
        ClientInstance.initSsl(okClientConfig);
        return ClientInstance.sslClient;
    }

    /**
     * 获取 Client
     *
     * @return Client
     */
    public static OkHttpClient getClient() {
        return ClientInstance.client;
    }

    /**
     * 获取 SSLClient
     *
     * @return Client
     */
    public static OkHttpClient getSslClient() {
        return ClientInstance.sslClient;
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

        private static void init(OkClientConfig okClientConfig) {
            init(okClientConfig, false);
        }

        private static void initSsl(OkClientConfig okClientConfig) {
            init(okClientConfig, true);
        }

        private static void init(OkClientConfig okClientConfig, boolean ssl) {
            if (ssl) {
                ClientInstance.sslClient = okClientConfig.buildClient(true);
            } else {
                ClientInstance.client = okClientConfig.buildClient(false);
            }
        }
    }

}
