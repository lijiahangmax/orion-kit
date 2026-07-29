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
package cn.orionsec.kit.net.host.ssh.shell;

import cn.orionsec.kit.net.host.ssh.TerminalType;
import com.jcraft.jsch.ChannelShell;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * ShellExecutor 单元测试
 * <p>
 * ChannelShell 构造器为包级私有 通过反射创建未连接实例
 * 不连接任何真实服务器 仅测试离线行为
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class ShellExecutorTest {

    /**
     * 反射创建未连接的 ChannelShell
     */
    private static ChannelShell newChannelShell() {
        try {
            Constructor<ChannelShell> constructor = ChannelShell.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShellExecutor createExecutor() {
        return new ShellExecutor(newChannelShell());
    }

    @Test
    public void testDefaultValue() {
        ShellExecutor executor = createExecutor();
        Assert.assertEquals(TerminalType.XTERM.getType(), executor.getTerminalType());
        Assert.assertEquals(180, executor.getCols());
        Assert.assertEquals(36, executor.getRows());
        Assert.assertEquals(1366, executor.getWidth());
        Assert.assertEquals(768, executor.getHeight());
    }

    @Test
    public void testInitialState() {
        ShellExecutor executor = createExecutor();
        // 构造时即初始化输入输出流
        Assert.assertNotNull(executor.getInputStream());
        Assert.assertNotNull(executor.getOutputStream());
        Assert.assertFalse(executor.isDone());
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    public void testGetChannel() {
        ChannelShell channel = newChannelShell();
        ShellExecutor executor = new ShellExecutor(channel);
        Assert.assertSame(channel, executor.getChannel());
    }

    @Test
    public void testTerminalTypeString() {
        ShellExecutor executor = createExecutor();
        executor.terminalType("linux");
        Assert.assertEquals("linux", executor.getTerminalType());
    }

    @Test
    public void testTerminalTypeEnum() {
        ShellExecutor executor = createExecutor();
        // 接口默认方法
        executor.terminalType(TerminalType.XTERM_256_COLOR);
        Assert.assertEquals("xterm-256color", executor.getTerminalType());
    }

    @Test
    public void testSize() {
        ShellExecutor executor = createExecutor();
        executor.size(100, 20);
        Assert.assertEquals(100, executor.getCols());
        Assert.assertEquals(20, executor.getRows());
        // dpi 不变
        Assert.assertEquals(1366, executor.getWidth());
        Assert.assertEquals(768, executor.getHeight());
    }

    @Test
    public void testDpi() {
        ShellExecutor executor = createExecutor();
        executor.dpi(1920, 1080);
        Assert.assertEquals(1920, executor.getWidth());
        Assert.assertEquals(1080, executor.getHeight());
        // 行列不变
        Assert.assertEquals(180, executor.getCols());
        Assert.assertEquals(36, executor.getRows());
    }

    @Test
    public void testSizeAll() {
        ShellExecutor executor = createExecutor();
        executor.size(120, 40, 1024, 768);
        Assert.assertEquals(120, executor.getCols());
        Assert.assertEquals(40, executor.getRows());
        Assert.assertEquals(1024, executor.getWidth());
        Assert.assertEquals(768, executor.getHeight());
    }

    @Test
    public void testResizeNotConnected() {
        // 未连接时 resize 只更新本地 pty 参数 不会抛出异常
        ShellExecutor executor = createExecutor();
        executor.size(90, 30);
        executor.resize();
    }

    @Test
    public void testSetterNotThrow() {
        ShellExecutor executor = createExecutor();
        executor.env("LANG", "en_US");
        executor.env("K".getBytes(), "V".getBytes());
        executor.x11Forward(true);
        executor.x11Forward(false);
        executor.setAgentForwarding(true);
        executor.setAgentForwarding(false);
        executor.callback(() -> {
        });
    }

    @Test
    public void testExecWithoutStreamHandler() {
        ShellExecutor executor = createExecutor();
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("shell std output stream handler is null", e.getMessage());
        }
    }

    @Test
    public void testExecNotConnected() {
        ShellExecutor executor = createExecutor();
        executor.streamHandler(in -> {
        });
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("channel is not connected", e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testConnectWithoutSession() {
        // channel 未绑定 session 连接失败抛出异常 (不会发起真实网络连接)
        createExecutor().connect();
    }

    @Test
    public void testCloseNotConnected() {
        ShellExecutor executor = createExecutor();
        executor.close();
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试环境连接")
    public void testShellOnRealServer() {
        // 需要真实 SSH 服务器: connect 后 exec 交互式读写 shell
    }

}
