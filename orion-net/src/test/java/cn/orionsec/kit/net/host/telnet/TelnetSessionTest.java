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
package cn.orionsec.kit.net.host.telnet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TelnetSession 单元测试
 * <p>
 * 不连接任何真实服务器 仅测试离线行为
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class TelnetSessionTest {

    @Test
    public void testDefaultPortConstant() {
        Assert.assertEquals(23, TelnetSession.DEFAULT_TELNET_PORT);
    }

    @Test
    public void testCreateDefault() {
        TelnetSession session = TelnetSession.create("127.0.0.1");
        Assert.assertEquals("127.0.0.1", session.getHost());
        Assert.assertEquals(23, session.getPort());
        Assert.assertNull(session.getUsername());
        Assert.assertEquals("UTF-8", session.getCharset());
        Assert.assertEquals("login:", session.getLoginPrompt());
        Assert.assertEquals("Password:", session.getPasswordPrompt());
        Assert.assertEquals("$", session.getPrompt());
        // 默认终端参数
        Assert.assertEquals("xterm", session.getTerminalType());
        Assert.assertEquals(180, session.getCols());
        Assert.assertEquals(36, session.getRows());
    }

    @Test
    public void testCreateWithPort() {
        TelnetSession session = TelnetSession.create("127.0.0.1", 2323);
        Assert.assertEquals("127.0.0.1", session.getHost());
        Assert.assertEquals(2323, session.getPort());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateBlankHost() {
        TelnetSession.create("");
    }

    @Test(expected = RuntimeException.class)
    public void testCreateNullHost() {
        TelnetSession.create(null);
    }

    @Test
    public void testBuilderChain() {
        TelnetSession session = TelnetSession.create("127.0.0.1");
        TelnetSession same = session.host("192.168.1.1")
                .port(1023)
                .timeout(3000)
                .readTimeout(2000)
                .username("root")
                .password("pass")
                .charset("GBK")
                .loginPrompt("Username:")
                .passwordPrompt("Pwd:")
                .prompt("#")
                .terminalType("vt100")
                .size(80, 24);
        // 链式调用返回自身
        Assert.assertSame(session, same);
        Assert.assertEquals("192.168.1.1", session.getHost());
        Assert.assertEquals(1023, session.getPort());
        Assert.assertEquals("root", session.getUsername());
        Assert.assertEquals("GBK", session.getCharset());
        Assert.assertEquals("Username:", session.getLoginPrompt());
        Assert.assertEquals("Pwd:", session.getPasswordPrompt());
        Assert.assertEquals("#", session.getPrompt());
        Assert.assertEquals("vt100", session.getTerminalType());
        Assert.assertEquals(80, session.getCols());
        Assert.assertEquals(24, session.getRows());
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeTimeout() {
        TelnetSession.create("127.0.0.1").timeout(-1);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeReadTimeout() {
        TelnetSession.create("127.0.0.1").readTimeout(-1);
    }

    @Test
    public void testIsConnectedDefault() {
        Assert.assertFalse(TelnetSession.create("127.0.0.1").isConnected());
    }

    @Test
    public void testGetCommandExecutorNotConnected() {
        TelnetSession session = TelnetSession.create("127.0.0.1");
        try {
            session.getCommandExecutor("pwd");
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("telnet session is not connected"));
        }
    }

    @Test
    public void testGetShellExecutorNotConnected() {
        TelnetSession session = TelnetSession.create("127.0.0.1");
        try {
            session.getShellExecutor();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("telnet session is not connected"));
        }
    }

    @Test
    public void testDisconnectNotConnected() {
        // 未连接时 disconnect 不会抛出异常
        TelnetSession session = TelnetSession.create("127.0.0.1");
        session.disconnect();
        Assert.assertFalse(session.isConnected());
    }

    @Test
    public void testCloseNotConnected() {
        // 未连接时 close 不会抛出异常
        TelnetSession session = TelnetSession.create("127.0.0.1");
        session.close();
        Assert.assertFalse(session.isConnected());
    }

    @Test
    @Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
    public void testConnectOnRealServer() {
        // 需要真实 Telnet 服务器: connect 登录后 getShellExecutor / getCommandExecutor 执行命令
    }

}
