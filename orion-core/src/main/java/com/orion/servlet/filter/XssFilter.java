package com.orion.servlet.filter;

import com.orion.lang.Null;
import com.orion.utils.Strings;
import com.orion.utils.Xsses;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Xss过滤器
 *
 * @author Li
 * @version 1.0
 * @since 2019/07/29 9:53
 */
public class XssFilter implements Filter {

    /**
     * 应用名称
     */
    private static String applicationContext = "/web";

    /**
     * key: 不需要过滤的Url
     * value: 这个url中不需要过滤的字段, 如果有多个字段, 则用","分开, 如: a,b,c
     */
    private static Map<String, String> xssSkipUrlMap = new HashMap<>();

    static {
        xssSkipUrlMap.put("/text", "url,text");
    }

    /**
     * 过滤器配置
     */
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURI();
        url = url.replace(applicationContext, Strings.EMPTY);
        // 需要配置不需要拦截的url
        if (xssSkipUrlMap.get(url) != null) {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request, xssSkipUrlMap.get(url)), response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    public static void setApplicationContext(String applicationContext) {
        XssFilter.applicationContext = applicationContext;
    }

    /**
     * xss请求过滤包装
     */
    static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        /**
         * 存放url中不需要过滤的字段名
         */
        private Map<String, Null> fieldMap = new HashMap<>();

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String field) {
            super(servletRequest);
            String[] fieldArr = field.split(",");
            for (String f : fieldArr) {
                fieldMap.put(f.trim(), Null.VALUE);
            }
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);
            if (values == null) {
                return null;
            }
            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                // 如果fieldMap中不存在某个字段, 则过滤这个字段
                if (fieldMap.get(parameter) == null) {
                    encodedValues[i] = Xsses.clean(values[i]);
                } else {
                    encodedValues[i] = values[i];
                }
            }
            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            if (value == null) {
                return null;
            }
            // 如果fieldMap中不存在, 则过滤
            if (fieldMap.get(parameter) == null) {
                return Xsses.clean(value);
            }
            return value;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null) {
                return null;
            }
            return Xsses.clean(value);
        }
    }

}
