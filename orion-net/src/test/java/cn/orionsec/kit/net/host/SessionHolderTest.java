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

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * SessionHolder 单元测试
 * <p>
 * 不建立任何真实 SSH 连接 仅测试创建和配置
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SessionHolderTest {

    @Test
    public void testDefaultSshPort() {
        Assert.assertEquals(22, SessionHolder.DEFAULT_SSH_PORT);
    }

    @Test
    public void testCreate() {
        SessionHolder holder = SessionHolder.create();
        Assert.assertNotNull(holder);
        Assert.assertNotNull(holder.ch);
    }

    @Test
    public void testConstructorWithJsch() {
        JSch ch = new JSch();
        SessionHolder holder = new SessionHolder(ch);
        Assert.assertSame(ch, holder.ch);
    }

    @Test(expected = RuntimeException.class)
    public void testConstructorNullJsch() {
        new SessionHolder(null);
    }

    @Test
    public void testStaticConfig() {
        // 静态块设置的全局配置
        Assert.assertEquals("no", JSch.getConfig("StrictHostKeyChecking"));
        Assert.assertTrue(JSch.getConfig("server_host_key").contains("ssh-rsa"));
        Assert.assertTrue(JSch.getConfig("PubkeyAcceptedAlgorithms").contains("ssh-rsa"));
    }

    @Test
    public void testSetLogger() {
        SessionHolder holder = SessionHolder.create();
        // 设置各级别日志不抛出异常
        holder.setLogger(SessionLogger.DEBUG);
        holder.setLogger(SessionLogger.ERROR);
    }

    @Test(expected = RuntimeException.class)
    public void testAddIdentityNullPath() {
        SessionHolder.create().addIdentity(null);
    }

    @Test
    public void testAddIdentityInvalidFile() {
        SessionHolder holder = SessionHolder.create();
        try {
            holder.addIdentity("/not/exists/id_rsa_" + System.nanoTime());
            Assert.fail("should throw exception");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("add identity error"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void testAddIdentityValueNullKeyName() {
        SessionHolder.create().addIdentityValue(null, "value");
    }

    @Test(expected = RuntimeException.class)
    public void testAddIdentityValueNullKey() {
        SessionHolder.create().addIdentityValue("key", null);
    }

    @Test
    public void testAddIdentityValueAndRemove() throws Exception {
        SessionHolder holder = SessionHolder.create();
        // 内存生成密钥 不需要服务器
        KeyPair keyPair = KeyPair.genKeyPair(holder.ch, KeyPair.RSA, 2048);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        keyPair.writePrivateKey(out);
        String privateKey = out.toString();

        holder.addIdentityValue("orion-test-key", privateKey);
        List<String> keys = holder.getLoadKeys();
        Assert.assertTrue(keys.contains("orion-test-key"));

        // 删除指定密钥
        holder.removeIdentity("orion-test-key");
        Assert.assertFalse(holder.getLoadKeys().contains("orion-test-key"));

        // 删除所有密钥
        holder.addIdentityValue("orion-test-key2", privateKey);
        Assert.assertEquals(1, holder.getLoadKeys().size());
        holder.removeAllIdentity();
        Assert.assertTrue(holder.getLoadKeys().isEmpty());
    }

    @Test
    public void testGetLoadKeysEmpty() {
        SessionHolder holder = SessionHolder.create();
        Assert.assertTrue(holder.getLoadKeys().isEmpty());
    }

    @Test
    public void testSetKnownHostsStream() {
        SessionHolder holder = SessionHolder.create();
        holder.setKnownHosts(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testGetSessionDefaultPort() {
        // 仅创建 session 不连接
        SessionStore store = SessionHolder.create().getSession("127.0.0.1", "root");
        Assert.assertNotNull(store);
        Assert.assertEquals("127.0.0.1", store.getHost());
        Assert.assertEquals(SessionHolder.DEFAULT_SSH_PORT, store.getPort());
        Assert.assertEquals("root", store.getUsername());
        Assert.assertFalse(store.isConnected());
    }

    @Test
    public void testGetSessionCustomPort() {
        SessionStore store = SessionHolder.create().getSession("192.168.1.100", 2222, "admin");
        Assert.assertNotNull(store);
        Assert.assertEquals("192.168.1.100", store.getHost());
        Assert.assertEquals(2222, store.getPort());
        Assert.assertEquals("admin", store.getUsername());
    }

}
