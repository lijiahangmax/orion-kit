package com.orion.socket;

import com.orion.constant.Const;
import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

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
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/5 16:35
 */
public class TcpReceive implements AutoCloseable {

    private int port;

    private ServerSocket serverSocket;

    /**
     * 接收的socket
     */
    private List<Socket> receiveSocketList;

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
                    Exceptions.printStacks(e);
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

    public TcpReceive sendAll(byte[] bs) throws IOException {
        return sendAll(bs, 0, bs.length);
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
