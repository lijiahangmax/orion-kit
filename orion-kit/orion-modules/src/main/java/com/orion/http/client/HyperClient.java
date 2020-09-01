package com.orion.http.client;

import com.orion.lang.DefaultX509TrustManager;
import com.orion.utils.io.Streams;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Hyper HttpClient
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/12 14:21
 */
public class HyperClient {

    static {
        ClientInstance.init(new HyperClientConfig().logInterceptor());
        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.1");
            sc.init(null, new TrustManager[]{DefaultX509TrustManager.DEFAULT_X509_TRUST_MANAGER}, null);
            ClientInstance.initSsl(new HyperClientConfig()
                    .sslContext(sc)
                    .sslSocketFactory(new SSLConnectionSocketFactory(sc))
                    .logInterceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新配置client
     *
     * @param config config
     * @return reload client
     */
    public static CloseableHttpClient reloadClient(HyperClientConfig config) {
        ClientInstance.init(config);
        return ClientInstance.client;
    }

    /**
     * 重新配置ssl client
     *
     * @param config config
     * @return reload ssl client
     */
    public static CloseableHttpClient reloadSslClient(HyperClientConfig config) {
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

        private static void init(HyperClientConfig config) {
            init(config, false);
        }

        private static void initSsl(HyperClientConfig config) {
            init(config, true);
        }

        private static void init(HyperClientConfig config, boolean ssl) {
            if (config == null) {
                if (ssl) {
                    sslClient = HttpClients.custom().build();
                } else {
                    client = HttpClients.custom().build();
                }
            } else {
                if (ssl) {
                    ClientInstance.sslClient = config.createClient(true);
                } else {
                    ClientInstance.client = config.createClient(false);
                }
            }
        }
    }

}
