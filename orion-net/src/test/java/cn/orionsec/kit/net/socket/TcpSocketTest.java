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
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * TcpSend + TcpReceive 配对测试 (仅 localhost)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class TcpSocketTest {

    private static final String LOCALHOST = "127.0.0.1";

    /**
     * 获取一个可用端口
     */
    private static int findFreePort() throws IOException {
        try (ServerSocket probe = new ServerSocket(0)) {
            return probe.getLocalPort();
        }
    }

    /**
     * 等待服务端接收到连接
     */
    private static Socket waitForAccept(TcpReceive receive) throws InterruptedException {
        long deadline = System.currentTimeMillis() + 8000;
        while (System.currentTimeMillis() < deadline) {
            List<Socket> list = receive.getReceiveSocketList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
            Thread.sleep(20);
        }
        fail("wait accept timeout");
        return null;
    }

    @Test(timeout = 10000)
    public void testClientSendToServer() throws IOException, InterruptedException {
        int port = findFreePort();
        try (TcpReceive receive = new TcpReceive(port)) {
            receive.bufferSize(4096).timeout(8000).accept(1);
            try (TcpSend send = new TcpSend(LOCALHOST, port)) {
                send.bufferSize(4096).timeout(8000);
                byte[] data = "hello tcp".getBytes();
                send.send((byte) 1)
                        .send(data)
                        .send(data, 6, 3)
                        .sendLf()
                        .flush();
                Socket accepted = waitForAccept(receive);
                accepted.setSoTimeout(8000);
                InputStream in = accepted.getInputStream();
                int expected = 1 + data.length + 3 + 1;
                byte[] buf = new byte[expected];
                int total = 0;
                while (total < expected) {
                    int read = in.read(buf, total, expected - total);
                    assertTrue(read > 0);
                    total += read;
                }
                assertEquals(1, buf[0]);
                assertArrayEquals(data, Arrays.copyOfRange(buf, 1, 1 + data.length));
                assertArrayEquals("tcp".getBytes(), Arrays.copyOfRange(buf, 1 + data.length, 1 + data.length + 3));
                // sendLf 写入的是 13
                assertEquals(13, buf[expected - 1]);
            }
        }
    }

    @Test(timeout = 10000)
    public void testServerSendAllToClient() throws IOException, InterruptedException {
        int port = findFreePort();
        try (TcpReceive receive = new TcpReceive(port)) {
            receive.timeout(8000).accept(1);
            try (TcpSend send = new TcpSend(LOCALHOST, port)) {
                send.timeout(8000);
                waitForAccept(receive);
                // 无可用数据时 read 返回 0
                assertEquals(0, send.read(new byte[8]));
                byte[] data = "server-data".getBytes();
                receive.sendAll((byte) 9).sendAll(data);
                int expected = 1 + data.length;
                byte[] buf = new byte[64];
                int total = 0;
                long deadline = System.currentTimeMillis() + 8000;
                while (total < expected && System.currentTimeMillis() < deadline) {
                    int read = send.read(buf, total, buf.length - total);
                    if (read > 0) {
                        total += read;
                    } else {
                        Thread.sleep(20);
                    }
                }
                assertEquals(expected, total);
                assertEquals(9, buf[0]);
                assertArrayEquals(data, Arrays.copyOfRange(buf, 1, expected));
            }
        }
    }

    @Test(timeout = 10000)
    public void testFindSocket() throws IOException, InterruptedException {
        int port = findFreePort();
        try (TcpReceive receive = new TcpReceive(port)) {
            receive.timeout(8000).accept(1);
            try (TcpSend send = new TcpSend(LOCALHOST, port)) {
                Socket accepted = waitForAccept(receive);
                String host = accepted.getInetAddress().getCanonicalHostName();
                // 按 host + port 查找
                assertSame(accepted, receive.findSocket(host, accepted.getPort()));
                // 端口不匹配
                assertNull(receive.findSocket(host, 1));
                // 按 host 查找
                List<Socket> list = receive.findSocket(host);
                assertEquals(1, list.size());
                assertSame(accepted, list.get(0));
                assertNotNull(send.getSocket());
            }
        }
    }

    @Test(timeout = 10000)
    public void testGettersAndClosePool() throws IOException, InterruptedException {
        int port = findFreePort();
        ExecutorService pool = Executors.newCachedThreadPool();
        try (TcpReceive receive = new TcpReceive(port)) {
            receive.timeout(8000).acceptThreadPool(pool).accept(1);
            try (TcpSend send = new TcpSend(LOCALHOST, port)) {
                waitForAccept(receive);
                // receive getters
                assertEquals(port, receive.getPort());
                assertNotNull(receive.getServerSocket());
                assertEquals(1, receive.getReceiveSocketList().size());
                // send getters
                assertEquals(LOCALHOST, send.getHost());
                assertEquals(port, send.getPort());
                assertNotNull(send.getSocket());
                assertNotNull(send.getInput());
                assertNotNull(send.getOutput());
                assertTrue(send.getSocket().isConnected());
            }
            receive.closePool();
            assertTrue(pool.isShutdown());
        }
    }

}
