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

import com.jcraft.jsch.SftpATTRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.Date;

/**
 * SftpFileWrapper 单元测试
 * <p>
 * SftpATTRS 构造器私有 通过反射创建 不需要真实 SSH 服务器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpFileWrapperTest {

    private SftpATTRS attrs;

    private SftpFileWrapper wrapper;

    @Before
    public void setup() throws Exception {
        Constructor<SftpATTRS> constructor = SftpATTRS.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        this.attrs = constructor.newInstance();
        this.wrapper = new SftpFileWrapper("/home/orion/file.txt", attrs);
    }

    @Test
    public void testGetAttrs() {
        Assert.assertSame(attrs, wrapper.getAttrs());
    }

    @Test
    public void testPath() {
        Assert.assertEquals("/home/orion/file.txt", wrapper.getPath());
        Assert.assertEquals("file.txt", wrapper.getName());
        Assert.assertEquals("/home/orion/file.txt", wrapper.toString());
    }

    @Test
    public void testInitialValues() {
        Assert.assertEquals(0L, wrapper.getSize());
        Assert.assertEquals(0, wrapper.getUid());
        Assert.assertEquals(0, wrapper.getGid());
        Assert.assertEquals(0, wrapper.getPermission());
        Assert.assertNotNull(wrapper.getAccessTime());
        Assert.assertNotNull(wrapper.getModifyTime());
        Assert.assertNotNull(wrapper.getPermissionString());
    }

    @Test
    public void testSetSize() {
        wrapper.setSize(2048L);
        Assert.assertEquals(2048L, wrapper.getSize());
        // 同步修改 attrs
        Assert.assertEquals(2048L, attrs.getSize());
    }

    @Test
    public void testSetUidGid() {
        wrapper.setUid(1000);
        Assert.assertEquals(1000, wrapper.getUid());
        Assert.assertEquals(1000, attrs.getUId());

        wrapper.setGid(1001);
        Assert.assertEquals(1001, wrapper.getGid());
        Assert.assertEquals(1001, attrs.getGId());
        // uid 保持不变
        Assert.assertEquals(1000, attrs.getUId());
    }

    @Test
    public void testSetPermission() {
        wrapper.setPermission(755);
        Assert.assertEquals(755, wrapper.getPermission());
        // 10进制 755 -> 8进制 0755
        Assert.assertEquals(0755, attrs.getPermissions() & 0xFFF);
    }

    @Test
    public void testSetModifyTime() {
        // 精确到秒
        Date time = new Date(1700000000000L);
        wrapper.setModifyTime(time);
        Assert.assertEquals(time, wrapper.getModifyTime());
        Assert.assertEquals(1700000000, attrs.getMTime());
    }

    @Test
    public void testSetAccessTime() {
        Date time = new Date(1700000000000L);
        wrapper.setAccessTime(time);
        Assert.assertEquals(time, wrapper.getAccessTime());
        Assert.assertEquals(1700000000, attrs.getATime());
    }

    @Test
    public void testFileType() {
        // 无目录/连接标志时为普通文件
        Assert.assertTrue(wrapper.isRegularFile());
        Assert.assertFalse(wrapper.isDirectory());
        Assert.assertFalse(wrapper.isLinkFile());
    }

    @Test
    public void testExtendsSftpFile() {
        Assert.assertTrue(wrapper instanceof SftpFile);
    }

}
