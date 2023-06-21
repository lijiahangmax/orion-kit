package com.orion.web.servlet.wrapper;

import com.orion.lang.utils.Exceptions;
import com.orion.web.servlet.web.Servlets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * body 可重复读包装器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/21 10:05
 */
public class CacheServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存的内容
     */
    private final byte[] body;

    public CacheServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.body = this.readBody(request);
    }

    private byte[] readBody(HttpServletRequest request) {
        try {
            return Servlets.getBody(request);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int available() {
                return body.length;
            }
        };
    }

}
