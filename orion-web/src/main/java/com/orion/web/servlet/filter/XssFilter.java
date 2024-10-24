/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package com.orion.web.servlet.filter;

import com.orion.lang.utils.Strings;
import com.orion.web.servlet.wrapper.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * xss 过滤器
 *
 * @author Jiahang Li
 * @version 1.0
 * @since 2019/07/29 9:53
 */
public class XssFilter implements Filter {

    /**
     * 应用名称
     */
    private String applicationContext;

    /**
     * key: 需要过滤的 url
     * value: url 中不需要过滤的字段, 如果有多个字段则用","分开, 如: a,b,c
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

}
