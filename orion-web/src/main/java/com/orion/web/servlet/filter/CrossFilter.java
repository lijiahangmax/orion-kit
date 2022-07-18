package com.orion.web.servlet.filter;

import com.orion.lang.constant.StandardHttpHeader;

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
