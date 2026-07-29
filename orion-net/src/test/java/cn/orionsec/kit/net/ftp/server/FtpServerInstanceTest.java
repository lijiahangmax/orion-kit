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
package cn.orionsec.kit.net.ftp.server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * FtpServerInstance 测试 (内嵌 FTP 服务器)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpServerInstanceTest {

    @Rule
    public TemporaryFolder home = new TemporaryFolder();

    /**
     * 获取空闲端口
     */
    private static int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    @Test(timeout = 30000)
    public void testChainSetter() {
        FtpServerInstance instance = new FtpServerInstance("127.0.0.1", 21)
                .host("127.0.0.1")
                .port(2121)
                .idleTimeout(120);
        assertEquals("127.0.0.1", instance.getHost());
        assertEquals(2121, instance.getPort());
        assertEquals(120, instance.getIdleTimeout());

        FtpServerConfig serverConfig = new FtpServerConfig();
        instance.serverConfig(serverConfig);
        assertSame(serverConfig, instance.getServerConfig());
    }

    @Test(timeout = 30000)
    public void testDefaultConstructor() {
        FtpServerInstance instance = FtpServerInstance.newInstance();
        assertEquals("127.0.0.1", instance.getHost());
        assertEquals(21, instance.getPort());
        assertEquals(300, instance.getIdleTimeout());
        assertNotNull(instance.getServerConfig());
        // 未监听时工厂为空
        assertNull(instance.getServerFactory());
        assertNull(instance.getFactory());
        assertNull(instance.getUserManager());
        assertNull(instance.getFtpServer());

        assertEquals(2222, FtpServerInstance.newInstance(2222).getPort());
    }

    @Test(timeout = 30000)
    public void testServerLifecycle() throws IOException {
        int port = findFreePort();
        FtpServerInstance instance = new FtpServerInstance(port);
        // 监听前添加用户
        FtpUser user = new FtpUser("orion", "123456", home.getRoot().getAbsolutePath());
        user.setWritePermission(true);
        instance.addUser(user);
        instance.listener().start();
        try {
            assertNotNull(instance.getServerFactory());
            assertNotNull(instance.getFactory());
            assertNotNull(instance.getUserManager());
            assertNotNull(instance.getFtpServer());
            assertFalse(instance.isStopped());
            assertFalse(instance.isSuspended());

            // 暂停 / 恢复
            instance.suspend();
            assertTrue(instance.isSuspended());
            instance.resume();
            assertFalse(instance.isSuspended());
        } finally {
            instance.stop();
        }
        assertTrue(instance.isStopped());
    }

    @Test(timeout = 30000)
    public void testUserManage() throws IOException {
        int port = findFreePort();
        FtpServerInstance instance = new FtpServerInstance(port);
        // 监听前添加
        instance.addUser(new FtpUser("u1", "123456", home.getRoot().getAbsolutePath()));
        instance.addUsers(Collections.singletonList(new FtpUser("u2", "123456", home.getRoot().getAbsolutePath())));
        instance.addUser("u3", "123456");
        instance.listener().start();
        try {
            assertTrue(instance.userExists("u1"));
            assertTrue(instance.userExists("u2"));
            assertTrue(instance.userExists("u3"));
            assertFalse(instance.userExists("nobody"));

            // 监听后添加
            instance.addUser("u4", "123456", home.getRoot().getAbsolutePath());
            instance.addUser(new FtpUser("u5", "123456", home.getRoot().getAbsolutePath()));
            instance.addUsers(Collections.singletonList(new FtpUser("u6", "123456", home.getRoot().getAbsolutePath())));
            List<String> userNames = instance.getUserNames();
            assertTrue(userNames.containsAll(Arrays.asList("u1", "u2", "u3", "u4", "u5", "u6")));

            // 删除用户
            instance.deleteUser("u1");
            instance.deleteUsers(Arrays.asList("u2", "u3"));
            assertFalse(instance.userExists("u1"));
            assertFalse(instance.userExists("u2"));
            assertFalse(instance.userExists("u3"));
            assertTrue(instance.userExists("u4"));
        } finally {
            instance.stop();
        }
    }

}
