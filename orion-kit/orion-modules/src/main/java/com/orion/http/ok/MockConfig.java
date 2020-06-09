package com.orion.http.ok;

/**
 * Mock 配置类
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/8 10:33
 */
@SuppressWarnings("ALL")
public class MockConfig {

    /**
     * call超时时间
     */
    private long callTimeout = 60;

    /**
     * 连接超时时间
     */
    private long connectTimeout = 60;

    /**
     * 读超时时间
     */
    private long readTimeout = 60;

    /**
     * 写超时时间
     */
    private long writeTimeout = 60;

    /**
     * 是否开启logInterceptor
     */
    private boolean logInterceptor;

    /**
     * ssl证书文件路径
     */
    private String certPath;

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

    public String getCertPath() {
        return certPath;
    }

    public MockConfig setCertPath(String certPath) {
        this.certPath = certPath;
        return this;
    }

    @Override
    public String toString() {
        return "MockConfig{" +
                "callTimeout=" + callTimeout +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", logInterceptor=" + logInterceptor +
                ", certPath='" + certPath + '\'' +
                '}';
    }

}
