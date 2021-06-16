package com.orion.utils;

import com.orion.constant.Const;
import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.mutable.MutableString;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;

/**
 * url工具类
 *
 * @author Li
 * @version 1.0.0
 * @since 2019/9/10 14:26
 */
public class Urls {

    private Urls() {
    }

    /**
     * 获取url资源
     *
     * @param url url
     * @return url source
     */
    public static String getUrlSource(String url) {
        String[] uri = url.split("/");
        return uri[uri.length - 1].split("\\?")[0];
    }

    /**
     * 将map参数拼接为url
     *
     * @param request 请求参数
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request) {
        return buildQueryString(request, null, false, null);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request 请求参数
     * @param encode  是否编码
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request, boolean encode) {
        return buildQueryString(request, null, encode, null);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request       请求参数
     * @param encode        是否编码
     * @param encodeCharset 编码格式
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request, boolean encode, String encodeCharset) {
        return buildQueryString(request, null, encode, encodeCharset);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request    请求参数
     * @param skipFields 跳过构建字段
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request, List<String> skipFields) {
        return buildQueryString(request, skipFields, false, null);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request    请求参数
     * @param skipFields 跳过构建字段
     * @param encode     是否编码
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request, List<String> skipFields, boolean encode) {
        return buildQueryString(request, skipFields, encode, null);
    }

    /**
     * 将map参数拼接为url
     *
     * @param request       请求参数
     * @param skipFields    跳过构建字段
     * @param encode        是否编码
     * @param encodeCharset 编码格式
     * @return url
     */
    public static String buildQueryString(Map<String, ?> request, List<String> skipFields, boolean encode, String encodeCharset) {
        if (request == null || request.isEmpty()) {
            return Strings.EMPTY;
        }
        List<String> fieldNames = new ArrayList<>(request.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = Strings.newBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = Strings.EMPTY;
            Object o = request.get(fieldName);
            if (o != null) {
                fieldValue = o.toString();
            }
            boolean skip = false;
            if (skipFields != null) {
                for (String skipField : skipFields) {
                    if (skipField != null && fieldName != null && skipField.trim().equals(fieldName.trim())) {
                        skip = true;
                        break;
                    }
                }
            }
            if (!skip) {
                if (Strings.isNotBlank(fieldValue)) {
                    if (encode) {
                        sb.append(fieldName).append("=").append(encode(fieldValue, encodeCharset)).append("&");
                    } else {
                        sb.append(fieldName).append("=").append(fieldValue).append("&");
                    }
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
    public static MutableHashMap<String, String> getQueryString(String url) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        if (Strings.isBlank(url)) {
            return map;
        }
        url = decode(url);
        String[] query = url.split("\\?");
        String[] entries;
        if (query.length == 1) {
            entries = query[0].split("&");
        } else if (query.length == 2) {
            entries = query[1].split("&");
        } else {
            return map;
        }
        for (String entry : entries) {
            String[] kv = entry.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            } else if (kv.length == 1) {
                map.put(kv[0], null);
            }
        }
        return map;
    }

    /**
     * 获取url参数
     *
     * @param url url
     * @param key key
     * @return value
     */
    public static MutableString queryExt(String url, String key) {
        return new MutableString(query(url, key));
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
    public static MutableHashMap<String, String> queries(String url, String... keys) {
        Map<String, String> queryString = getQueryString(url);
        MutableHashMap<String, String> res = new MutableHashMap<>();
        if (keys != null) {
            for (String key : keys) {
                res.put(key, queryString.get(key));
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
        return encode(url, Const.UTF_8);
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
            if (charset == null) {
                return URLEncoder.encode(url, Const.UTF_8);
            }
            return URLEncoder.encode(url, charset);
        } catch (Exception e) {
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
        return decode(url, Const.UTF_8);
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
            if (charset == null) {
                return URLDecoder.decode(url, Const.UTF_8);
            }
            return URLDecoder.decode(url, charset);
        } catch (Exception e) {
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
        private String host = Strings.EMPTY;

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
            this.setProtocolPort();
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
                queryString = buildQueryString(query);
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
            if (Const.PROTOCOL_HTTP.equals(this.protocol)) {
                this.port = 80;
            } else if (Const.PROTOCOL_HTTPS.equals(this.protocol)) {
                this.port = 443;
            } else if (Const.PROTOCOL_FTP.equals(this.protocol)) {
                this.port = 21;
            } else if (Const.PROTOCOL_SSH.equals(this.protocol)) {
                this.port = 22;
            }
        }

    }

}
