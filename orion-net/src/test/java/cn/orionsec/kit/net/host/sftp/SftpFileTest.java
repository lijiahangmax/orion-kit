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

import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.Date;

/**
 * SftpFile 单元测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class SftpFileTest {

    @Test
    public void testSetterGetter() {
        SftpFile file = new SftpFile();
        Date accessTime = new Date();
        Date modifyTime = new Date();
        file.setPath("/home/orion/test.txt");
        file.setAccessTime(accessTime);
        file.setModifyTime(modifyTime);
        file.setSize(1024L);
        file.setUid(1000);
        file.setGid(1001);
        file.setPermission(644);
        file.setPermissionString("-rw-r--r--");

        Assert.assertEquals("/home/orion/test.txt", file.getPath());
        Assert.assertEquals(accessTime, file.getAccessTime());
        Assert.assertEquals(modifyTime, file.getModifyTime());
        Assert.assertEquals(1024L, file.getSize());
        Assert.assertEquals(1000, file.getUid());
        Assert.assertEquals(1001, file.getGid());
        Assert.assertEquals(644, file.getPermission());
        Assert.assertEquals("-rw-r--r--", file.getPermissionString());
    }

    @Test
    public void testGetName() {
        SftpFile file = new SftpFile();
        file.setPath("/home/orion/test.txt");
        Assert.assertEquals("test.txt", file.getName());
        file.setPath("/dir");
        Assert.assertEquals("dir", file.getName());
    }

    @Test
    public void testIsDirectory() {
        SftpFile file = new SftpFile();
        file.setPermissionString("drwxr-xr-x");
        Assert.assertTrue(file.isDirectory());
        Assert.assertFalse(file.isRegularFile());
        Assert.assertFalse(file.isLinkFile());
    }

    @Test
    public void testIsLinkFile() {
        SftpFile file = new SftpFile();
        file.setPermissionString("lrwxrwxrwx");
        Assert.assertTrue(file.isLinkFile());
        Assert.assertFalse(file.isDirectory());
        Assert.assertFalse(file.isRegularFile());
    }

    @Test
    public void testIsRegularFile() {
        SftpFile file = new SftpFile();
        file.setPermissionString("-rw-r--r--");
        Assert.assertTrue(file.isRegularFile());
        Assert.assertFalse(file.isDirectory());
        Assert.assertFalse(file.isLinkFile());
    }

    @Test
    public void testNullPermissionString() {
        SftpFile file = new SftpFile();
        Assert.assertFalse(file.isDirectory());
        Assert.assertFalse(file.isLinkFile());
        Assert.assertFalse(file.isRegularFile());
    }

    @Test
    public void testToString() {
        SftpFile file = new SftpFile();
        file.setPath("/tmp/a.log");
        Assert.assertEquals("/tmp/a.log", file.toString());
    }

    @Test
    public void testSerializable() {
        Assert.assertTrue(new SftpFile() instanceof Serializable);
    }

    @Test
    public void testDefaultValues() {
        SftpFile file = new SftpFile();
        Assert.assertNull(file.getPath());
        Assert.assertNull(file.getAccessTime());
        Assert.assertNull(file.getModifyTime());
        Assert.assertEquals(0L, file.getSize());
        Assert.assertEquals(0, file.getUid());
        Assert.assertEquals(0, file.getGid());
        Assert.assertEquals(0, file.getPermission());
        Assert.assertNull(file.getPermissionString());
    }

}
