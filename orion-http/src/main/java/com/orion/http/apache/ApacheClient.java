package com.orion.http.apache;

import com.orion.constant.StandardTlsVersion;
import com.orion.lang.DefaultX509TrustManager;
import com.orion.utils.Exceptions;
import com.orion.utils.io.Streams;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Apache HttpClient
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 14:21
 */
public class ApacheClient {

    static {
        ClientInstance.init(new ApacheClientConfig().logInterceptor());
        try {
            SSLContext sc = SSLContext.getInstance(StandardTlsVersion.TLS_1_1);
            sc.init(null, new TrustManager[]{DefaultX509TrustManager.DEFAULT_X509_TRUST_MANAGER}, null);
            ClientInstance.initSsl(new ApacheClientConfig()
                    .sslContext(sc)
                    .sslSocketFactory(new SSLConnectionSocketFactory(sc))
                    .logInterceptor());
        } catch (Exception e) {
            Exceptions.printStacks(e);
            throw Exceptions.init("init apache client error " + e.getMessage());
        }
    }

    /**
     * 重新配置client
     *
     * @param config config
     * @return reload client
     */
    public static CloseableHttpClient reloadClient(ApacheClientConfig config) {
        ClientInstance.init(config);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param config config
     * @return reload ssl client
     */
    public static CloseableHttpClient reloadSslClient(ApacheClientConfig config) {
        ClientInstance.initSsl(config);
        return ClientInstance.sslClient;
    }

    /**
     * 获取 Client
     *
     * @return Client
     */
    public static CloseableHttpClient getClient() {
        return ClientInstance.client;
    }

    /**
     * 获取 SSLClient
     *
     * @return Client
     */
    public static CloseableHttpClient getSslClient() {
        return ClientInstance.sslClient;
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
        Streams.close(ClientInstance.client);
    }

    /**
     * 关闭 ssl client
     */
    public static void closeSslClient() {
        Streams.close(ClientInstance.sslClient);
    }

    private static class ClientInstance {

        private static CloseableHttpClient client;
        private static CloseableHttpClient sslClient;

        private ClientInstance() {
        }

        private static void init(ApacheClientConfig config) {
            init(config, false);
        }

        private static void initSsl(ApacheClientConfig config) {
            init(config, true);
        }

        private static void init(ApacheClientConfig config, boolean ssl) {
            if (ssl) {
                ClientInstance.sslClient = config.buildClient(true);
            } else {
                ClientInstance.client = config.buildClient(false);
            }
        }
    }

}
