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
package cn.orionsec.kit.net.host;

import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import com.jcraft.jsch.JSch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SessionStore 单元测试
 * <p>
 * 仅测试配置链与参数校验 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SessionStoreTest {

    private SessionStore store;

    @Before
    public void setup() throws Exception {
        // 仅创建 session 不连接
        this.store = new SessionStore(new JSch().getSession("orion", "127.0.0.1", 2222));
    }

    @Test
    public void testHostPortUsername() {
        Assert.assertEquals("127.0.0.1", store.getHost());
        Assert.assertEquals(2222, store.getPort());
        Assert.assertEquals("orion", store.getUsername());
    }

    @Test
    public void testSetHostPort() {
        store.setHost("192.168.1.1");
        store.setPort(22);
        Assert.assertEquals("192.168.1.1", store.getHost());
        Assert.assertEquals(22, store.getPort());
    }

    @Test
    public void testPasswordChain() {
        Assert.assertSame(store, store.password("password"));
        Assert.assertSame(store, store.password("password".getBytes()));
    }

    @Test
    public void testConfig() {
        Assert.assertSame(store, store.config("PreferredAuthentications", "password"));
        Assert.assertEquals("password", store.getConfig("PreferredAuthentications"));
        store.setConfig("PreferredAuthentications", "publickey");
        Assert.assertEquals("publickey", store.getConfig("PreferredAuthentications"));
    }

    @Test
    public void testTimeout() {
        Assert.assertSame(store, store.timeout(3000));
        Assert.assertSame(store, store.timeout(0));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testTimeoutNegative() {
        store.timeout(-1);
    }

    @Test
    public void testHttpProxy() {
        Assert.assertSame(store, store.httpProxy("127.0.0.1", 8080));
        Assert.assertSame(store, store.httpProxy("127.0.0.1", 8080, "user", "pass"));
    }

    @Test
    public void testSocks4Proxy() {
        Assert.assertSame(store, store.socks4Proxy("127.0.0.1", 1080));
        Assert.assertSame(store, store.socks4Proxy("127.0.0.1", 1080, "user", "pass"));
    }

    @Test
    public void testSocks5Proxy() {
        Assert.assertSame(store, store.socks5Proxy("127.0.0.1", 1080));
        Assert.assertSame(store, store.socks5Proxy("127.0.0.1", 1080, "user", "pass"));
    }

    @Test
    public void testProxyByType() {
        Assert.assertSame(store, store.proxy(SessionProxyType.HTTP, "127.0.0.1", 8080));
        Assert.assertSame(store, store.proxy(SessionProxyType.SOCKS4, "127.0.0.1", 1080, "user", "pass"));
        Assert.assertSame(store, store.proxy(SessionProxyType.SOCKS5, "127.0.0.1", 1080, null, null));
        // 类型为 null 不设置代理
        Assert.assertSame(store, store.proxy(null, "127.0.0.1", 8080, null, null));
    }

    @Test
    public void testClientVersion() {
        Assert.assertSame(store, store.clientVersion("SSH-2.0-ORION_TEST"));
    }

    @Test
    public void testDaemonThread() {
        Assert.assertSame(store, store.daemonThread(true));
        Assert.assertSame(store, store.daemonThread(false));
    }

    @Test
    public void testIsConnected() {
        Assert.assertFalse(store.isConnected());
    }

    @Test
    public void testGetSession() {
        Assert.assertNotNull(store.getSession());
    }

    @Test
    public void testCloseWithoutConnect() {
        // 未连接时 close 不抛出异常
        store.close();
        Assert.assertFalse(store.isConnected());
    }

    @Test
    public void testGetCommandExecutorSessionDown() {
        // 未连接时打开 channel 抛出异常 不会发起网络请求
        try {
            store.getCommandExecutor("echo 1");
            Assert.fail("should throw exception");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("could not open channel"));
        }
    }

    @Test
    public void testGetShellExecutorSessionDown() {
        try {
            store.getShellExecutor();
            Assert.fail("should throw exception");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("could not open channel"));
        }
    }

    @Test
    public void testGetSftpExecutorSessionDown() {
        try {
            store.getSftpExecutor();
            Assert.fail("should throw exception");
        } catch (IllegalStateException e) {
            Assert.assertTrue(e.getMessage().contains("could not open channel"));
        }
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试中建立连接")
    public void testConnect() {
        store.password("password").connect();
    }

}
