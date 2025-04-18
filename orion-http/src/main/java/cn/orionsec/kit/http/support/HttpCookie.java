/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.http.support;

import cn.orionsec.kit.lang.constant.StandardHttpHeader;
import cn.orionsec.kit.lang.define.collect.MutableHashMap;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.convert.Converts;
import cn.orionsec.kit.lang.utils.time.Dates;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Cookies
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/7/1 10:13
 */
public class HttpCookie implements Serializable {

    public static final String[] EXPIRES_DATE = new String[]{
            "EEE, dd MMM yyyy HH:mm:ss 'GMT'",
            "EEE, dd-MMM-yyyy HH:mm:ss 'GMT'",
            "EEE, d MMM yyyy HH:mm:ss 'GMT'",
            "EEE, d-MMM-yyyy HH:mm:ss 'GMT'",
    };

    public static final String COOKIE = StandardHttpHeader.COOKIE;

    public static final String SET_COOKIE = StandardHttpHeader.SET_COOKIE;

    /**
     * 键值对
     */
    private MutableHashMap<String, String> values;

    /**
     * 过期时间
     */
    private Date expires;

    /**
     * maxAge
     */
    private Integer maxAge;

    /**
     * domain
     */
    private String domain;

    /**
     * path
     */
    private String path;

    /**
     * 是否使用 ssl
     */
    private boolean secure;

    /**
     * 是否为 httpOnly
     */
    private boolean httpOnly;

    public HttpCookie() {
    }

    public HttpCookie(String cookie) {
        this.analysisCookie(cookie);
    }

    private void analysisCookie(String cookie) {
        cookie = cookie.trim();
        int cookieIndex = cookie.toLowerCase().indexOf("cookie:");
        if (cookieIndex != -1) {
            cookie = cookie.substring(cookieIndex + 7);
        }
        String[] entities = cookie.split(";");
        for (String entity : entities) {
            this.analysisEntry(entity);
        }
    }

    private void analysisEntry(String entity) {
        String[] kv = entity.split("=");
        String key = kv[0].toLowerCase().trim();
        if (kv.length == 1) {
            switch (key) {
                case "secure":
                    this.secure = true;
                    break;
                case "httponly":
                    this.httpOnly = true;
                    break;
                default:
                    if (values == null) {
                        values = new MutableHashMap<>();
                    }
                    values.put(key, null);
                    break;
            }
        } else if (kv.length == 2) {
            String value = kv[1].trim();
            switch (key) {
                case "expires":
                    this.expires = Dates.parse(value, Locale.US, EXPIRES_DATE);
                    break;
                case "max-age":
                    this.maxAge = Converts.toInt(value);
                    break;
                case "domain":
                    this.domain = value;
                    break;
                case "path":
                    this.path = value;
                    break;
                default:
                    if (values == null) {
                        values = new MutableHashMap<>();
                    }
                    values.put(key, value);
                    break;
            }
        }
    }

    public HttpCookie addValue(String key, String value) {
        if (values == null) {
            this.values = new MutableHashMap<>();
        }
        values.put(key, value);
        return this;
    }

    public MutableHashMap<String, String> getValues() {
        return values;
    }

    public void setValues(MutableHashMap<String, String> values) {
        this.values = values;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String cookie() {
        return COOKIE + ": " + this;
    }

    public String setCookie() {
        return SET_COOKIE + ": " + this;
    }

    @Override
    public String toString() {
        Valid.notNull(values, "cookies value is null");
        StringBuilder cookie = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (i != 0) {
                cookie.append("; ");
            }
            cookie.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        if (expires != null) {
            cookie.append("; ").append("expires").append("=").append(Dates.format(expires, EXPIRES_DATE[0], Locale.US));
        }
        if (maxAge != null) {
            cookie.append("; ").append("max-age").append("=").append(maxAge);
        }
        if (domain != null) {
            cookie.append("; ").append("domain").append("=").append(domain);
        }
        if (path != null) {
            cookie.append("; ").append("path").append("=").append(path);
        }
        if (secure) {
            cookie.append("; ").append("secure");
        }
        if (httpOnly) {
            cookie.append("; ").append("HttpOnly");
        }
        return cookie.toString();
    }
}
