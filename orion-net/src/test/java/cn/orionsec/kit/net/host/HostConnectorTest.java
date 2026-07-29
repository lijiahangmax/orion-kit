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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelStubs;
import org.junit.Assert;
import org.junit.Test;

/**
 * HostConnector 接口默认方法单元测试
 * <p>
 * 使用无 session 的未连接 channel 存根 不建立任何真实 SSH 连接
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class HostConnectorTest {

    private HostConnector connectorOf(Channel channel) {
        return () -> channel;
    }

    @Test
    public void testGetChannel() {
        Channel channel = ChannelStubs.unconnected();
        HostConnector connector = this.connectorOf(channel);
        Assert.assertSame(channel, connector.getChannel());
    }

    @Test
    public void testIsConnected() {
        HostConnector connector = this.connectorOf(ChannelStubs.unconnected());
        Assert.assertFalse(connector.isConnected());
    }

    @Test
    public void testIsClosed() {
        HostConnector connector = this.connectorOf(ChannelStubs.unconnected());
        Assert.assertFalse(connector.isClosed());
    }

    @Test
    public void testIsEof() {
        HostConnector connector = this.connectorOf(ChannelStubs.unconnected());
        Assert.assertFalse(connector.isEof());
    }

    @Test
    public void testDisconnectChannel() {
        // 未连接时断开 channel 不抛出异常
        HostConnector connector = this.connectorOf(ChannelStubs.unconnected());
        connector.disconnectChannel();
        Assert.assertFalse(connector.isConnected());
    }

    @Test(expected = RuntimeException.class)
    public void testConnectWithoutSession() {
        // 无 session 连接失败 包装为连接异常
        this.connectorOf(ChannelStubs.unconnected()).connect();
    }

    @Test(expected = RuntimeException.class)
    public void testConnectTimeoutWithoutSession() {
        this.connectorOf(ChannelStubs.unconnected()).connect(100);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSessionWithoutSession() {
        // channel 未绑定 session 包装为运行时异常
        this.connectorOf(ChannelStubs.unconnected()).getSession();
    }

}
