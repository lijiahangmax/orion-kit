/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.orion.http.ok;

import com.orion.lang.able.Buildable;
import com.orion.lang.constant.Const;
import com.orion.lang.utils.collect.Lists;
import okhttp3.*;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 配置构建
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/4/8 10:33
 */
public class OkClientBuilder implements Buildable<OkHttpClient> {

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
     * 是否开启 logInterceptor
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
     * 请求响应拦截器
     */
    private List<Interceptor> interceptors;

    /**
     * 网络请求拦截器
     */
    private List<Interceptor> networkInterceptors;

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

    public OkClientBuilder() {
        this.connectTimeout = Const.MS_S_10;
        this.callTimeout = Const.MS_S_15;
        this.readTimeout = Const.MS_S_15;
        this.writeTimeout = Const.MS_S_15;
        this.maxRoute = 12;
        this.maxRequest = 64;
    }

    public static OkClientBuilder create() {
        return new OkClientBuilder();
    }

    public OkClientBuilder proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public OkClientBuilder cookies(CookieJar cookies) {
        this.cookies = cookies;
        return this;
    }

    public OkClientBuilder logInterceptor(boolean open) {
        this.logInterceptor = open;
        return this;
    }

    public OkClientBuilder logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public OkClientBuilder interceptor(Interceptor interceptor) {
        if (interceptors == null) {
            this.interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
        return this;
    }

    public OkClientBuilder networkInterceptor(Interceptor interceptor) {
        if (networkInterceptors == null) {
            this.networkInterceptors = new ArrayList<>();
        }
        networkInterceptors.add(interceptor);
        return this;
    }

    public OkClientBuilder callTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public OkClientBuilder connectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public OkClientBuilder readTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public OkClientBuilder writeTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public OkClientBuilder maxRoute(int maxRoute) {
        this.maxRoute = maxRoute;
        return this;
    }

    public OkClientBuilder maxRequest(int maxRequest) {
        this.maxRequest = maxRequest;
        return this;
    }

    public OkClientBuilder dispatcherPool(ExecutorService dispatcherPool) {
        this.dispatcherPool = dispatcherPool;
        return this;
    }

    public OkClientBuilder dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    public OkClientBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public OkClientBuilder trustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
        return this;
    }

    public OkClientBuilder connectionSpecs(List<ConnectionSpec> connectionSpecs) {
        if (this.connectionSpecs == null) {
            this.connectionSpecs = connectionSpecs;
        } else {
            this.connectionSpecs.addAll(connectionSpecs);
        }
        return this;
    }

    public OkClientBuilder connectionSpecs(ConnectionSpec connectionSpec) {
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .callTimeout(callTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        if (dispatcher != null) {
            builder.dispatcher(dispatcher);
        } else {
            if (dispatcherPool != null || maxRoute != 0 || maxRequest != 0) {
                if (dispatcherPool != null) {
                    this.dispatcher = new Dispatcher(dispatcherPool);
                } else {
                    this.dispatcher = new Dispatcher();
                }
                if (this.maxRoute != 0) {
                    dispatcher.setMaxRequestsPerHost(maxRoute);
                }
                if (this.maxRequest != 0) {
                    dispatcher.setMaxRequests(maxRequest);
                }
                builder.dispatcher(dispatcher);
            }
        }
        if (logInterceptor) {
            builder.addInterceptor(new OkLoggerInterceptor());
        }
        if (!Lists.isEmpty(interceptors)) {
            interceptors.forEach(builder::addInterceptor);
        }
        if (!Lists.isEmpty(networkInterceptors)) {
            interceptors.forEach(builder::addNetworkInterceptor);
        }
        if (proxyHost != null && proxyPort != 0) {
            builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
        }
        if (cookies != null) {
            builder.cookieJar(cookies);
        }
        if (sslSocketFactory != null && trustManager != null) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }
        if (connectionSpecs != null) {
            builder.connectionSpecs(connectionSpecs);
        }
        return builder;
    }

    @Override
    public OkHttpClient build() {
        return this.buildClientBuilder().build();
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
