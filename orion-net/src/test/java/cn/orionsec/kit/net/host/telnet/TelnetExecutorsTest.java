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

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * TelnetExecutors 单元测试
 * <p>
 * 静态方法均需真实 Telnet 服务器 仅测试类结构
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class TelnetExecutorsTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        // 工具类构造器私有
        Constructor<TelnetExecutors> constructor = TelnetExecutors.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Assert.assertNotNull(constructor.newInstance());
    }

    @Test
    public void testStaticMethodExist() throws Exception {
        // 校验方法签名存在
        Assert.assertNotNull(TelnetExecutors.class.getMethod("getCommandOutputResult",
                String.class, String.class, String.class, String.class));
        Assert.assertNotNull(TelnetExecutors.class.getMethod("getCommandOutputResult",
                String.class, int.class, String.class, String.class, String.class));
        Assert.assertNotNull(TelnetExecutors.class.getMethod("getCommandOutputResultString",
                ITelnetCommandExecutor.class));
        Assert.assertNotNull(TelnetExecutors.class.getMethod("getCommandOutputResult",
                ITelnetCommandExecutor.class));
        Assert.assertNotNull(TelnetExecutors.class.getMethod("execCommand",
                ITelnetCommandExecutor.class, OutputStream.class));
    }

    @Test
    @Ignore("需要真实 Telnet 服务器 无法在单元测试环境连接")
    public void testGetCommandOutputResultOnRealServer() {
        // 需要真实 Telnet 服务器: getCommandOutputResult(host, port, username, password, command)
    }

}
