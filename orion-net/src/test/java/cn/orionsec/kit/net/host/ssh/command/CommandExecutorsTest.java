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

import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import com.jcraft.jsch.Channel;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * CommandExecutors 单元测试
 * <p>
 * 使用内存 stub 执行器测试静态方法 不连接任何真实服务器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class CommandExecutorsTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<CommandExecutors> constructor = CommandExecutors.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Assert.assertNotNull(constructor.newInstance());
    }

    @Test(expected = RuntimeException.class)
    public void testGetCommandOutputResultNullExecutor() throws IOException {
        CommandExecutors.getCommandOutputResult((ICommandExecutor) null);
    }

    @Test
    public void testExecCommandDefaultMerge() throws IOException {
        MockCommandExecutor executor = new MockCommandExecutor("mock-output");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CommandExecutors.execCommand(executor, out);
        // 默认合并输出流
        Assert.assertTrue(executor.merged);
        Assert.assertTrue(executor.connected);
        Assert.assertTrue(executor.executed);
        Assert.assertEquals("mock-output", out.toString());
    }

    @Test
    public void testExecCommandNotMerge() throws IOException {
        MockCommandExecutor executor = new MockCommandExecutor("std-only");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CommandExecutors.execCommand(executor, out, false);
        Assert.assertFalse(executor.merged);
        Assert.assertEquals("std-only", out.toString());
    }

    @Test
    public void testGetCommandOutputResultBytes() throws IOException {
        MockCommandExecutor executor = new MockCommandExecutor("bytes-result");
        byte[] result = CommandExecutors.getCommandOutputResult(executor);
        Assert.assertArrayEquals("bytes-result".getBytes(StandardCharsets.UTF_8), result);
    }

    @Test
    public void testGetCommandOutputResultString() throws IOException {
        MockCommandExecutor executor = new MockCommandExecutor("string-result");
        Assert.assertEquals("string-result", CommandExecutors.getCommandOutputResultString(executor));
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试环境连接")
    public void testGetCommandOutputResultOnRealServer() {
        // 需要真实 SSH 服务器: getCommandOutputResult(host, port, username, password, command)
    }

    /**
     * 内存 stub 执行器 exec 时将预设输出写入 transfer 的输出流
     */
    private static class MockCommandExecutor implements ICommandExecutor {

        private final byte[] output;

        private OutputStream transferOut;

        boolean merged;

        boolean connected;

        boolean executed;

        MockCommandExecutor(String output) {
            this.output = output.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public void merge() {
            this.merged = true;
        }

        @Override
        public void transfer(OutputStream out) {
            this.transferOut = out;
        }

        @Override
        public void connect() {
            this.connected = true;
        }

        @Override
        public void connect(int timeout) {
            this.connected = true;
        }

        @Override
        public void exec() {
            this.executed = true;
            try {
                transferOut.write(output);
                transferOut.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void pty(boolean enable) {
        }

        @Override
        public void errorStreamHandler(Consumer<InputStream> errorStreamHandler) {
        }

        @Override
        public void transferError(OutputStream out) {
        }

        @Override
        public void timeout(long timeout, TimeoutChecker<TimeoutEndpoint> checker) {
        }

        @Override
        public boolean checkTimeout() {
            return false;
        }

        @Override
        public int getExitCode() {
            return 0;
        }

        @Override
        public boolean isTimeout() {
            return false;
        }

        @Override
        public String getCommand() {
            return new String(output, StandardCharsets.UTF_8);
        }

        @Override
        public byte[] getCommandBytes() {
            return output;
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public void env(String key, String value) {
        }

        @Override
        public void env(byte[] key, byte[] value) {
        }

        @Override
        public void x11Forward(boolean enable) {
        }

        @Override
        public void setAgentForwarding(boolean enable) {
        }

        @Override
        public void sendSignal(String signal) {
        }

        @Override
        public void callback(Runnable callback) {
        }

        @Override
        public void streamHandler(Consumer<InputStream> streamHandler) {
        }

        @Override
        public void write(byte[] command) {
        }

        @Override
        public InputStream getInputStream() {
            return null;
        }

        @Override
        public OutputStream getOutputStream() {
            return null;
        }

        @Override
        public boolean isDone() {
            return executed;
        }

        @Override
        public Channel getChannel() {
            return null;
        }

        @Override
        public void close() {
        }

    }

}
