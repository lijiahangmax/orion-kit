package com.orion.mock;

import com.orion.utils.Strings;

/**
 * Mock API
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/4/9 12:42
 */
public enum MockApi {

    /**
     * 微信获取accessToken
     */
    WX_GET_ACCESS_TOKEN("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}", MockMethod.GET, null, "微信获取accessToken", false),

    /**
     * 微信公众号推送模板消息
     */
    WX_PUSH_TEMPLATE_MESSAGE("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={}", MockMethod.POST, MockContent.APPLICATION_JSON, "微信公众号推送模板消息", false),

    /**
     * 微信获取用户openId
     */
    WX_GET_USER_OPEN_ID("https://api.weixin.qq.com/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code", MockMethod.GET, null, "微信获取用户openId", false),

    /**
     * 微信获取用户信息
     */
    WX_GET_USER_INFO("https://api.weixin.qq.com/cgi-bin/user/info?access_token={}&openid={}", MockMethod.GET, null, "微信获取用户信息", false);

    /**
     * url
     */
    String url;

    /**
     * method
     */
    MockMethod method;

    /**
     * contentType
     */
    MockContent contentType;

    /**
     * tag
     */
    String tag;

    /**
     * ssl
     */
    boolean ssl;

    MockApi(String url, MockMethod method, MockContent contentType, String tag, boolean ssl) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.tag = tag;
        this.ssl = ssl;
    }

    /**
     * 格式化url的参数
     *
     * @param o 参数
     * @return this
     */
    public MockApi formatUrl(Object... o) {
        this.url = Strings.format(this.url, o);
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

}
