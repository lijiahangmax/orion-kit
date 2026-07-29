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
package cn.orionsec.kit.net.host.sftp;

import cn.orionsec.kit.lang.constant.Const;
import com.jcraft.jsch.ChannelSftp;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * SftpExecutor 单元测试
 * <p>
 * 仅测试对象构建和不需要连接的行为 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpExecutorTest {

    @Test
    public void testConstructorDefaultCharset() {
        SftpExecutor executor = new SftpExecutor(new ChannelSftp());
        Assert.assertEquals(Const.UTF_8, executor.getCharset());
    }

    @Test
    public void testConstructorCustomCharset() {
        SftpExecutor executor = new SftpExecutor(new ChannelSftp(), "GBK");
        Assert.assertEquals("GBK", executor.getCharset());
    }

    @Test
    public void testGetChannel() {
        ChannelSftp channel = new ChannelSftp();
        SftpExecutor executor = new SftpExecutor(channel);
        Assert.assertSame(channel, executor.getChannel());
    }

    @Test
    public void testBufferSize() {
        SftpExecutor executor = new SftpExecutor(new ChannelSftp());
        Assert.assertEquals(Const.BUFFER_KB_32, executor.getBufferSize());
        executor.bufferSize(Const.BUFFER_KB_8);
        Assert.assertEquals(Const.BUFFER_KB_8, executor.getBufferSize());
    }

    @Test
    public void testChannelState() {
        // 未连接状态
        SftpExecutor executor = new SftpExecutor(new ChannelSftp());
        Assert.assertFalse(executor.isConnected());
        Assert.assertFalse(executor.isClosed());
        Assert.assertFalse(executor.isEof());
    }

    @Test(expected = RuntimeException.class)
    public void testConnectWithoutSession() {
        // channel 未绑定 session 连接失败 不会发起网络请求
        new SftpExecutor(new ChannelSftp()).connect();
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试中执行远程操作")
    public void testGetHome() {
        new SftpExecutor(new ChannelSftp()).getHome();
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试中执行远程操作")
    public void testFileOperations() {
        SftpExecutor executor = new SftpExecutor(new ChannelSftp());
        executor.isExist("/tmp");
        executor.getFile("/tmp");
        executor.list("/tmp");
    }

    @Test
    @Ignore("需要真实 SSH 服务器 无法在单元测试中执行远程操作")
    public void testGetServerVersion() {
        new SftpExecutor(new ChannelSftp()).getServerVersion();
    }

}
