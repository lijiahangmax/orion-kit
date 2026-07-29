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

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * IHostExecutor 接口默认方法单元测试
 * <p>
 * 使用内存记录实现 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class IHostExecutorTest {

    /**
     * 记录写入内容的执行器实现
     */
    private static class RecordingHostExecutor implements IHostExecutor {

        private final ByteArrayOutputStream written = new ByteArrayOutputStream();

        private boolean executed;

        private boolean closed;

        private Runnable callback;

        private Consumer<InputStream> streamHandler;

        @Override
        public void callback(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void transfer(OutputStream out) {
        }

        @Override
        public void streamHandler(Consumer<InputStream> streamHandler) {
            this.streamHandler = streamHandler;
        }

        @Override
        public void write(byte[] command) {
            written.write(command, 0, command.length);
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
        public void exec() {
            this.executed = true;
        }

        @Override
        public void close() {
            this.closed = true;
        }

        private String writtenString() {
            return new String(written.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @Test
    public void testRunCallsExec() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        Assert.assertFalse(executor.isDone());
        executor.run();
        Assert.assertTrue(executor.isDone());
    }

    @Test
    public void testWriteString() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.write("ls -la");
        Assert.assertEquals("ls -la", executor.writtenString());
    }

    @Test
    public void testWriteStringWithCharset() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.write("pwd", "UTF-8");
        Assert.assertEquals("pwd", executor.writtenString());
    }

    @Test
    public void testWriteLine() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.writeLine("echo 1");
        Assert.assertEquals("echo 1\n", executor.writtenString());
    }

    @Test
    public void testWriteLineWithCharset() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.writeLine("echo 2", "UTF-8");
        Assert.assertEquals("echo 2\n", executor.writtenString());
    }

    @Test
    public void testInterrupt() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.interrupt();
        // ctrl + c
        Assert.assertArrayEquals(new byte[]{3}, executor.written.toByteArray());
    }

    @Test
    public void testExit() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.exit();
        Assert.assertEquals("exit 0\n", executor.writtenString());
    }

    @Test
    public void testExitWithCode() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.exit(130);
        Assert.assertEquals("exit 130\n", executor.writtenString());
    }

    @Test
    public void testCallbackAndStreamHandler() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        Runnable callback = () -> {
        };
        Consumer<InputStream> handler = in -> {
        };
        executor.callback(callback);
        executor.streamHandler(handler);
        Assert.assertSame(callback, executor.callback);
        Assert.assertSame(handler, executor.streamHandler);
    }

    @Test
    public void testClose() {
        RecordingHostExecutor executor = new RecordingHostExecutor();
        executor.close();
        Assert.assertTrue(executor.closed);
    }

}
