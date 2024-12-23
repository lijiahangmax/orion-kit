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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * UDP Server 接收
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/6/5 16:35
 */
public class UdpReceive implements AutoCloseable {

    /**
     * 端口
     */
    private final int port;

    private final DatagramSocket ds;

    public UdpReceive(int port) throws IOException {
        this.port = port;
        this.ds = new DatagramSocket(port);
        this.ds.setSendBufferSize(Const.BUFFER_KB_4);
        this.ds.setSoTimeout(Const.MS_S_10);
    }

    public UdpReceive bufferSize(int bufferSize) throws SocketException {
        this.ds.setSendBufferSize(bufferSize);
        return this;
    }

    public UdpReceive timeout(int timeOut) throws SocketException {
        this.ds.setSoTimeout(timeOut);
        return this;
    }

    public int receive(byte[] bs) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        ds.receive(packet);
        return packet.getLength();
    }

    public int receive(byte[] bs, int len) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, len);
        ds.receive(packet);
        return packet.getLength();
    }

    public int receive(byte[] bs, int off, int len) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, off, len);
        ds.receive(packet);
        return packet.getLength();
    }

    public DatagramPacket receivePacket(byte[] bs) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, bs.length);
        ds.receive(packet);
        return packet;
    }

    public DatagramPacket receivePacket(byte[] bs, int len) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, len);
        ds.receive(packet);
        return packet;
    }

    public DatagramPacket receivePacket(byte[] bs, int off, int len) throws IOException {
        DatagramPacket packet = new DatagramPacket(bs, off, len);
        ds.receive(packet);
        return packet;
    }

    @Override
    public void close() {
        Streams.close(ds);
    }

    public int getPort() {
        return port;
    }

    public DatagramSocket getDatagramSocket() {
        return ds;
    }

}
