package com.orion.http.ok;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Mock 配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/4/8 10:33
 */
@SuppressWarnings("ALL")
public class MockClientConfig implements Serializable {

    /**
     * call超时时间
     */
    private long callTimeout = 3000;

    /**
     * 连接超时时间
     */
    private long connectTimeout = 3000;

    /**
     * 读超时时间
     */
    private long readTimeout = 15000;

    /**
     * 写超时时间
     */
    private long writeTimeout = 15000;

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

    private SSLSocketFactory sslSocketFactory;

    private X509TrustManager trustManager;

    public MockClientConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public MockClientConfig cookies(CookieJar cookies) {
        this.cookies = cookies;
        return this;
    }

    public MockClientConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public MockClientConfig callTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public MockClientConfig connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public MockClientConfig readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public MockClientConfig writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public MockClientConfig sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public MockClientConfig trustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
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

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public OkHttpClient.Builder createClientBuilder() {
        return createClientBuilder(false);
    }

    public OkHttpClient.Builder createClientBuilder(boolean ssl) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .callTimeout(this.callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(this.readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(this.writeTimeout, TimeUnit.MILLISECONDS);
        if (this.logInterceptor) {
            client.addInterceptor(new MockLoggerInterceptor());
        }
        if (this.proxyHost != null && this.proxyPort != 0) {
            client.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyHost, this.proxyPort)));
        }
        if (this.cookies != null) {
            client.cookieJar(this.cookies);
        }
        if (ssl) {
            client.sslSocketFactory(sslSocketFactory, trustManager);
        }
        return client;
    }

    public OkHttpClient createClient() {
        return this.createClientBuilder(false).build();
    }

    public OkHttpClient createClient(boolean ssl) {
        return this.createClientBuilder(ssl).build();
    }

    @Override
    public String toString() {
        return "MockConfig{" +
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
