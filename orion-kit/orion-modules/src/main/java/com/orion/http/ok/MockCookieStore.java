package com.orion.http.ok;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock Cookie
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/29 17:33
 */
public class MockCookieStore implements CookieJar {

    private ArrayList<Cookie> defaultList = new ArrayList<>();

    private Map<String, List<Cookie>> urlCookies = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Cookie> loadForRequest(HttpUrl url) {
        ArrayList<Cookie> clone = (ArrayList<Cookie>) defaultList.clone();
        List<Cookie> cookies = urlCookies.get(url.toString());
        if (cookies != null) {
            clone.addAll(cookies);
        }
        return clone;
    }

    public MockCookieStore addCookie(Cookie cookie) {
        defaultList.add(cookie);
        return this;
    }

    public MockCookieStore addCookies(List<Cookie> cookies) {
        defaultList.addAll(cookies);
        return this;
    }

    public MockCookieStore addCookie(String url, Cookie cookie) {
        List<Cookie> cookies = urlCookies.computeIfAbsent(url, k -> new ArrayList<>());
        cookies.add(cookie);
        return this;
    }

    public MockCookieStore addCookie(String url, List<Cookie> cookies) {
        List<Cookie> store = urlCookies.computeIfAbsent(url, k -> new ArrayList<>());
        store.addAll(cookies);
        return this;
    }

    public MockCookieStore addCookie(String key, String value, String domain) {
        defaultList.add(new Cookie.Builder().name(key).value(value).domain(domain).build());
        return this;
    }

    public MockCookieStore addCookie(Map<String, String> cookies, String domain) {
        cookies.forEach((k, v) -> defaultList.add(new Cookie.Builder().name(k).value(v).domain(domain).build()));
        return this;
    }

    public MockCookieStore addCookie(String url, String key, String value, String domain) {
        List<Cookie> cookies = urlCookies.computeIfAbsent(url, k -> new ArrayList<>());
        cookies.add(new Cookie.Builder().name(key).value(value).domain(domain).build());
        return this;
    }

    public MockCookieStore addCookie(String url, Map<String, String> cookies, String domain) {
        List<Cookie> store = urlCookies.computeIfAbsent(url, k -> new ArrayList<>());
        cookies.forEach((k, v) -> store.add(new Cookie.Builder().name(k).value(v).domain(domain).build()));
        return this;
    }

}
