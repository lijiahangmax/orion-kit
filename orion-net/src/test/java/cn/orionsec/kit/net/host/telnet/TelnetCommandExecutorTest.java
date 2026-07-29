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

import org.apache.commons.net.telnet.TelnetClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TelnetCommandExecutor 单元测试
 * <p>
 * 使用内存流构造执行器 不连接任何真实服务器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public class TelnetCommandExecutorTest {

    /**
     * 模拟已连接的 client
     */
    private static class ConnectedTelnetClient extends TelnetClient {
        @Override
        public boolean isConnected() {
            return true;
        }
    }

    /**
     * 创建基于内存流的执行器
     */
    private TelnetCommandExecutor createExecutor(String input, ByteArrayOutputStream out, String command, boolean connected) {
        return new TelnetCommandExecutor(connected ? new ConnectedTelnetClient() : new TelnetClient(),
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                out, "$", "UTF-8", 0, command);
    }

    private TelnetCommandExecutor createExecutor(String input, ByteArrayOutputStream out, String command) {
        return createExecutor(input, out, command, false);
    }

    @Test
    public void testGetCommand() {
        TelnetCommandExecutor executor = createExecutor("", new ByteArrayOutputStream(), "pwd");
        Assert.assertEquals("pwd", executor.getCommand());
    }

    @Test
    public void testExecCommandCleansEchoAndPrompt() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TelnetCommandExecutor executor = createExecutor("pwd\n/root\n$ ", out, "pwd");
        String result = executor.execCommand("pwd");
        // 命令 + LF 写入输出流
        Assert.assertEquals("pwd\n", out.toString());
        // 默认清理首行回显和末尾提示符
        Assert.assertEquals("/root\n", result);
    }

    @Test
    public void testExecCommandKeepEcho() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TelnetCommandExecutor executor = createExecutor("pwd\n/root\n$ ", out, "pwd");
        executor.keepEcho(true);
        String result = executor.execCommand("pwd");
        // 保留回显和提示符
        Assert.assertEquals("pwd\n/root\n$", result);
    }

    @Test
    public void testExecCommandWithoutEcho() throws IOException {
        // 服务端无回显时不清理首行
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TelnetCommandExecutor executor = createExecutor("/root\n$ ", out, "pwd");
        Assert.assertEquals("/root\n", executor.execCommand("pwd"));
    }

    @Test(expected = RuntimeException.class)
    public void testExecCommandBlank() throws IOException {
        createExecutor("", new ByteArrayOutputStream(), "pwd").execCommand("");
    }

    @Test
    public void testExecNotConnected() {
        TelnetCommandExecutor executor = createExecutor("", new ByteArrayOutputStream(), "pwd");
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet session is not connected", e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testExecBlankCommand() {
        createExecutor("", new ByteArrayOutputStream(), "", true).exec();
    }

    @Test
    public void testExecTransferCollects() throws IOException {
        // exec + transfer 收集命令输出
        ByteArrayOutputStream commandOut = new ByteArrayOutputStream();
        TelnetCommandExecutor executor = createExecutor("pwd\n/root\n$ ", commandOut, "pwd", true);
        AtomicBoolean callback = new AtomicBoolean();
        executor.callback(() -> callback.set(true));
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        TelnetExecutors.execCommand(executor, result);
        Assert.assertEquals("/root\n", result.toString());
        Assert.assertTrue(executor.isDone());
        Assert.assertTrue(callback.get());
    }

    @Test
    public void testExecutorsGetCommandOutputResult() throws IOException {
        // TelnetExecutors 收集输出为 byte[] / String
        TelnetCommandExecutor executor = createExecutor("pwd\n/root\n$ ", new ByteArrayOutputStream(), "pwd", true);
        Assert.assertEquals("/root\n", TelnetExecutors.getCommandOutputResultString(executor));
    }

    @Test
    public void testMaxReadBufferConfigurable() {
        TelnetCommandExecutor executor = createExecutor("aaaaaaaaaaaaaaaaaaaaaaaa", new ByteArrayOutputStream(), "pwd");
        executor.maxReadBuffer(8);
        try {
            executor.execCommand("pwd");
            Assert.fail("expect exception");
        } catch (IOException e) {
            Assert.fail("expect runtime exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet read buffer overflow", e.getMessage());
        }
    }

    @Test
    @Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
    public void testExecCommandOnRealServer() {
        // 需要真实 Telnet 服务器: 连接登录后 execCommand 读取真实回显
    }

}
