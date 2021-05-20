package com.orion.socket;

import com.orion.constant.Const;
import com.orion.utils.io.Streams;

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
    public TcpSend sendLF() throws IOException {
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
