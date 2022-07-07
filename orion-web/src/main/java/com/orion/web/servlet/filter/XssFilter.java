package com.orion.web.servlet.filter;

import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Xsses;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;

/**
 * xss 过滤器
 *
 * @author Li
 * @version 1.0
 * @since 2019/07/29 9:53
 */
public class XssFilter implements Filter {

    /**
     * 应用名称
     */
    private String applicationContext = "/";

    /**
     * key: 不需要过滤的 url
     * value: 这个url中不需要过滤的字段, 如果有多个字段, 则用","分开, 如: a,b,c
     */
    private final Map<String, String> ignoreFields;

    public XssFilter(String applicationContext) {
        this.applicationContext = applicationContext;
        this.ignoreFields = new HashMap<>();
    }

    /**
     * 设置应用上下文
     *
     * @param applicationContext 应用上下文
     */
    public void setApplicationContext(String applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 添加忽略的字段
     *
     * @param url   url
     * @param field field
     */
    public void addIgnoreField(String url, String field) {
        ignoreFields.put(url, field);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = ((HttpServletRequest) request).getRequestURI();
        url = url.replace(applicationContext, Strings.EMPTY);
        // 需要配置不需要拦截的url
        if (ignoreFields.get(url) != null) {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request, ignoreFields.get(url)), response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * xss请求过滤包装
     */
    static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        /**
         * 存放 url 中不需要过滤的字段名
         */
        private final Set<String> currentIgnoreField;

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
            this.currentIgnoreField = Collections.emptySet();
        }

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String field) {
            super(servletRequest);
            this.currentIgnoreField = new HashSet<>();
            String[] fieldArr = field.split(",");
            for (String f : fieldArr) {
                currentIgnoreField.add(f.trim());
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
                if (currentIgnoreField.contains(parameter)) {
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
            if (currentIgnoreField.contains(parameter)) {
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
