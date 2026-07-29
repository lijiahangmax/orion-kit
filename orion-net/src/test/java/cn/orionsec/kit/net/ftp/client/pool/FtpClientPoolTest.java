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
package cn.orionsec.kit.net.ftp.client.pool;

import cn.orionsec.kit.net.ftp.client.Ftps;
import cn.orionsec.kit.net.ftp.client.config.FtpConfig;
import cn.orionsec.kit.net.ftp.client.instance.IFtpInstance;
import cn.orionsec.kit.net.ftp.server.FtpServerInstance;
import cn.orionsec.kit.net.ftp.server.FtpUser;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

/**
 * FtpClientPool 集成测试 (内嵌 FTP 服务器)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpClientPoolTest {

    private static final String USERNAME = "orion";

    private static final String PASSWORD = "123456";

    @ClassRule
    public static final TemporaryFolder HOME = new TemporaryFolder();

    private static int port;

    private static FtpServerInstance server;

    private static FtpConfig config;

    @BeforeClass
    public static void startServer() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
        }
        FtpUser user = new FtpUser(USERNAME, PASSWORD, HOME.getRoot().getAbsolutePath());
        user.setWritePermission(true);
        server = new FtpServerInstance(port)
                .addUser(user)
                .listener()
                .start();
        config = new FtpConfig("127.0.0.1", port).auth(USERNAME, PASSWORD);
    }

    @AfterClass
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @Test(timeout = 30000)
    public void testFactoryNullConfig() {
        assertThrows(RuntimeException.class, () -> new FtpClientFactory(null));
    }

    @Test(timeout = 30000)
    public void testFactoryCreateClient() {
        FtpClientFactory factory = new FtpClientFactory(config);
        assertSame(config, factory.getConfig());

        FTPClient client = factory.createClient();
        assertTrue(Ftps.isActive(client));
        Ftps.destroy(client);
        assertFalse(Ftps.isActive(client));

        // null 安全
        assertFalse(Ftps.isActive(null));
        Ftps.destroy(null);
    }

    @Test(timeout = 30000)
    public void testPoolBasic() throws InterruptedException {
        FtpClientPool pool = Ftps.createClientPool(config, 2);
        try {
            assertEquals(2, pool.getFreeSize());
            assertNotNull(pool.getFactory());
            assertSame(config, pool.getConfig());
            assertFalse(pool.isNoAvailableThenCreate());

            // 获取连接
            FTPClient client = pool.getClient();
            assertNotNull(client);
            assertEquals(1, pool.getFreeSize());
            assertTrue(Ftps.isActive(client));

            // 归还连接
            pool.returnClient(client);
            assertEquals(2, pool.getFreeSize());
        } finally {
            pool.close();
        }
        assertEquals(0, pool.getFreeSize());
    }

    @Test(timeout = 30000)
    public void testPoolInstance() throws InterruptedException, IOException {
        FtpClientPool pool = Ftps.createClientPool(config, 1);
        try {
            IFtpInstance instance = pool.getInstance();
            assertEquals(0, pool.getFreeSize());
            assertNotNull(instance.getPool());

            // 使用实例操作
            instance.write("/pool/p.txt", "pool");
            assertEquals(4L, instance.getSize("/pool/p.txt"));

            // 关闭实例会归还连接
            instance.close();
            assertEquals(1, pool.getFreeSize());
        } finally {
            pool.close();
        }
    }

    @Test(timeout = 30000)
    public void testNoAvailableThenCreate() throws InterruptedException {
        FtpClientPool pool = Ftps.createClientPool(config, 1)
                .timeout(1000)
                .noAvailableThenCreate(true);
        try {
            assertTrue(pool.isNoAvailableThenCreate());
            FTPClient c1 = pool.getClient();
            // 池中无连接时创建临时连接
            FTPClient c2 = pool.getClient();
            assertNotSame(c1, c2);
            assertTrue(Ftps.isActive(c2));

            pool.returnClient(c1);
            // 池已满 归还超时后销毁
            pool.returnClient(c2);
            assertEquals(1, pool.getFreeSize());
        } finally {
            pool.close();
        }
    }

    @Test(timeout = 30000)
    public void testGetClientTimeout() throws InterruptedException {
        FtpClientPool pool = Ftps.createClientPool(config, 1).timeout(500);
        try {
            FTPClient client = pool.getClient();
            // 池中无空闲连接 超时报错
            assertThrows(RuntimeException.class, pool::getClient);
            pool.returnClient(client);
        } finally {
            pool.close();
        }
    }

}
