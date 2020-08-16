package com.orion.net.socket;

import com.orion.utils.io.Streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * TCP Client 发送
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/5 15:41
 */
public class TcpSend {

    /**
     * IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    private Socket socket;

    private InputStream in;

    private OutputStream out;

    public TcpSend(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(host, port);
        this.socket.setSendBufferSize(4 * 1024);
        this.socket.setTcpNoDelay(true);
        this.socket.setSoTimeout(10 * 1000);
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

    public TcpSend send(byte[] send, int off, int len) throws IOException {
        out.write(send, off, len);
        return this;
    }

    public TcpSend send(byte[] send) throws IOException {
        out.write(send);
        return this;
    }

    public TcpSend send(byte send) throws IOException {
        out.write(send);
        return this;
    }

    public TcpSend sendLF() throws IOException {
        out.write(13);
        return this;
    }

    public TcpSend flush() throws IOException {
        out.flush();
        return this;
    }

    public byte[] readAvailable() throws IOException {
        int available = this.in.available();
        if (available <= 0) {
            return new byte[0];
        } else {
            byte[] bs = new byte[available];
            in.read(bs);
            return bs;
        }
    }

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
