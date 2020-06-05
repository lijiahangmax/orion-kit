package com.orion.socket;

import com.orion.utils.Streams;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Udp Client 发送
 *
 * @author ljh15
 * @version 1.0.0
 * @date 2020/6/5 16:15
 */
public class UdpSend {

    /**
     * IP
     */
    private String host;

    /**
     * 端口
     */
    private int port;

    private InetAddress inetAddress;

    private DatagramSocket ds;

    public UdpSend(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.ds = new DatagramSocket();
        this.inetAddress = InetAddress.getByName(host);
        this.ds.setSendBufferSize(4 * 1024);
        this.ds.setSoTimeout(10 * 1000);
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

    public UdpSend send(byte[] bs, int off, int len) throws IOException {
        DatagramPacket dp = new DatagramPacket(bs, off, len, this.inetAddress, this.port);
        this.ds.send(dp);
        return this;
    }

    public void close() {
        Streams.closeQuietly(ds);
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
