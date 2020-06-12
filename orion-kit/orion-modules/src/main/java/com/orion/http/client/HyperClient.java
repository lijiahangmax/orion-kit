package com.orion.http.client;

import com.orion.utils.Streams;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * Hyper HttpClient
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 14:21
 */
public class HyperClient {

    static {
        ClientInstance.init(new HyperConfig().logInterceptor());
        ClientInstance.initSsl(new HyperConfig().logInterceptor());
    }

    /**
     * 重新配置client
     *
     * @param config config
     * @return reload client
     */
    public static CloseableHttpClient reloadClient(HyperConfig config) {
        ClientInstance.init(config);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param config config
     * @return reload ssl client
     */
    public static CloseableHttpClient reloadSslClient(HyperConfig config) {
        ClientInstance.initSsl(config);
        return ClientInstance.sslClient;
    }

    /**
     * 获取 Client
     *
     * @return Client
     */
    public static CloseableHttpClient getClient() {
        CloseableHttpClient client = ClientInstance.client;
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
    public static CloseableHttpClient getSslClient() {
        CloseableHttpClient client = ClientInstance.sslClient;
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
    public static CloseableHttpClient setClient(CloseableHttpClient client) {
        ClientInstance.client = client;
        return client;
    }

    /**
     * 设置 sslClient
     *
     * @param sslClient sslClient
     * @return this ssl client
     */
    public static CloseableHttpClient setSslClient(CloseableHttpClient sslClient) {
        ClientInstance.sslClient = sslClient;
        return sslClient;
    }

    /**
     * 关闭 client
     */
    public static void closeClient() {
        Streams.closeQuietly(ClientInstance.client);
    }

    /**
     * 关闭 ssl client
     */
    public static void closeSslClient() {
        Streams.closeQuietly(ClientInstance.sslClient);
    }

    private static class ClientInstance {

        private static CloseableHttpClient client;
        private static CloseableHttpClient sslClient;

        private ClientInstance() {
        }

        private static void init(HyperConfig config) {
            init(config, false);
        }

        private static void initSsl(HyperConfig config) {
            init(config, true);
        }

        private static void init(HyperConfig config, boolean ssl) {
            if (config == null) {
                if (ssl) {
                    sslClient = HttpClients.custom().build();
                } else {
                    client = HttpClients.custom().build();
                }
            } else {
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(config.getConnectTimeout())
                        .setSocketTimeout(config.getSocketTimeout())
                        .setConnectionRequestTimeout(config.getRequestTimeout())
                        .build();
                HttpClientBuilder builder = HttpClients.custom()
                        .setMaxConnPerRoute(config.getRoute())
                        .setDefaultRequestConfig(requestConfig);
                String userAgent = config.getUserAgent();
                if (userAgent != null) {
                    builder.setUserAgent(userAgent);
                }
                if (config.isLogInterceptor()) {
                    HyperLoggerInterceptor loggerInterceptor = new HyperLoggerInterceptor();
                    builder.addInterceptorFirst((HttpRequestInterceptor) loggerInterceptor)
                            .addInterceptorFirst((HttpResponseInterceptor) loggerInterceptor);
                }
                String proxyHost = config.getProxyHost();
                if (proxyHost != null) {
                    builder.setProxy(new HttpHost(proxyHost, config.getProxyPort()));
                }
                if (ssl) {
                    builder.setSSLContext(config.getSslContext())
                            .setSSLSocketFactory(config.getSslSocketFactory());
                    ClientInstance.sslClient = builder.build();
                } else {
                    ClientInstance.client = builder.build();
                }
            }
        }
    }

}
