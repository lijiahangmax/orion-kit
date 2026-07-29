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
package cn.orionsec.kit.net.host.ssh;

import cn.orionsec.kit.net.host.ssh.command.CommandExecutor;
import com.jcraft.jsch.ChannelExec;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * BaseSshExecutor 单元测试
 * <p>
 * BaseSshExecutor 为抽象类 使用 CommandExecutor 作为具体实现进行测试
 * 不连接任何真实服务器 仅测试离线行为
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class BaseSshExecutorTest {

    /**
     * 创建未连接的执行器
     */
    private CommandExecutor createExecutor() {
        return new CommandExecutor(new ChannelExec(), "echo 1");
    }

    @Test
    public void testGetChannel() {
        ChannelExec channel = new ChannelExec();
        CommandExecutor executor = new CommandExecutor(channel, "echo 1");
        Assert.assertSame(channel, executor.getChannel());
    }

    @Test
    public void testInitialState() {
        CommandExecutor executor = createExecutor();
        Assert.assertFalse(executor.isDone());
        // 未执行时 标准输入/输出流未初始化
        Assert.assertNull(executor.getInputStream());
        Assert.assertNull(executor.getOutputStream());
        // 未连接
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    public void testCallbackSetter() {
        CommandExecutor executor = createExecutor();
        // 仅设置回调 不触发执行
        executor.callback(() -> {
        });
    }

    @Test
    public void testExecWithoutStreamHandler() {
        CommandExecutor executor = createExecutor();
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("command std out stream handler is null", e.getMessage());
        }
    }

    @Test
    public void testStreamHandlerSetter() {
        CommandExecutor executor = createExecutor();
        executor.streamHandler(in -> {
        });
        // 设置处理器后 exec 报错信息变为未连接 证明 handler 已被设置
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("channel is not connected", e.getMessage());
        }
    }

    @Test
    public void testTransferSetter() throws IOException {
        CommandExecutor executor = createExecutor();
        executor.transfer(new ByteArrayOutputStream());
        // transfer 内部设置了 streamHandler
        try {
            executor.exec();
            Assert.fail("expect exception");
        } catch (RuntimeException e) {
            Assert.assertEquals("channel is not connected", e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testSendSignalNotConnected() {
        // 未连接时发送信号量抛出异常
        createExecutor().sendSignal("INT");
    }

    @Test
    public void testCloseNotConnected() {
        // 未连接时关闭不会抛出异常
        CommandExecutor executor = createExecutor();
        executor.close();
        Assert.assertFalse(executor.isConnected());
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试环境连接")
    public void testExecOnRealServer() {
        // 需要真实 SSH 服务器: 连接后 exec 监听标准输出并触发 callback
    }

}
