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
package cn.orionsec.kit.net.host.ssh.command;

import com.jcraft.jsch.ChannelExec;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * CommandExecutor 单元测试
 * <p>
 * 不连接任何真实服务器 仅测试离线行为
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CommandExecutorTest {

    /**
     * 创建未连接的执行器
     */
    private CommandExecutor createExecutor(String command) {
        return new CommandExecutor(new ChannelExec(), command);
    }

    @Test
    public void testConstructWithString() {
        CommandExecutor executor = createExecutor("echo 1");
        Assert.assertEquals("echo 1", executor.getCommand());
        Assert.assertArrayEquals("echo 1".getBytes(StandardCharsets.UTF_8), executor.getCommandBytes());
    }

    @Test
    public void testConstructWithCharset() {
        CommandExecutor executor = new CommandExecutor(new ChannelExec(), "echo 中文", "UTF-8");
        Assert.assertEquals("echo 中文", executor.getCommand());
        Assert.assertArrayEquals("echo 中文".getBytes(StandardCharsets.UTF_8), executor.getCommandBytes());
    }

    @Test
    public void testConstructWithBytes() {
        byte[] command = "pwd".getBytes(StandardCharsets.UTF_8);
        CommandExecutor executor = new CommandExecutor(new ChannelExec(), command);
        Assert.assertSame(command, executor.getCommandBytes());
        Assert.assertEquals("pwd", executor.getCommand());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("ls -la", createExecutor("ls -la").toString());
    }

    @Test
    public void testInitialState() {
        CommandExecutor executor = createExecutor("echo 1");
        Assert.assertFalse(executor.isDone());
        Assert.assertFalse(executor.isTimeout());
        Assert.assertNull(executor.getErrorStream());
        Assert.assertNull(executor.getInputStream());
        Assert.assertNull(executor.getOutputStream());
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    public void testExitCodeNotExecuted() {
        CommandExecutor executor = createExecutor("echo 1");
        // 未执行时 jsch 返回 -1
        Assert.assertEquals(-1, executor.getExitCode());
        Assert.assertFalse(executor.isSuccessExit());
    }

    @Test
    public void testSetterNotThrow() throws IOException {
        CommandExecutor executor = createExecutor("echo 1");
        // 以下均为本地状态设置 不需要连接
        executor.merge();
        executor.pty(false);
        executor.pty(true);
        executor.env("LANG", "en_US");
        executor.env("K".getBytes(), "V".getBytes());
        executor.x11Forward(true);
        executor.x11Forward(false);
        executor.setAgentForwarding(true);
        executor.setAgentForwarding(false);
        executor.errorStreamHandler(in -> {
        });
        executor.transferError(new ByteArrayOutputStream());
    }

    @Test
    public void testCheckTimeoutNotSet() {
        CommandExecutor executor = createExecutor("echo 1");
        // 未设置超时时间 永不超时
        Assert.assertFalse(executor.checkTimeout());
        Assert.assertFalse(executor.isTimeout());
    }

    @Test
    public void testCheckTimeoutExceeded() {
        CommandExecutor executor = createExecutor("echo 1");
        // startTime 未初始化(0) 设置 1ms 后必定超时
        executor.timeout(1, null);
        Assert.assertTrue(executor.checkTimeout());
        Assert.assertTrue(executor.isTimeout());
    }

    @Test
    public void testTimeoutWithTimeUnit() {
        CommandExecutor executor = createExecutor("echo 1");
        // 接口默认方法换算单位
        executor.timeout(1, TimeUnit.MILLISECONDS, null);
        Assert.assertTrue(executor.checkTimeout());
        Assert.assertTrue(executor.isTimeout());
    }

    @Test
    public void testExecWithoutStreamHandler() {
        CommandExecutor executor = createExecutor("echo 1");
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("command std out stream handler is null", e.getMessage());
        }
    }

    @Test
    public void testExecNotConnected() {
        CommandExecutor executor = createExecutor("echo 1");
        executor.streamHandler(in -> {
        });
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("channel is not connected", e.getMessage());
        }
    }

    @Test
    public void testCloseNotConnected() {
        CommandExecutor executor = createExecutor("echo 1");
        executor.close();
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试环境连接")
    public void testExecCommandOnRealServer() {
        // 需要真实 SSH 服务器: connect 后 exec 读取命令输出并校验 exitCode
    }

}
