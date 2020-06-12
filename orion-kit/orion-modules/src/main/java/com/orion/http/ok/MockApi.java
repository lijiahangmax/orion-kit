package com.orion.http.ok;

import com.orion.utils.Strings;

import java.io.Serializable;
import java.util.Map;

/**
 * Mock API
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 12:42
 */
public class MockApi implements Serializable {

    private static final long serialVersionUID = -4875745193358767L;

    /**
     * url
     */
    private String url;

    /**
     * method
     */
    private MockMethod method;

    /**
     * contentType
     */
    private MockContent contentType;

    /**
     * tag
     */
    private String tag;

    /**
     * ssl
     */
    private boolean ssl;

    public MockApi() {
    }

    public MockApi(String url) {
        this.url = url;
    }

    public MockApi(String url, MockMethod method) {
        this.url = url;
        this.method = method;
    }

    public MockApi(String url, MockMethod method, MockContent contentType) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
    }

    public MockApi(String url, MockMethod method, MockContent contentType, String tag) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.tag = tag;
    }

    public MockApi(String url, MockMethod method, MockContent contentType, String tag, boolean ssl) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.tag = tag;
        this.ssl = ssl;
    }

    /**
     * 格式化url的参数 {}
     *
     * @param o 参数
     * @return this
     */
    public MockApi format(Object... o) {
        this.url = Strings.format(this.url, o);
        return this;
    }

    /**
     * 格式化url的参数 ${?}
     *
     * @param map 参数
     * @return this
     */
    public MockApi format(Map<String, Object> map) {
        this.url = Strings.format(this.url, map);
        return this;
    }

    /**
     * 获取MockRequest对象
     *
     * @return MockRequest
     */
    public MockRequest getRequest() {
        return new MockRequest()
                .url(this.url)
                .method(this.method)
                .contentType(this.contentType)
                .tag(this.tag)
                .ssl(this.ssl);
    }

    public String getUrl() {
        return url;
    }

    public MockApi setUrl(String url) {
        this.url = url;
        return this;
    }

    public MockMethod getMethod() {
        return method;
    }

    public MockApi setMethod(MockMethod method) {
        this.method = method;
        return this;
    }

    public MockContent getContentType() {
        return contentType;
    }

    public MockApi setContentType(MockContent contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public MockApi setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public boolean isSsl() {
        return ssl;
    }

    public MockApi setSsl(boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    @Override
    public String toString() {
        return url;
    }

}
