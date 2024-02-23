package com.orion.web.servlet.web;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.time.Dates;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Cookie 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/3/13 11:12
 */
public class CookiesExt {

    public static final int SESSION = -1;
    public static final int NO_SAVE = 0;
    public static final int ONE_HOUR = 3600;
    public static final int ONE_DAY = 3600 * 24;
    public static final int ONE_WEEK = 3600 * 24 * 7;

    private CookiesExt() {
    }

    /**
     * 获取cookie
     *
     * @param request request
     * @param key     key
     * @return cookies
     */
    public static String get(HttpServletRequest request, String key) {
        return get(request, key, null);
    }

    /**
     * 获取所有cookie
     *
     * @param request request
     * @param key     key
     * @param def     默认值
     * @return cookies
     */
    public static String get(HttpServletRequest request, String key, String def) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return def;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return def;
    }

    /**
     * 获取所有cookie
     *
     * @param request request
     * @param keys    keys
     * @return cookies
     */
    public static Map<String, String> gets(HttpServletRequest request, String... keys) {
        Map<String, String> map = new HashMap<>(Const.CAPACITY_16);
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return map;
        }
        for (Cookie cookie : cookies) {
            for (String key : keys) {
                if (key.equals(cookie.getName())) {
                    map.put(key, cookie.getValue());
                }
            }
        }
        return map;
    }

    /**
     * 获取所有cookie
     *
     * @param request request
     * @return cookies
     */
    public static Map<String, String> list(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(Const.CAPACITY_16);
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return map;
        }
        for (Cookie cookie : cookies) {
            map.put(cookie.getName(), cookie.getValue());
        }
        return map;
    }

    /**
     * cookie的数量
     *
     * @param request request
     * @return 数量
     */
    public static int getCount(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return cookies == null ? 0 : cookies.length;
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     */
    public static void set(HttpServletResponse response, String key, String value) {
        set(response, key, value, ONE_DAY, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     */
    public static void set(HttpServletResponse response, String key, String value, int expire) {
        set(response, key, value, expire, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param path     路径
     */
    public static void set(HttpServletResponse response, String key, String value, int expire, String path) {
        set(response, key, value, expire, path, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param path     路径
     * @param domain   域名
     */
    public static void set(HttpServletResponse response, String key, String value, int expire, String path, String domain) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(Math.max(expire, -1));
        if (path != null) {
            cookie.setPath(path);
        } else {
            cookie.setPath(Const.ROOT_PATH);
        }
        if (domain != null && !Const.LOCALHOST.equals(domain)) {
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     */
    public static void set(HttpServletResponse response, String key, String value, Date expire) {
        set(response, key, value, expire, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param path     路径
     */
    public static void set(HttpServletResponse response, String key, String value, Date expire, String path) {
        set(response, key, value, expire, path, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param path     路径
     * @param domain   域名
     */
    public static void set(HttpServletResponse response, String key, String value, Date expire, String path, String domain) {
        set(response, key, value, (int) (Dates.intervalMs(expire, new Date()) / Const.MS_S_1), path, domain);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     */
    public static void sets(HttpServletResponse response, Map<String, String> map) {
        sets(response, map, ONE_DAY, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, int expire) {
        sets(response, map, expire, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     * @param path     路径
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, int expire, String path) {
        sets(response, map, expire, path, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, Date expire) {
        sets(response, map, expire, null, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     * @param path     路径
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, Date expire, String path) {
        sets(response, map, expire, path, null);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     * @param path     路径
     * @param domain   域名
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, Date expire, String path, String domain) {
        sets(response, map, (int) (Dates.intervalMs(expire, new Date()) / Const.MS_S_1), path, domain);
    }

    /**
     * 设置cookie的值
     *
     * @param response response
     * @param map      key, value
     * @param expire   过期时间
     * @param path     路径
     * @param domain   域名
     */
    public static void sets(HttpServletResponse response, Map<String, String> map, int expire, String path, String domain) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            set(response, entry.getKey(), entry.getValue(), expire, path, domain);
        }
    }

    /**
     * 删除cookie
     *
     * @param response response
     * @param key      key
     */
    public static void delete(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(NO_SAVE);
        cookie.setPath(Const.ROOT_PATH);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response response
     * @param keys     keys
     */
    public static void delete(HttpServletResponse response, String... keys) {
        for (String key : keys) {
            Cookie cookie = new Cookie(key, null);
            cookie.setMaxAge(NO_SAVE);
            cookie.setPath(Const.ROOT_PATH);
            response.addCookie(cookie);
        }
    }

}
