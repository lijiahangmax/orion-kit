package com.orion.net.socket;

import com.orion.utils.Exceptions;
import com.orion.utils.Threads;
import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TCP Server 接收
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/6/5 16:35
 */
public class TcpReceive {

    private int port;

    private ServerSocket serverSocket;

    private List<Socket> receiveSocketList = new ArrayList<>();

    private ExecutorService acceptThreadPool;

    public TcpReceive(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReceiveBufferSize(4 * 1024);
        this.serverSocket.setSoTimeout(10 * 1000);
        this.acceptThreadPool = Executors.newSingleThreadExecutor();
    }

    public TcpReceive bufferSize(int bufferSize) throws SocketException {
        this.serverSocket.setReceiveBufferSize(bufferSize);
        return this;
    }

    public TcpReceive timeout(int timeOut) throws SocketException {
        this.serverSocket.setSoTimeout(timeOut);
        return this;
    }

    public TcpReceive accept(int count) {
        acceptThreadPool.execute(() -> {
            for (int i = 0; i < count; i++) {
                try {
                    receiveSocketList.add(serverSocket.accept());
                } catch (IOException e) {
                    throw Exceptions.ioRuntime(e);
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

    public TcpReceive sendAll(byte[] bs, int off, int len) throws IOException {
        for (Socket socket : receiveSocketList) {
            if (socket.isConnected() && !socket.isOutputShutdown()) {
                OutputStream o = socket.getOutputStream();
                o.write(bs, off, len);
                o.flush();
            }
        }
        return this;
    }

    public void close() {
        Threads.shutdownPool(acceptThreadPool, 6, TimeUnit.SECONDS);
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
