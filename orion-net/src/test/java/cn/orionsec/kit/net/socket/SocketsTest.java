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
import java.net.ServerSocket;

import static org.junit.Assert.*;

/**
 * Sockets 工具类测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SocketsTest {

    @Test(timeout = 10000)
    public void testCreateWithAvailablePort() throws IOException {
        int freePort;
        try (ServerSocket probe = new ServerSocket(0)) {
            freePort = probe.getLocalPort();
        }
        ServerSocket socket = Sockets.create(new int[]{freePort});
        assertNotNull(socket);
        assertEquals(freePort, socket.getLocalPort());
        socket.close();
    }

    @Test(timeout = 10000)
    public void testCreateWithOccupiedPort() throws IOException {
        try (ServerSocket occupied = new ServerSocket(0)) {
            int port = occupied.getLocalPort();
            // 端口被占用 返回 null
            assertNull(Sockets.create(new int[]{port}));
        }
    }

    @Test(timeout = 10000)
    public void testCreateFallbackToNextPort() throws IOException {
        int freePort;
        try (ServerSocket probe = new ServerSocket(0)) {
            freePort = probe.getLocalPort();
        }
        try (ServerSocket occupied = new ServerSocket(0)) {
            int occupiedPort = occupied.getLocalPort();
            // 第一个端口被占用 使用第二个端口
            ServerSocket socket = Sockets.create(new int[]{occupiedPort, freePort});
            assertNotNull(socket);
            assertEquals(freePort, socket.getLocalPort());
            socket.close();
        }
    }

    @Test(timeout = 10000)
    public void testCreateWithRange() throws IOException {
        ServerSocket socket = Sockets.create(10001, 65535);
        assertNotNull(socket);
        assertTrue(socket.getLocalPort() >= 10001);
        assertTrue(socket.getLocalPort() <= 65535);
        socket.close();
    }

    @Test(timeout = 10000)
    public void testCreateDefault() throws IOException {
        ServerSocket socket = Sockets.create();
        assertNotNull(socket);
        assertTrue(socket.getLocalPort() >= 5001);
        assertTrue(socket.getLocalPort() <= 65535);
        socket.close();
    }

    @Test
    public void testCreateWithInvalidRange() {
        // start 必须 > 1000
        try {
            Sockets.create(1000, 2000);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // end 必须 > 1000
        try {
            Sockets.create(2000, 1000);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
        // end 必须 <= 65535
        try {
            Sockets.create(2000, 65536);
            fail("expect exception");
        } catch (RuntimeException e) {
            // ignore
        }
    }

}
