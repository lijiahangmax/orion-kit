package com.orion.servlet.web;

import com.orion.constant.StandardContentType;
import com.orion.lang.collect.MutableHashMap;
import com.orion.lang.mutable.MutableString;
import com.orion.utils.Urls;
import com.orion.utils.io.Streams;
import com.orion.utils.net.IPs;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

/**
 * Servlet工具类
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/14 17:10
 */
public class Servlets {

    private static final String USER_AGENT = "User-Agent";

    private Servlets() {
    }

    // ------------------------- request -------------------------

    /**
     * 获取请求参数
     *
     * @param request request
     * @param key     key
     * @return stringExt
     */
    public static MutableString getParameter(HttpServletRequest request, String key) {
        return new MutableString(request.getParameter(key));
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @param keys    keys
     * @return stringExt
     */
    public static MutableHashMap<String, String> getParameters(HttpServletRequest request, String... keys) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        for (String key : keys) {
            map.put(key, request.getParameter(key));
        }
        return map;
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @return stringExt
     */
    public static MutableHashMap<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MutableHashMap<String, String> map = new MutableHashMap<>();
        for (Map.Entry<String, String[]> es : parameterMap.entrySet()) {
            map.put(es.getKey(), es.getValue()[0]);
        }
        return map;
    }

    /**
     * 获取UA
     *
     * @param request request
     * @return UA
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(USER_AGENT);
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @param key     keys
     * @return 请求头
     */
    public static MutableString getHeader(HttpServletRequest request, String key) {
        return new MutableString(request.getHeader(key));
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @param keys    keys
     * @return 请求头
     */
    public static MutableHashMap<String, String> getHeaders(HttpServletRequest request, String... keys) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        for (String key : keys) {
            map.put(key, request.getHeader(key));
        }
        return map;
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return 请求头
     */
    public static MutableHashMap<String, String> getHeaderMap(HttpServletRequest request) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        Enumeration<String> it = request.getHeaderNames();
        while (it.hasMoreElements()) {
            String key = it.nextElement();
            map.put(key, request.getHeader(key));
        }
        return map;
    }

    /**
     * 通过httpServletRequest获取ip
     *
     * @param request request
     * @return IP地址 可能为null
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip;
        ip = IPs.checkIp(request.getHeader("x-forwarded-for"));
        if (ip != null) {
            return ip;
        }
        ip = IPs.checkIp(request.getHeader("Proxy-Client-IP"));
        if (ip != null) {
            return ip;
        }
        ip = IPs.checkIp(request.getHeader("X-Forwarded-For"));
        if (ip != null) {
            return ip;
        }
        ip = IPs.checkIp(request.getHeader("WL-Proxy-Client-IP"));
        if (ip != null) {
            return ip;
        }
        ip = IPs.checkIp(request.getHeader("X-Real-IP"));
        if (ip != null) {
            return ip;
        }
        return IPs.checkIp(request.getRemoteAddr());
    }

    /**
     * 获取请求方法
     *
     * @param request request
     * @return 请求方法
     */
    public static String getMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    /**
     * url请求参数
     *
     * @param request request
     * @return url请求参数
     */
    public static String getQueryString(HttpServletRequest request) {
        return request.getQueryString();
    }

    /**
     * url请求参数
     *
     * @param request request
     * @return url请求参数
     */
    public static MutableHashMap<String, String> getQueryStringMap(HttpServletRequest request) {
        return Urls.getQueryString(request.getQueryString());
    }

    /**
     * 获取资源路径
     *
     * @param request request
     * @return 资源路径
     */
    public static String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 获取全路径
     *
     * @param request request
     * @return 全路径
     */
    public static String getRequestURL(HttpServletRequest request) {
        return request.getRequestURL().toString();
    }

    /**
     * 获取协议
     *
     * @param request request
     * @return 协议
     */
    public static String getProtocol(HttpServletRequest request) {
        return request.getProtocol();
    }

    /**
     * 获取协议类型
     *
     * @param request request
     * @return 协议类型
     */
    public static String getScheme(HttpServletRequest request) {
        return request.getScheme();
    }

    /**
     * 获取内容长度
     *
     * @param request request
     * @return 内容长度
     */
    public static int getContentLength(HttpServletRequest request) {
        return request.getContentLength();
    }

    /**
     * 获取内容长度
     *
     * @param request request
     * @return 内容长度
     */
    public static long getContentLengthLong(HttpServletRequest request) {
        return request.getContentLengthLong();
    }

    /**
     * 获取内容类型
     *
     * @param request request
     * @return 内容类型
     */
    public static String getContentType(HttpServletRequest request) {
        return request.getContentType();
    }

    /**
     * 获取请求编码
     *
     * @param request request
     * @return 编码
     */
    public static String getCharset(HttpServletRequest request) {
        return request.getCharacterEncoding();
    }

    /**
     * 获取应用服务器IP 如: tomcat
     *
     * @param request request
     * @return 应用服务器IP
     */
    public static String getApplicationAddr(HttpServletRequest request) {
        return request.getLocalAddr();
    }

    /**
     * 获取应用服务器主机名称 如: tomcat
     *
     * @param request request
     * @return 应用服务器主机名称
     */
    public static String getApplicationName(HttpServletRequest request) {
        return request.getLocalName();
    }

    /**
     * 获取应用服务器端口 如: tomcat
     *
     * @param request request
     * @return 应用服务器端口
     */
    public static int getApplicationPort(HttpServletRequest request) {
        return request.getLocalPort();
    }

    /**
     * 获取发出请求的IP 如: nginx
     *
     * @param request request
     * @return 发出请求的IP
     */
    public static String getRequestAddr(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    /**
     * 获取发出请求的主机 如: nginx
     *
     * @param request request
     * @return 发出请求的主机
     */
    public static String getRequestHost(HttpServletRequest request) {
        return request.getRemoteHost();
    }

    /**
     * 获取发出请求的端口 如: nginx
     *
     * @param request request
     * @return 发出请求的端口
     */
    public static int getRequestPort(HttpServletRequest request) {
        return request.getRemotePort();
    }

    /**
     * URL发出请求的主机 如: 域名 www.xx.com
     *
     * @param request request
     * @return 发出请求的主机
     */
    public static String getServerName(HttpServletRequest request) {
        return request.getServerName();
    }

    /**
     * URL发出请求的端口 如: 域名
     *
     * @param request request
     * @return 发出请求的端口
     */
    public static int getServerPort(HttpServletRequest request) {
        return request.getServerPort();
    }

    /**
     * 获取请求体的流
     *
     * @param request request
     * @return BufferedReader
     * @throws IOException IOException
     */
    public static BufferedReader getReader(HttpServletRequest request) throws IOException {
        return request.getReader();
    }

    /**
     * 获取请求体的流
     *
     * @param request request
     * @return ServletInputStream
     * @throws IOException IOException
     */
    public static ServletInputStream getInputStream(HttpServletRequest request) throws IOException {
        return request.getInputStream();
    }

    /**
     * 获取请求体
     *
     * @param request request
     * @return body
     * @throws IOException IOException
     */
    public static byte[] getBody(HttpServletRequest request) throws IOException {
        return Streams.toByteArray(request.getInputStream());
    }

    // ------------------------- response -------------------------

    /**
     * 页面重定向
     *
     * @param response response
     * @param url      重定向url
     */
    public static void redirect(HttpServletResponse response, String url) throws IOException {
        response.sendRedirect(url);
    }

    /**
     * 获取UA
     *
     * @param response response
     * @return UA
     */
    public static String getUserAgent(HttpServletResponse response) {
        return response.getHeader(USER_AGENT);
    }

    /**
     * 获取ContentType
     *
     * @param response response
     * @return ContentType
     */
    public static String getContentType(HttpServletResponse response) {
        return response.getHeader(StandardContentType.CONTENT_TYPE);
    }

    /**
     * 获取请求头
     *
     * @param response response
     * @param key      keys
     * @return 请求头
     */
    public static MutableString getHeader(HttpServletResponse response, String key) {
        return new MutableString(response.getHeader(key));
    }

    /**
     * 获取请求头
     *
     * @param response response
     * @param keys     keys
     * @return 请求头
     */
    public static MutableHashMap<String, String> getHeaders(HttpServletResponse response, String... keys) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        for (String key : keys) {
            map.put(key, response.getHeader(key));
        }
        return map;
    }

    /**
     * 获取请求头
     *
     * @param response response
     * @return 请求头
     */
    public static MutableHashMap<String, String> getHeaderMap(HttpServletResponse response) {
        MutableHashMap<String, String> map = new MutableHashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            map.put(headerName, response.getHeader(headerName));
        }
        return map;
    }

    /**
     * 设置响应头
     *
     * @param response response
     * @param key      key
     * @param value    value
     */
    public static void setHander(HttpServletResponse response, String key, String value) {
        response.setHeader(key, value);
    }

    /**
     * 设置响应头
     *
     * @param response response
     * @param handler  key,value
     */
    public static void setHanders(HttpServletResponse response, Map<String, String> handler) {
        for (Map.Entry<String, String> entry : handler.entrySet()) {
            response.setHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取HTTP状态码
     *
     * @param response response
     * @return HTTP状态码
     */
    public static int getStatus(HttpServletResponse response) {
        return response.getStatus();
    }

    /**
     * 设置HTTP状态码
     *
     * @param response response
     * @param status   status
     */
    public static void getStatus(HttpServletResponse response, int status) {
        response.setStatus(status);
    }

    /**
     * 设置响应体长度
     *
     * @param response response
     * @param length   长度
     */
    public static void setContentLength(HttpServletResponse response, int length) {
        response.setContentLength(length);
    }

    /**
     * 设置响应体长度
     *
     * @param response response
     * @param length   长度
     */
    public static void setContentLengthLong(HttpServletResponse response, long length) {
        response.setContentLengthLong(length);
    }

    /**
     * 设置响应内容类型
     *
     * @param response    response
     * @param contentType 响应内容类型
     */
    public static void setContentType(HttpServletResponse response, String contentType) {
        response.setContentType(contentType);
    }

    /**
     * 设置响应编码
     *
     * @param response response
     * @param charset  编码格式
     */
    public static void setCharset(HttpServletResponse response, String charset) {
        response.setCharacterEncoding(charset);
    }

    /**
     * 获取响应流
     *
     * @param response response
     * @return 响应流
     * @throws IOException IOException
     */
    public static PrintWriter getWriter(HttpServletResponse response) throws IOException {
        return response.getWriter();
    }

    /**
     * 获取响应流
     *
     * @param response response
     * @return 响应流
     * @throws IOException IOException
     */
    public static ServletOutputStream getOutputStream(HttpServletResponse response) throws IOException {
        return response.getOutputStream();
    }

    /**
     * 将输入流设置到response的输出流
     *
     * @param response response
     * @param in       输入流
     * @throws IOException IOException
     */
    public static void transfer(HttpServletResponse response, InputStream in) throws IOException {
        response.setContentType("application/octet-stream");
        Streams.transfer(in, response.getOutputStream());
    }

    /**
     * 将输入流设置到response的输出流
     *
     * @param response response
     * @param in       输入流
     * @param fileName 文件名
     * @throws IOException IOException
     */
    public static void transfer(HttpServletResponse response, InputStream in, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        Streams.transfer(in, response.getOutputStream());
    }

    /**
     * 将输入流设置到response的输出流
     *
     * @param response response
     * @param reader   输入流
     * @throws IOException IOException
     */
    public static void transfer(HttpServletResponse response, Reader reader) throws IOException {
        response.setContentType("application/octet-stream");
        Streams.transfer(reader, response.getWriter());
    }

    /**
     * 将输入流设置到response的输出流
     *
     * @param response response
     * @param reader   输入流
     * @param fileName 文件名
     * @throws IOException IOException
     */
    public static void transfer(HttpServletResponse response, Reader reader, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        Streams.transfer(reader, response.getWriter());
    }

    /**
     * 解决跨域
     *
     * @param response response
     */
    public static void cross(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,login_token,X-PINGOTHER,Content-Type, Accept, Origin, Last-Modified");
        response.setHeader("Access-Control-Max-Age", "86400");
    }

}
