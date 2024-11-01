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
package cn.orionsec.kit.web.servlet.filter;

import cn.orionsec.kit.lang.constant.StandardHttpHeader;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2019/07/23 20:04
 */
public class CrossFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader(StandardHttpHeader.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setHeader(StandardHttpHeader.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        httpServletResponse.setHeader(StandardHttpHeader.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpServletResponse.setHeader(StandardHttpHeader.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, X-Requested-With, X-PINGOTHER, Accept, Origin, Last-Modified");
        httpServletResponse.setHeader(StandardHttpHeader.ACCESS_CONTROL_MAX_AGE, "86400");
        chain.doFilter(request, httpServletResponse);
    }

    @Override
    public void destroy() {
    }

}
