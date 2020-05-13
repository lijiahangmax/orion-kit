package com.orion.utils;

import com.orion.lang.MapEntry;
import com.orion.lang.StringExt;
import com.orion.utils.collect.Maps;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import java.util.Collections;

/**
 * url工具类
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/9/10 14:26
 */
public class Urls {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 将map参数拼接为url 拼接签名
     *
     * @param request 请求参数
     * @return url
     */
    public static String buildUrl(Map<String, ?> request) {
        return buildUrl(request, null);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request    请求参数
     * @param skipFields 跳过构建字段
     * @return url
     */
    public static String buildUrl(Map<String, ?> request, List<String> skipFields) {
        if (request == null || request.isEmpty()) {
            return "";
        }
        List<String> fieldNames = new ArrayList<>(request.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = Strings.newBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = "";
            Object o = request.get(fieldName);
            if (o != null) {
                fieldValue = o.toString();
            }
            boolean skip = false;
            if (skipFields != null) {
                for (String skipField : skipFields) {
                    if (skipField != null && fieldName != null && skipField.trim().equals(fieldName.trim())) {
                        skip = true;
                    }
                }
            }
            if (!skip) {
                if (Strings.isNotBlank(fieldValue)) {
                    sb.append(fieldName).append("=").append(fieldValue).append("&");
                } else {
                    sb.append(fieldName).append("&");
                }
            }
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 将url中的参数转化为map
     *
     * @param url url
     * @return 参数
     */
    public static Map<String, String> getQueryString(String url) {
        if (Strings.isBlank(url)) {
            return new HashMap<>();
        }
        url = decode(url);
        String[] query = url.split("\\?");
        String[] entries;
        if (query.length == 1) {
            entries = query[0].split("&");
        } else if (query.length == 2) {
            entries = query[1].split("&");
        } else {
            return new HashMap<>();
        }
        MapEntry<String, String>[] mapEntries = new MapEntry[entries.length];
        for (int i = 0, len = entries.length; i < len; i++) {
            String[] kv = entries[i].split("=");
            if (kv.length == 2) {
                mapEntries[i] = new MapEntry<>(kv[0], kv[1]);
            } else if (kv.length == 1) {
                mapEntries[i] = new MapEntry<>(kv[0], "");
            }
        }
        return Maps.of(mapEntries);
    }

    /**
     * 将url中的参数转化为map
     *
     * @param url url
     * @return 参数
     */
    public static Map<String, StringExt> getQueryStringExt(String url) {
        if (Strings.isBlank(url)) {
            return new HashMap<>();
        }
        url = decode(url);
        String[] query = url.split("\\?");
        String[] entries;
        if (query.length == 1) {
            entries = query[0].split("&");
        } else if (query.length == 2) {
            entries = query[1].split("&");
        } else {
            return new HashMap<>();
        }
        MapEntry<String, StringExt>[] mapEntries = new MapEntry[entries.length];
        for (int i = 0, len = entries.length; i < len; i++) {
            String[] kv = entries[i].split("=");
            if (kv.length == 2) {
                mapEntries[i] = new MapEntry<>(kv[0], new StringExt(kv[1]));
            } else if (kv.length == 1) {
                mapEntries[i] = new MapEntry<>(kv[0], new StringExt());
            }
        }
        return Maps.of(mapEntries);
    }

    /**
     * 获取url参数
     *
     * @param url url
     * @param key key
     * @return value
     */
    public static StringExt queryExt(String url, String key) {
        Map<String, StringExt> queryString = getQueryStringExt(url);
        for (Map.Entry<String, StringExt> entry : queryString.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return new StringExt();
    }

    /**
     * 获取url参数
     *
     * @param url url
     * @param key key
     * @return value
     */
    public static String query(String url, String key) {
        Map<String, String> queryString = getQueryString(url);
        for (Map.Entry<String, String> entry : queryString.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取url参数
     *
     * @param url  url
     * @param keys keys
     * @return values
     */
    public static Map<String, StringExt> queryExts(String url, String... keys) {
        Map<String, StringExt> queryString = getQueryStringExt(url);
        Map<String, StringExt> res = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                StringExt stringExt = queryString.get(key);
                if (stringExt == null) {
                    res.put(key, new StringExt());
                } else {
                    res.put(key, stringExt);
                }
            }
        }
        return res;
    }

    /**
     * 获取url参数
     *
     * @param url  url
     * @param keys keys
     * @return values
     */
    public static Map<String, String> querys(String url, String... keys) {
        Map<String, String> queryString = getQueryString(url);
        Map<String, String> res = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                String s = queryString.get(key);
                if (s == null) {
                    res.put(key, null);
                } else {
                    res.put(key, s);
                }
            }
        }
        return res;
    }

    /**
     * 对url进行编码 默认UTF-8
     *
     * @param url url
     * @return 编码后的url
     */
    public static String encode(String url) {
        return encode(url, DEFAULT_CHARSET);
    }

    /**
     * 对url进行编码
     *
     * @param url     url
     * @param charset 编码格式
     * @return 编码后的url
     */
    public static String encode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }

    /**
     * 对url进行解码 默认UTF-8
     *
     * @param url url
     * @return 解码后的url
     */
    public static String decode(String url) {
        return decode(url, DEFAULT_CHARSET);
    }

    /**
     * 对url进行解码 默认UTF-8
     *
     * @param url     url
     * @param charset 解码格式
     * @return 解码后的url
     */
    public static String decode(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return url;
        }
    }

    /**
     * 打开输入流
     *
     * @param url url
     * @return 输入流
     * @throws IOException IOException
     */
    public static InputStream openInputStream(URL url) throws IOException {
        URLConnection con = url.openConnection();
        con.connect();
        return con.getInputStream();
    }

    /**
     * 打开输入流
     *
     * @param url url
     * @return 输入流
     * @throws IOException IOException
     */
    public static InputStream openInputStream(String url) throws IOException {
        URL u = new URL(url);
        URLConnection con = u.openConnection();
        con.connect();
        return con.getInputStream();
    }

    /**
     * 获取url返回的字节数组
     *
     * @param url url
     * @return 数组
     * @throws IOException IOException
     */
    public static byte[] getUrlBytes(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.connect();
        return Streams.toByteArray(con.getInputStream());
    }

    /**
     * 获取url返回的字节数组
     *
     * @param url url
     * @return 数组
     * @throws IOException IOException
     */
    public static byte[] getUrlBytes(URL url) throws IOException {
        URLConnection con = url.openConnection();
        con.connect();
        return Streams.toByteArray(con.getInputStream());
    }

    /**
     * url流
     *
     * @return url流
     */
    public static UrlStream getUrlStream() {
        return new UrlStream();
    }

    public static class UrlStream {

        /**
         * 协议
         */
        private String protocol = "http";

        /**
         * 主机
         */
        private String host = "";

        /**
         * 端口
         */
        private int port;

        /**
         * path
         */
        private String path;

        /**
         * query
         */
        private Map<String, String> query;

        /**
         * queryString
         */
        private String queryString;

        /**
         * route
         */
        private String route;

        private UrlStream() {
        }

        public UrlStream protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public UrlStream host(String host) {
            this.host = host;
            return this;
        }

        public UrlStream port(int port) {
            this.port = port;
            return this;
        }

        public UrlStream path(String path) {
            this.path = path;
            return this;
        }

        public UrlStream query(Map<String, String> query) {
            if (this.query == null) {
                this.query = query;
            } else {
                this.query.putAll(query);
            }
            return this;
        }

        public UrlStream query(String key, String value) {
            if (this.query == null) {
                this.query = new HashMap<>();
            }
            this.query.put(key, value);
            return this;
        }

        public UrlStream route(String route) {
            this.route = route;
            return this;
        }

        public String getUrl() {
            return getUrl(false);
        }

        public String getUrl(boolean showPort) {
            setProtocolPort();
            StringBuilder sb = new StringBuilder();
            sb.append(protocol).append("://").append(host);
            if (showPort) {
                sb.append(':').append(port);
            }
            if (path != null) {
                if (path.charAt(0) == '/') {
                    sb.append(path);
                } else {
                    sb.append('/').append(path);
                }
            }
            if (query != null) {
                queryString = buildUrl(query);
                if (!Strings.isBlank(queryString)) {
                    sb.append("?");
                }
                sb.append(queryString);
            }
            if (route != null) {
                if (route.charAt(0) == '#') {
                    sb.append(route);
                } else {
                    sb.append('#').append(route);
                }
            }
            return sb.toString();
        }

        public String getProtocol() {
            return protocol;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public String getPath() {
            return path;
        }

        public Map<String, String> getQuery() {
            return query;
        }

        public String getQueryString() {
            return queryString;
        }

        public String getRoute() {
            return route;
        }

        @Override
        public String toString() {
            return this.getUrl(false);
        }

        /**
         * 设置protocol的端口
         */
        private void setProtocolPort() {
            if (this.port != 0) {
                return;
            }
            if ("http".equals(this.protocol)) {
                this.port = 80;
            } else if ("https".equals(this.protocol)) {
                this.port = 443;
            } else if ("ftp".equals(this.protocol)) {
                this.port = 21;
            } else if ("ssh".equals(this.protocol)) {
                this.port = 22;
            }
        }

    }

}
