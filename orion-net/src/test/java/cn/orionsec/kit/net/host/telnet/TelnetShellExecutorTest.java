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
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * TelnetShellExecutor 单元测试
 * <p>
 * 使用内存流构造执行器 不连接任何真实服务器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/29
 */
public class TelnetShellExecutorTest {

    /**
     * 创建基于内存流的执行器
     */
    private TelnetShellExecutor createExecutor(String input, ByteArrayOutputStream out) {
        return new TelnetShellExecutor(new TelnetClient(),
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                out, "$", "UTF-8", 0, "xterm", 180, 36);
    }

    private TelnetShellExecutor createExecutor(String input) {
        return createExecutor(input, new ByteArrayOutputStream());
    }

    @Test
    public void testPrompt() {
        TelnetShellExecutor executor = createExecutor("");
        Assert.assertEquals("$", executor.getPrompt());
        executor.prompt("#");
        Assert.assertEquals("#", executor.getPrompt());
    }

    @Test
    public void testCharset() {
        TelnetShellExecutor executor = createExecutor("");
        Assert.assertEquals("UTF-8", executor.getCharset());
        executor.charset("GBK");
        Assert.assertEquals("GBK", executor.getCharset());
    }

    @Test
    public void testTerminalDefaults() {
        TelnetShellExecutor executor = createExecutor("");
        Assert.assertEquals("xterm", executor.getTerminalType());
        Assert.assertEquals(180, executor.getCols());
        Assert.assertEquals(36, executor.getRows());
        executor.terminalType("vt100");
        executor.size(80, 24);
        Assert.assertEquals("vt100", executor.getTerminalType());
        Assert.assertEquals(80, executor.getCols());
        Assert.assertEquals(24, executor.getRows());
    }

    @Test
    public void testInitialState() {
        TelnetShellExecutor executor = createExecutor("");
        Assert.assertFalse(executor.isDone());
        Assert.assertNotNull(executor.getInputStream());
        Assert.assertNotNull(executor.getOutputStream());
        // client 未连接
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    public void testWriteBytes() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TelnetShellExecutor executor = createExecutor("", out);
        executor.write("pwd".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("pwd", out.toString());
    }

    @Test
    public void testWriteString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TelnetShellExecutor executor = createExecutor("", out);
        // 接口默认方法
        executor.write("ls");
        executor.writeLine("cd /");
        Assert.assertEquals("lscd /\n", out.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testConnectNotConnected() {
        // client 未连接时 connect 校验抛出异常
        createExecutor("").connect();
    }

    @Test
    public void testExecWithoutStreamHandler() {
        TelnetShellExecutor executor = createExecutor("");
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet std output stream handler is null", e.getMessage());
        }
    }

    @Test
    public void testExecNotConnected() {
        TelnetShellExecutor executor = createExecutor("");
        executor.streamHandler(in -> {
        });
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet session is not connected", e.getMessage());
        }
    }

    @Test
    public void testReadUntil() throws IOException {
        TelnetShellExecutor executor = createExecutor("welcome$ tail");
        // 读取直到命中 $
        Assert.assertEquals("welcome$", executor.readUntil("$"));
    }

    @Test
    public void testReadUntilMultiCharPattern() throws IOException {
        TelnetShellExecutor executor = createExecutor("line1\nlogin: ");
        Assert.assertEquals("line1\nlogin:", executor.readUntil("login:"));
    }

    @Test
    public void testReadUntilNotFound() throws IOException {
        // 未命中时读取到流结束
        TelnetShellExecutor executor = createExecutor("no-pattern-here");
        Assert.assertEquals("no-pattern-here", executor.readUntil("$"));
    }

    @Test
    public void testReadUntilBlankPattern() throws IOException {
        TelnetShellExecutor executor = createExecutor("anything");
        Assert.assertEquals("", executor.readUntil(""));
        Assert.assertEquals("", executor.readUntil(null));
    }

    @Test
    public void testReadUntilPrompt() throws IOException {
        TelnetShellExecutor executor = createExecutor("cmd output\n$ ");
        Assert.assertEquals("cmd output\n$", executor.readUntilPrompt());
    }

    @Test
    public void testReadUntilUtf8MultiByte() throws IOException {
        // UTF-8 多字节字符按 charset 正确解码
        TelnetShellExecutor executor = createExecutor("中文输出$ ");
        Assert.assertEquals("中文输出$", executor.readUntil("$"));
    }

    @Test
    public void testReadUntilGbkDecode() throws IOException {
        // GBK 编码流按 GBK 解码
        byte[] gbkBytes = "中文提示符# ".getBytes(Charset.forName("GBK"));
        TelnetShellExecutor executor = new TelnetShellExecutor(new TelnetClient(),
                new ByteArrayInputStream(gbkBytes),
                new ByteArrayOutputStream(), "#", "GBK", 0, "xterm", 180, 36);
        Assert.assertEquals("中文提示符#", executor.readUntil("#"));
    }

    @Test(expected = RuntimeException.class)
    public void testReadUntilBufferOverflow() throws IOException {
        // 超过默认 32KB 未命中 pattern 抛出溢出异常
        char[] chars = new char[33000];
        Arrays.fill(chars, 'a');
        createExecutor(new String(chars)).readUntil("$");
    }

    @Test
    public void testMaxReadBufferConfigurable() throws IOException {
        char[] chars = new char[100];
        Arrays.fill(chars, 'a');
        TelnetShellExecutor executor = createExecutor(new String(chars));
        // 缩小缓冲区上限后溢出
        executor.maxReadBuffer(16);
        Assert.assertEquals(16, executor.getMaxReadBuffer());
        try {
            executor.readUntil("$");
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet read buffer overflow", e.getMessage());
        }
    }

    @Test
    public void testSocketTimeoutConverted() {
        // soTimeout 阻塞超时转为 timeout 异常
        InputStream blockingIn = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new SocketTimeoutException("Read timed out");
            }
        };
        TelnetShellExecutor executor = new TelnetShellExecutor(new TelnetClient(),
                blockingIn, new ByteArrayOutputStream(), "$", "UTF-8", 0, "xterm", 180, 36);
        try {
            executor.readUntil("$");
            Assert.fail("expect exception");
        } catch (IOException e) {
            Assert.fail("expect runtime exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet read timeout", e.getMessage());
        }
    }

    @Test
    public void testWallClockTimeout() {
        // 数据缓慢到达时命中读取超时
        InputStream slowIn = new InputStream() {
            @Override
            public int read() throws IOException {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return 'a';
            }
        };
        TelnetShellExecutor executor = new TelnetShellExecutor(new TelnetClient(),
                slowIn, new ByteArrayOutputStream(), "$", "UTF-8", 0, "xterm", 180, 36);
        try {
            executor.readUntil("$", 50);
            Assert.fail("expect exception");
        } catch (IOException e) {
            Assert.fail("expect runtime exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("telnet read timeout", e.getMessage());
        }
    }

    @Test
    public void testCloseNotConnected() {
        // client 未连接时关闭不会抛出异常
        TelnetShellExecutor executor = createExecutor("");
        executor.close();
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    @Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
    public void testExecOnRealServer() {
        // 需要真实 Telnet 服务器: 连接登录后 exec 监听输出流并交互
    }

}
