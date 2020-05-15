package com.orion.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author Li
 * @version 1.0.0
 * @date 2019/07/23 20:04
 */
public class CrossFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,login_token,X-PINGOTHER,Content-Type, Accept, Origin, Last-Modified");
        httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
        chain.doFilter(request, httpServletResponse);
    }

    @Override
    public void destroy() {
    }

}
