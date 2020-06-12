package com.orion.http.ok;

import java.io.Serializable;

/**
 * Mock 配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:33
 */
@SuppressWarnings("ALL")
public class MockConfig implements Serializable {

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
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    public MockConfig proxy(String host, int port) {
        this.proxyHost = host;
        this.proxyPort = port;
        return this;
    }

    public MockConfig logInterceptor() {
        this.logInterceptor = true;
        return this;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public MockConfig setCallTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
        return this;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public MockConfig setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public MockConfig setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public MockConfig setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public boolean isLogInterceptor() {
        return logInterceptor;
    }

    public MockConfig setLogInterceptor(boolean logInterceptor) {
        this.logInterceptor = logInterceptor;
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
