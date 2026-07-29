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

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * UdpSend + UdpReceive 配对测试 (仅 localhost)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class UdpSocketTest {

    private static final String LOCALHOST = "127.0.0.1";

    /**
     * 获取一个可用 UDP 端口
     */
    private static int findFreeUdpPort() throws SocketException {
        try (DatagramSocket probe = new DatagramSocket(0)) {
            return probe.getLocalPort();
        }
    }

    @Test(timeout = 10000)
    public void testSendAndReceive() throws IOException {
        int port = findFreeUdpPort();
        try (UdpReceive receive = new UdpReceive(port);
             UdpSend send = new UdpSend(LOCALHOST, port)) {
            receive.bufferSize(4096).timeout(8000);
            send.bufferSize(4096).timeout(8000);
            // send(byte[]) + receive(byte[])
            byte[] data = "hello udp".getBytes();
            send.send(data);
            byte[] buf = new byte[64];
            int len = receive.receive(buf);
            assertEquals(data.length, len);
            assertArrayEquals(data, Arrays.copyOfRange(buf, 0, len));
            // send(byte[], len) + receive(byte[], len)
            send.send(data, 5);
            byte[] buf2 = new byte[32];
            int len2 = receive.receive(buf2, 32);
            assertEquals(5, len2);
            assertArrayEquals("hello".getBytes(), Arrays.copyOfRange(buf2, 0, len2));
            // send(byte[], off, len) + receive(byte[], off, len)
            send.send(data, 6, 3);
            byte[] buf3 = new byte[32];
            int len3 = receive.receive(buf3, 2, 30);
            assertEquals(3, len3);
            assertArrayEquals("udp".getBytes(), Arrays.copyOfRange(buf3, 2, 2 + len3));
        }
    }

    @Test(timeout = 10000)
    public void testReceivePacket() throws IOException {
        int port = findFreeUdpPort();
        try (UdpReceive receive = new UdpReceive(port);
             UdpSend send = new UdpSend(LOCALHOST, port)) {
            receive.timeout(8000);
            send.timeout(8000);
            // receivePacket(byte[])
            byte[] data = "packet".getBytes();
            send.send(data);
            byte[] buf = new byte[64];
            DatagramPacket packet = receive.receivePacket(buf);
            assertEquals(data.length, packet.getLength());
            assertArrayEquals(data, Arrays.copyOfRange(packet.getData(), packet.getOffset(), packet.getOffset() + packet.getLength()));
            assertTrue(packet.getAddress().isLoopbackAddress());
            // receivePacket(byte[], len)
            send.send(data, 3);
            byte[] buf2 = new byte[32];
            DatagramPacket packet2 = receive.receivePacket(buf2, 32);
            assertEquals(3, packet2.getLength());
            // receivePacket(byte[], off, len)
            send.send(data, 0, 6);
            byte[] buf3 = new byte[32];
            DatagramPacket packet3 = receive.receivePacket(buf3, 4, 28);
            assertEquals(6, packet3.getLength());
            assertEquals(4, packet3.getOffset());
        }
    }

    @Test(timeout = 10000)
    public void testSendLf() throws IOException {
        int port = findFreeUdpPort();
        try (UdpReceive receive = new UdpReceive(port);
             UdpSend send = new UdpSend(LOCALHOST, port)) {
            receive.timeout(8000);
            send.timeout(8000);
            send.sendLf();
            byte[] buf = new byte[8];
            int len = receive.receive(buf);
            assertEquals(1, len);
            assertEquals('\n', buf[0]);
        }
    }

    @Test(timeout = 10000)
    public void testGetters() throws IOException {
        int port = findFreeUdpPort();
        try (UdpReceive receive = new UdpReceive(port);
             UdpSend send = new UdpSend(LOCALHOST, port)) {
            // receive getters
            assertEquals(port, receive.getPort());
            assertNotNull(receive.getDatagramSocket());
            // send getters
            assertEquals(LOCALHOST, send.getHost());
            assertEquals(port, send.getPort());
            assertNotNull(send.getInetAddress());
            assertEquals(LOCALHOST, send.getInetAddress().getHostAddress());
            assertNotNull(send.getDatagramSocket());
        }
    }

}
