package com.orion.http.apache;

import com.orion.constant.Const;
import com.orion.http.useragent.StandardUserAgent;
import com.orion.utils.collect.Lists;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Apache HttpClient 配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/12 15:07
 */
public class ApacheClientConfig implements Serializable {

    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * socket超时时间
     */
    private int socketTimeout;

    /**
     * 请求超时时间
     */
    private int requestTimeout;

    /**
     * 是否开启logInterceptor
     */
    private boolean logInterceptor;

    /**
     * 同一域名下同时访问支持的次数
     */
    private int maxRoute;

    /**
     * 最大连接数
     */
    private int maxRequest;

    /**
     * 连接存活时间
     */
    private long connTimeToLive;

    /**
     * 连接存活时间单位
     */
    private TimeUnit connTimeToLiveTimeUnit;

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
     * 请求拦截器
     */
    private List<HttpRequestInterceptor> requestInterceptors;

    /**
     * 响应拦截器
     */
    private List<HttpResponseInterceptor> responseInterceptors;

    /**
     * connection manager
     */
    private HttpClientConnectionManager connectionManager;

    /**
     * 重试策略
     */
    private HttpRequestRetryHandler requestRetryHandler;

    /**
     * SSL Context
     */
    private SSLContext sslContext;

    /**
     * SSL Socket Factory
     */
    private LayeredConnectionSocketFactory sslSocketFactory;

    public ApacheClientConfig() {
        this.connectTimeout = Const.MS_S_3;
        this.socketTimeout = Const.MS_S_15;
        this.requestTimeout = Const.MS_S_15;
        this.userAgent = StandardUserAgent.CHROME_3;
        this.maxRoute = 12;
        this.maxRequest = 64;
        this.connTimeToLive = -1;
        this.connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
    }

    public ApacheClientConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public ApacheClientConfig logInterceptor(boolean open) {
        this.logInterceptor = open;
        return this;
    }

    public ApacheClientConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public ApacheClientConfig connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public ApacheClientConfig socketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public ApacheClientConfig requestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public ApacheClientConfig maxRoute(int route) {
        this.maxRoute = route;
        return this;
    }

    public ApacheClientConfig maxRequest(int maxRequest) {
        this.maxRequest = maxRequest;
        return this;
    }

    public ApacheClientConfig connTimeToLive(long connTimeToLive) {
        this.connTimeToLive = connTimeToLive;
        return this;
    }

    public ApacheClientConfig connTimeToLive(long connTimeToLive, TimeUnit connTimeToLiveTimeUnit) {
        this.connTimeToLive = connTimeToLive;
        this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
        return this;
    }

    public ApacheClientConfig userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public ApacheClientConfig cookies(CookieStore cookies) {
        this.cookies = cookies;
        return this;
    }

    public ApacheClientConfig requestInterceptor(HttpRequestInterceptor interceptor) {
        if (requestInterceptors == null) {
            this.requestInterceptors = new ArrayList<>();
        }
        requestInterceptors.add(interceptor);
        return this;
    }

    public ApacheClientConfig responseInterceptor(HttpResponseInterceptor interceptor) {
        if (responseInterceptors == null) {
            this.responseInterceptors = new ArrayList<>();
        }
        responseInterceptors.add(interceptor);
        return this;
    }

    public ApacheClientConfig connectionManager(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        return this;
    }

    public ApacheClientConfig noRetry() {
        this.requestRetryHandler = ApacheClientRetryPolicy.NO_RETRY.getHandler();
        return this;
    }

    public ApacheClientConfig requestRetryHandler(HttpRequestRetryHandler requestRetryHandler) {
        this.requestRetryHandler = requestRetryHandler;
        return this;
    }

    public ApacheClientConfig requestRetryHandler(ApacheClientRetryPolicy policy) {
        this.requestRetryHandler = policy.getHandler();
        return this;
    }

    public ApacheClientConfig sslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public ApacheClientConfig sslSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
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

    public int getMaxRoute() {
        return maxRoute;
    }

    public int getMaxRequest() {
        return maxRequest;
    }

    public long getConnTimeToLive() {
        return connTimeToLive;
    }

    public TimeUnit getConnTimeToLiveTimeUnit() {
        return connTimeToLiveTimeUnit;
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

    public HttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public HttpRequestRetryHandler getRequestRetryHandler() {
        return requestRetryHandler;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public LayeredConnectionSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public HttpClientBuilder buildClientBuilder() {
        return this.buildClientBuilder(false);
    }

    public HttpClientBuilder buildClientBuilder(boolean ssl) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)
                .setSocketTimeout(this.socketTimeout)
                .setConnectionRequestTimeout(this.requestTimeout)
                .build();
        HttpClientBuilder builder = HttpClients.custom()
                .setConnectionTimeToLive(this.connTimeToLive, this.connTimeToLiveTimeUnit)
                .setMaxConnTotal(this.maxRequest)
                .setMaxConnPerRoute(this.maxRoute)
                .setDefaultRequestConfig(requestConfig);
        if (this.connectionManager != null) {
            builder.setConnectionManager(this.connectionManager);
        }
        if (this.requestRetryHandler != null) {
            builder.setRetryHandler(this.requestRetryHandler);
        }
        if (this.userAgent != null) {
            builder.setUserAgent(this.userAgent);
        }
        if (this.logInterceptor) {
            ApacheLoggerInterceptor loggerInterceptor = new ApacheLoggerInterceptor();
            builder.addInterceptorFirst((HttpRequestInterceptor) loggerInterceptor)
                    .addInterceptorLast((HttpResponseInterceptor) loggerInterceptor);
        }
        if (!Lists.isEmpty(requestInterceptors)) {
            requestInterceptors.forEach(builder::addInterceptorFirst);
        }
        if (!Lists.isEmpty(responseInterceptors)) {
            requestInterceptors.forEach(builder::addInterceptorLast);
        }
        // builder.setProxy(new HttpHost("127.0.0.1", 8888));
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

    public CloseableHttpClient buildClient() {
        return this.buildClientBuilder(false).build();
    }

    public CloseableHttpClient buildClient(boolean ssl) {
        return this.buildClientBuilder(ssl).build();
    }

    @Override
    public String toString() {
        return "ApacheConfig{" +
                "connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                ", requestTimeout=" + requestTimeout +
                ", logInterceptor=" + logInterceptor +
                ", route=" + maxRoute +
                ", userAgent='" + userAgent + '\'' +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                '}';
    }

}
