package com.orion.http.common;

import com.orion.lang.collect.MutableHashMap;
import com.orion.utils.Valid;
import com.orion.utils.convert.Converts;
import com.orion.utils.time.Dates;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Cookies
 *
 * @author ljh15
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

    public static final String COOKIE = "Cookie";

    public static final String SET_COOKIE = "Set-Cookie";

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
     * 是否使用ssl
     */
    private boolean secure;

    /**
     * 是否为httpOnly
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
        String key = kv[0].toLowerCase().trim(), value;
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
            value = kv[1].trim();
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

    public MutableHashMap<String, String> getValues() {
        return values;
    }

    public HttpCookie setValues(MutableHashMap<String, String> values) {
        this.values = values;
        return this;
    }

    public HttpCookie addValue(String key, String value) {
        if (this.values == null) {
            this.values = new MutableHashMap<>();
        }
        this.values.put(key, value);
        return this;
    }

    public Date getExpires() {
        return expires;
    }

    public HttpCookie setExpires(Date expires) {
        this.expires = expires;
        return this;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public HttpCookie setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public HttpCookie setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getPath() {
        return path;
    }

    public HttpCookie setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isSecure() {
        return secure;
    }

    public HttpCookie setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public HttpCookie setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public String cookie() {
        return COOKIE + ": " + this.toString();
    }

    private String setCookie() {
        return SET_COOKIE + ": " + this.toString();
    }

    @Override
    public String toString() {
        Valid.notNull(values, "Cookies value is null");
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
