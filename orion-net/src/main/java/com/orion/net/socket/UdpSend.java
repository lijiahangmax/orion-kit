package com.orion.net.socket;

import com.orion.lang.constant.Const;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.io.Streams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * udp client 发送
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/5 16:15
 */
public class UdpSend implements AutoCloseable {

    /**
     * IP
     */
    private final String host;

    /**
     * 端口
     */
    private final int port;

    private final InetAddress inetAddress;

    private final DatagramSocket ds;

    public UdpSend(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.ds = new DatagramSocket();
            this.inetAddress = InetAddress.getByName(host);
            this.ds.setSendBufferSize(Const.BUFFER_KB_4);
            this.ds.setSoTimeout(Const.MS_S_10);
        } catch (IOException e) {
            throw Exceptions.ioRuntime(e);
        }
    }

    public UdpSend bufferSize(int bufferSize) throws SocketException {
        this.ds.setSendBufferSize(bufferSize);
        return this;
    }

    public UdpSend timeout(int timeOut) throws SocketException {
        this.ds.setSoTimeout(timeOut);
        return this;
    }

    public UdpSend send(byte[] bs) throws IOException {
        DatagramPacket dp = new DatagramPacket(bs, bs.length, this.inetAddress, this.port);
        this.ds.send(dp);
        return this;
    }

    public UdpSend send(byte[] bs, int len) throws IOException {
        DatagramPacket dp = new DatagramPacket(bs, len, this.inetAddress, this.port);
        this.ds.send(dp);
        return this;
    }

    /**
     * 发送数据
     *
     * @param bs  bytes
     * @param off offset
     * @param len length
     * @return this
     * @throws IOException IOException
     */
    public UdpSend send(byte[] bs, int off, int len) throws IOException {
        DatagramPacket dp = new DatagramPacket(bs, off, len, this.inetAddress, this.port);
        this.ds.send(dp);
        return this;
    }

    /**
     * 发送 \n
     *
     * @return this
     * @throws IOException IOException
     */
    public UdpSend sendLf() throws IOException {
        DatagramPacket dp = new DatagramPacket(Const.LF.getBytes(), 1, this.inetAddress, this.port);
        this.ds.send(dp);
        return this;
    }

    @Override
    public void close() {
        Streams.close(ds);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public DatagramSocket getDatagramSocket() {
        return ds;
    }

}
