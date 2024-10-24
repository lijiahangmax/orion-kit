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
package com.orion.net.socket;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Threads;
import com.orion.lang.utils.io.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TCP Server 接收
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/5 16:35
 */
public class TcpReceive implements AutoCloseable {

    /**
     * LOG
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpReceive.class);

    private final int port;

    private final ServerSocket serverSocket;

    /**
     * 接收的socket
     */
    private final List<Socket> receiveSocketList;

    /**
     * 接收线程池
     */
    private ExecutorService acceptThreadPool;

    public TcpReceive(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReceiveBufferSize(Const.BUFFER_KB_4);
        this.receiveSocketList = new ArrayList<>();
        this.acceptThreadPool = Threads.CACHE_EXECUTOR;
    }

    public TcpReceive bufferSize(int bufferSize) throws SocketException {
        this.serverSocket.setReceiveBufferSize(bufferSize);
        return this;
    }

    public TcpReceive timeout(int timeOut) throws SocketException {
        this.serverSocket.setSoTimeout(timeOut);
        return this;
    }

    public TcpReceive acceptThreadPool(ExecutorService acceptThreadPool) {
        this.acceptThreadPool = acceptThreadPool;
        return this;
    }

    /**
     * 接收几个连接
     *
     * @param count count
     * @return this
     */
    public TcpReceive accept(int count) {
        acceptThreadPool.execute(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    receiveSocketList.add(serverSocket.accept());
                } catch (IOException e) {
                    LOGGER.error("TcpReceive.accept error", e);
                }
            }
        });
        return this;
    }

    public TcpReceive sendAll(byte b) throws IOException {
        for (Socket socket : receiveSocketList) {
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                OutputStream o = socket.getOutputStream();
                o.write(b);
                o.flush();
            }
        }
        return this;
    }

    /**
     * 批量发送数据
     *
     * @param bs bs
     * @return this
     * @throws IOException IOException
     */
    public TcpReceive sendAll(byte[] bs) throws IOException {
        return this.sendAll(bs, 0, bs.length);
    }

    /**
     * 批量发送数据
     *
     * @param bs  bytes
     * @param off offset
     * @param len length
     * @return this
     * @throws IOException IOException
     */
    public TcpReceive sendAll(byte[] bs, int off, int len) throws IOException {
        Iterator<Socket> iterator = receiveSocketList.iterator();
        while (iterator.hasNext()) {
            Socket socket = iterator.next();
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                OutputStream o = socket.getOutputStream();
                o.write(bs, off, len);
                o.flush();
            } else {
                iterator.remove();
            }
        }
        return this;
    }

    /**
     * 寻找客户端 socket
     *
     * @param host host
     * @param port port
     * @return socket
     */
    public Socket findSocket(String host, int port) {
        Iterator<Socket> iterator = receiveSocketList.iterator();
        while (iterator.hasNext()) {
            Socket socket = iterator.next();
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                if (socket.getInetAddress().getCanonicalHostName().equals(host) && socket.getPort() == port) {
                    return socket;
                }
            } else {
                iterator.remove();
            }
        }
        return null;
    }

    /**
     * 寻找客户端 socket
     *
     * @param host host
     * @return socket
     */
    public List<Socket> findSocket(String host) {
        List<Socket> list = new ArrayList<>();
        Iterator<Socket> iterator = receiveSocketList.iterator();
        while (iterator.hasNext()) {
            Socket socket = iterator.next();
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                if (socket.getInetAddress().getCanonicalHostName().equals(host)) {
                    list.add(socket);
                }
            } else {
                iterator.remove();
            }
        }
        return list;
    }

    /**
     * 关闭线程池
     *
     * @return this
     */
    public TcpReceive closePool() {
        Threads.shutdownPoolNow(acceptThreadPool, Const.MS_S_5, TimeUnit.MILLISECONDS);
        return this;
    }

    @Override
    public void close() {
        receiveSocketList.forEach(Streams::close);
        Streams.close(serverSocket);
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<Socket> getReceiveSocketList() {
        return receiveSocketList;
    }

}
