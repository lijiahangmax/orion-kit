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
package cn.orionsec.kit.net.ftp.client;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * FtpFile 测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class FtpFileTest {

    /**
     * 构建原始 FTPFile
     */
    private static FTPFile createRawFile(String name, int type) {
        FTPFile file = new FTPFile();
        file.setName(name);
        file.setSize(10L);
        file.setType(type);
        file.setUser("root");
        file.setGroup("admin");
        file.setLink("link-target");
        file.setHardLinkCount(2);
        file.setTimestamp(Calendar.getInstance());
        file.setRawListing("-rw-r--r-- 2 root admin 10 Mar 12 09:50 " + name);
        file.setPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION, true);
        file.setPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION, true);
        file.setPermission(FTPFile.GROUP_ACCESS, FTPFile.READ_PERMISSION, true);
        file.setPermission(FTPFile.WORLD_ACCESS, FTPFile.READ_PERMISSION, true);
        return file;
    }

    @Test
    public void testConstructorMapping() {
        FTPFile raw = createRawFile("a.txt", FTPFile.FILE_TYPE);
        FtpFile file = new FtpFile("/home/a.txt", raw);
        assertSame(raw, file.getFtpFile());
        assertEquals("/home/a.txt", file.getPath());
        assertEquals("a.txt", file.getName());
        assertEquals(10L, file.getSize());
        assertEquals(FTPFile.FILE_TYPE, file.getType());
        assertEquals("root", file.getUser());
        assertEquals("admin", file.getGroup());
        assertEquals("link-target", file.getLink());
        assertEquals(2, file.getHardLinkCount());
        assertNotNull(file.getModifyTime());
        assertEquals("-rw-r--r-- 2 root admin 10 Mar 12 09:50 a.txt", file.getRawListing());
        assertEquals("-rw-r--r--", file.getPermissionString());
        assertEquals("/home/a.txt", file.toString());
    }

    @Test
    public void testPermissionArray() {
        FtpFile file = new FtpFile("/a.txt", createRawFile("a.txt", FTPFile.FILE_TYPE));
        boolean[] permission = file.getPermission();
        assertEquals(9, permission.length);
        // user: rw-
        assertTrue(permission[0]);
        assertTrue(permission[1]);
        assertFalse(permission[2]);
        // group: r--
        assertTrue(permission[3]);
        assertFalse(permission[4]);
        assertFalse(permission[5]);
        // world: r--
        assertTrue(permission[6]);
        assertFalse(permission[7]);
        assertFalse(permission[8]);
    }

    @Test
    public void testFileType() {
        assertTrue(new FtpFile("/a", createRawFile("a", FTPFile.FILE_TYPE)).isFile());
        assertTrue(new FtpFile("/a", createRawFile("a", FTPFile.DIRECTORY_TYPE)).isDirectory());
        assertTrue(new FtpFile("/a", createRawFile("a", FTPFile.SYMBOLIC_LINK_TYPE)).isSymbolicLink());
        assertTrue(new FtpFile("/a", createRawFile("a", FTPFile.UNKNOWN_TYPE)).isUnknown());
        FtpFile file = new FtpFile("/a", createRawFile("a", FTPFile.FILE_TYPE));
        assertFalse(file.isDirectory());
        assertFalse(file.isSymbolicLink());
        assertFalse(file.isUnknown());
    }

    @Test
    public void testSetter() {
        FtpFile file = new FtpFile("/a.txt", createRawFile("a.txt", FTPFile.FILE_TYPE));
        file.setPath("/b.txt");
        file.setName("b.txt");
        file.setSize(20L);
        file.setType(FTPFile.DIRECTORY_TYPE);
        file.setUser("user1");
        file.setGroup("group1");
        file.setLink("link1");
        file.setHardLinkCount(3);
        Date now = new Date();
        file.setModifyTime(now);
        file.setRawListing("raw");
        file.setPermissionString("drwxr-xr-x");
        boolean[] permission = new boolean[9];
        file.setPermission(permission);

        assertEquals("/b.txt", file.getPath());
        assertEquals("b.txt", file.getName());
        assertEquals(20L, file.getSize());
        assertEquals(FTPFile.DIRECTORY_TYPE, file.getType());
        assertEquals("user1", file.getUser());
        assertEquals("group1", file.getGroup());
        assertEquals("link1", file.getLink());
        assertEquals(3, file.getHardLinkCount());
        assertEquals(now, file.getModifyTime());
        assertEquals("raw", file.getRawListing());
        assertEquals("drwxr-xr-x", file.getPermissionString());
        assertSame(permission, file.getPermission());
    }

}
