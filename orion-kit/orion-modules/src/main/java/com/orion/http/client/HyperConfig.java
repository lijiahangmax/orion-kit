package com.orion.http.client;

import org.apache.http.conn.socket.LayeredConnectionSocketFactory;

import javax.net.ssl.SSLContext;
import java.io.Serializable;

/**
 * Hyper HttpClient 配置
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/12 15:07
 */
public class HyperConfig implements Serializable {

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
     * SSL Context
     */
    private SSLContext sslContext;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    /**
     * SSL Socket Factory
     */
    private LayeredConnectionSocketFactory sslSocketFactory;

    public HyperConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public HyperConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HyperConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public HyperConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public HyperConfig setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public boolean isLogInterceptor() {
        return logInterceptor;
    }

    public int getRoute() {
        return route;
    }

    public HyperConfig setRoute(int route) {
        this.route = route;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public HyperConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public HyperConfig setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public LayeredConnectionSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public HyperConfig setSslSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
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
