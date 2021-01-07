package com.orion.http.apache;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Map;

/**
 * Apache Cookie
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/30 18:38
 */
public class ApacheCookieStore extends BasicCookieStore {

    public ApacheCookieStore addCookie(String key, String value, String domain) {
        return this.addCookie(key, value, domain, "/");
    }

    public ApacheCookieStore addCookie(String key, String value, String domain, String path) {
        BasicClientCookie c = new BasicClientCookie(key, value);
        c.setDomain(domain);
        c.setPath(path);
        addCookie(c);
        return this;
    }

    public ApacheCookieStore addCookies(Map<String, String> map, String domain) {
        return this.addCookies(map, domain, "/");
    }

    public ApacheCookieStore addCookies(Map<String, String> map, String domain, String path) {
        map.forEach((k, v) -> {
            BasicClientCookie c = new BasicClientCookie(k, v);
            c.setDomain(domain);
            c.setPath(path);
            addCookie(c);
        });
        return this;
    }

}
