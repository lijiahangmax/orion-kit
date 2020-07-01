package com.orion.http.client;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.Serializable;

/**
 * Hyper HttpClient 配置
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 15:07
 */
public class HyperClientConfig implements Serializable {

    /**
     * 连接超时时间
     */
    private int connectTimeout = 3000;

    /**
     * socket超时时间
     */
    private int socketTimeout = 15000;

    /**
     * 请求超时时间
     */
    private int requestTimeout = 15000;

    /**
     * 是否开启logInterceptor
     */
    private boolean logInterceptor;

    /**
     * 同意域名下同时访问支持的次数
     */
    private int route = 10;

    /**
     * UA
     */
    private String userAgent;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    /**
     * cookie
     */
    private CookieStore cookies;

    /**
     * SSL Context
     */
    private SSLContext sslContext;

    /**
     * SSL Socket Factory
     */
    private LayeredConnectionSocketFactory sslSocketFactory;

    public HyperClientConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public HyperClientConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public HyperClientConfig connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public HyperClientConfig socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public HyperClientConfig requestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public HyperClientConfig route(int route) {
        this.route = route;
        return this;
    }

    public HyperClientConfig userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HyperClientConfig cookies(CookieStore cookies) {
        this.cookies = cookies;
        return this;
    }

    public HyperClientConfig sslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public HyperClientConfig sslSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public boolean isLogInterceptor() {
        return logInterceptor;
    }

    public int getRoute() {
        return route;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public CookieStore getCookies() {
        return cookies;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public LayeredConnectionSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public HttpClientBuilder createClientBuilder() {
        return this.createClientBuilder(false);
    }

    public HttpClientBuilder createClientBuilder(boolean ssl) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .setConnectionRequestTimeout(this.requestTimeout)
                .build();
        HttpClientBuilder builder = HttpClients.custom()
                .setMaxConnPerRoute(this.route)
                .setDefaultRequestConfig(requestConfig);
        String userAgent = this.userAgent;
        if (userAgent != null) {
            builder.setUserAgent(userAgent);
        }
        if (this.logInterceptor) {
            HyperLoggerInterceptor loggerInterceptor = new HyperLoggerInterceptor();
            builder.addInterceptorFirst((HttpRequestInterceptor) loggerInterceptor)
                    .addInterceptorFirst((HttpResponseInterceptor) loggerInterceptor);
        }
        if (this.proxyHost != null && this.proxyPort != 0) {
            builder.setProxy(new HttpHost(this.proxyHost, this.proxyPort));
        }
        if (this.cookies != null) {
            builder.setDefaultCookieStore(this.cookies);
        }
        if (ssl) {
            builder.setSSLContext(this.sslContext)
                    .setSSLSocketFactory(this.sslSocketFactory);
        }
        return builder;
    }

    public CloseableHttpClient createClient() {
        return this.createClientBuilder(false).build();
    }

    public CloseableHttpClient createClient(boolean ssl) {
        return this.createClientBuilder(ssl).build();
    }

    @Override
    public String toString() {
        return "HyperConfig{" +
                "connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                ", requestTimeout=" + requestTimeout +
                ", logInterceptor=" + logInterceptor +
                ", route=" + route +
                ", userAgent='" + userAgent + '\'' +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                '}';
    }

}
