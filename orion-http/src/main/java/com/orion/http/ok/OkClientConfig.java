package com.orion.http.ok;

import okhttp3.ConnectionSpec;
import okhttp3.CookieJar;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 10:33
 */
public class OkClientConfig implements Serializable {

    /**
     * call超时时间
     */
    private long callTimeout;

    /**
     * 连接超时时间
     */
    private long connectTimeout;

    /**
     * 读超时时间
     */
    private long readTimeout;

    /**
     * 写超时时间
     */
    private long writeTimeout;

    /**
     * 是否开启logInterceptor
     */
    private boolean logInterceptor;

    /**
     * cookie
     */
    private CookieJar cookies;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    /**
     * 同一域名下同时访问支持的次数
     */
    private int maxRoute;

    /**
     * 最大请求数量
     */
    private int maxRequest;

    /**
     * 调度器线程池
     */
    private ExecutorService dispatcherPool;

    /**
     * 调度器
     */
    private Dispatcher dispatcher;

    /**
     * ssl factory
     */
    private SSLSocketFactory sslSocketFactory;

    /**
     * 凭证管理器
     */
    private X509TrustManager trustManager;

    /**
     * ssl 版本规范
     */
    private List<ConnectionSpec> connectionSpecs;

    public OkClientConfig() {
        this.callTimeout = 15000;
        this.connectTimeout = 3000;
        this.readTimeout = 15000;
        this.writeTimeout = 15000;
        this.maxRoute = 12;
        this.maxRequest = 64;
    }

    public OkClientConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public OkClientConfig cookies(CookieJar cookies) {
        this.cookies = cookies;
        return this;
    }

    public OkClientConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public OkClientConfig callTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public OkClientConfig connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public OkClientConfig readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public OkClientConfig writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public OkClientConfig sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public OkClientConfig trustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
        return this;
    }

    public OkClientConfig maxRoute(int maxRoute) {
        this.maxRoute = maxRoute;
        return this;
    }

    public OkClientConfig maxRequest(int maxRequest) {
        this.maxRequest = maxRequest;
        return this;
    }

    public OkClientConfig dispatcherPool(ExecutorService dispatcherPool) {
        this.dispatcherPool = dispatcherPool;
        return this;
    }

    public OkClientConfig dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    public OkClientConfig connectionSpecs(List<ConnectionSpec> connectionSpecs) {
        if (this.connectionSpecs == null) {
            this.connectionSpecs = connectionSpecs;
        } else {
            this.connectionSpecs.addAll(connectionSpecs);
        }
        return this;
    }

    public OkClientConfig connectionSpecs(ConnectionSpec connectionSpec) {
        if (this.connectionSpecs == null) {
            this.connectionSpecs = new ArrayList<>();
        }
        this.connectionSpecs.add(connectionSpec);
        return this;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public boolean isLogInterceptor() {
        return logInterceptor;
    }

    public CookieJar getCookies() {
        return cookies;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public int getMaxRoute() {
        return maxRoute;
    }

    public int getMaxRequest() {
        return maxRequest;
    }

    public ExecutorService getDispatcherPool() {
        return dispatcherPool;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public List<ConnectionSpec> getConnectionSpecs() {
        return connectionSpecs;
    }

    public OkHttpClient.Builder buildClientBuilder() {
        return buildClientBuilder(false);
    }

    public OkHttpClient.Builder buildClientBuilder(boolean ssl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .callTimeout(this.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(this.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(this.writeTimeout, TimeUnit.MILLISECONDS);
        if (this.dispatcher != null) {
            builder.dispatcher(this.dispatcher);
        } else {
            if (this.dispatcherPool != null || this.maxRoute != 0 || this.maxRequest != 0) {
                if (this.dispatcherPool != null) {
                    this.dispatcher = new Dispatcher(this.dispatcherPool);
                } else {
                    this.dispatcher = new Dispatcher();
                }
                if (this.maxRoute != 0) {
                    this.dispatcher.setMaxRequestsPerHost(this.maxRoute);
                }
                if (this.maxRequest != 0) {
                    this.dispatcher.setMaxRequests(this.maxRequest);
                }
                builder.dispatcher(this.dispatcher);
            }
        }
        if (this.logInterceptor) {
            builder.addInterceptor(new OkLoggerInterceptor());
        }
        if (this.proxyHost != null && this.proxyPort != 0) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyHost, this.proxyPort)));
        }
        if (this.cookies != null) {
            builder.cookieJar(this.cookies);
        }
        if (ssl) {
            builder.sslSocketFactory(this.sslSocketFactory, this.trustManager)
                    .connectionSpecs(this.connectionSpecs);
        }
        return builder;
    }

    public OkHttpClient buildClient() {
        return this.buildClientBuilder(false).build();
    }

    public OkHttpClient buildClient(boolean ssl) {
        return this.buildClientBuilder(ssl).build();
    }

    @Override
    public String toString() {
        return "OkHttpConfig{" +
                "callTimeout=" + callTimeout +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", logInterceptor=" + logInterceptor +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                '}';
    }

}
