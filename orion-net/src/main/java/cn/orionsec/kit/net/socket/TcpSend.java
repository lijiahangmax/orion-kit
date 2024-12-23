/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package cn.orionsec.kit.net.socket;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.utils.io.Streams;

import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * TCP Client 发送
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/5 15:41
 */
public class TcpSend implements AutoCloseable, Flushable {

    /**
     * host
     */
    private final String host;

    /**
     * 端口
     */
    private final int port;

    private final Socket socket;

    private final InputStream in;

    private final OutputStream out;

    public TcpSend(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
        this.socket.setSendBufferSize(Const.BUFFER_KB_4);
        this.socket.setTcpNoDelay(true);
        this.socket.setKeepAlive(true);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    public TcpSend bufferSize(int bufferSize) throws SocketException {
        this.socket.setSendBufferSize(bufferSize);
        return this;
    }

    public TcpSend timeout(int timeOut) throws SocketException {
        this.socket.setSoTimeout(timeOut);
        return this;
    }

    public TcpSend send(byte send) throws IOException {
        out.write(send);
        return this;
    }

    public TcpSend send(byte[] send) throws IOException {
        out.write(send);
        return this;
    }

    /**
     * 发送数据
     *
     * @param send bytes
     * @param off  offset
     * @param len  length
     * @return this
     * @throws IOException IOException
     */
    public TcpSend send(byte[] send, int off, int len) throws IOException {
        out.write(send, off, len);
        return this;
    }

    /**
     * 发送 \n
     *
     * @return this
     * @throws IOException IOException
     */
    public TcpSend sendLf() throws IOException {
        out.write(13);
        return this;
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    public int read(byte[] bs) throws IOException {
        return read(bs, 0, bs.length);
    }

    /**
     * 读取数据
     *
     * @param bs  bytes
     * @param off offset
     * @param len length
     * @return read length
     * @throws IOException IOException
     */
    public int read(byte[] bs, int off, int len) throws IOException {
        int available = this.in.available();
        if (available <= 0) {
            return 0;
        } else {
            return in.read(bs, off, len);
        }
    }

    @Override
    public void close() {
        Streams.close(socket);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInput() {
        return in;
    }

    public OutputStream getOutput() {
        return out;
    }

}
