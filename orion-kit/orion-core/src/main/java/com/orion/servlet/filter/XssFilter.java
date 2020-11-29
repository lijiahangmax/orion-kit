package com.orion.servlet.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String APPLICATION_CONTEXT = "/web";

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
        url = url.replace(APPLICATION_CONTEXT, "");
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

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * xss请求过滤包装
     */
    static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        /**
         * 存放url中不需要过滤的字段名
         */
        private Map<String, String> filedMap = new HashMap<>();

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        XssHttpServletRequestWrapper(HttpServletRequest servletRequest, String filed) {
            super(servletRequest);
            String[] filedArr = filed.split(",");
            for (String aFiledArr : filedArr) {
                filedMap.put(aFiledArr.trim(), "true");
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
                // 如果filedMap中不存在某个字段, 则过滤这个字段
                if (filedMap.get(parameter) == null) {
                    encodedValues[i] = cleanXSS(values[i]);
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
            // 如果filedMap中不存在某个字段, 则过滤这个字段
            // 如果存在, 则不过滤这个字段
            if (filedMap.get(parameter) == null) {
                return cleanXSS(value);
            }
            return value;
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            if (value == null) {
                return null;
            }
            return cleanXSS(value);
        }

        /**
         * 去除html代码
         *
         * @param htmlStr ignore
         * @return ignore
         */
        private String cleanXSS(String htmlStr) {
            String textStr = "";
            try {
                String regScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
                // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
                String regStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
                // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
                String regHtml = "<[^>]+>";
                // 定义HTML标签的正则表达式
                String patternStr = "\\s+";

                Pattern pScript = Pattern.compile(regScript, Pattern.CASE_INSENSITIVE);
                Matcher mScript = pScript.matcher(htmlStr);
                // 过滤script标签
                htmlStr = mScript.replaceAll("");
                Pattern pStyle = Pattern.compile(regStyle, Pattern.CASE_INSENSITIVE);
                Matcher mStyle = pStyle.matcher(htmlStr);
                // 过滤style标签
                htmlStr = mStyle.replaceAll("");
                Pattern pHtml = Pattern.compile(regHtml, Pattern.CASE_INSENSITIVE);
                Matcher mHtml = pHtml.matcher(htmlStr);
                // 过滤html标签
                htmlStr = mHtml.replaceAll("");
                // Pattern pBa = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
                // Matcher mBa = pBa.matcher(htmlStr);
                // 过滤空格
                // htmlStr = mBa.replaceAll("");
                textStr = htmlStr;
            } catch (Exception e) {
                // ignore
            }
            return textStr;
        }
    }

}
