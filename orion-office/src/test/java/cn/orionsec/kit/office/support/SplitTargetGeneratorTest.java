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
package cn.orionsec.kit.office.support;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * {@link SplitTargetGenerator} 目标输出流生成器测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class SplitTargetGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 暴露 protected 成员的子类
     */
    private static class TestGenerator extends SplitTargetGenerator {

        private TestGenerator(String suffix) {
            this.suffix = suffix;
        }

        private boolean callHasNext() {
            return super.hasNext();
        }

        private void callNext() {
            super.next();
        }

        private OutputStream current() {
            return currentOutputStream;
        }
    }

    @Test
    public void testTargetOutputStreams() {
        TestGenerator generator = new TestGenerator("csv");
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        assertSame(generator, generator.target(out1, out2));
        assertTrue(generator.callHasNext());
        generator.callNext();
        assertSame(out1, generator.current());
        assertTrue(generator.callHasNext());
        generator.callNext();
        assertSame(out2, generator.current());
        // 目标已耗尽
        assertFalse(generator.callHasNext());
        generator.callNext();
        assertNull(generator.current());
        generator.close();
    }

    @Test
    public void testTargetFilesAndAutoClose() throws Exception {
        File t1 = folder.newFile("t1.csv");
        File t2 = folder.newFile("t2.csv");
        TestGenerator generator = new TestGenerator("csv");
        generator.autoClose(true);
        generator.target(t1, t2);
        generator.callNext();
        generator.current().write("a1".getBytes(StandardCharsets.UTF_8));
        generator.callNext();
        generator.current().write("a2".getBytes(StandardCharsets.UTF_8));
        generator.close();
        assertEquals("a1", new String(Files.readAllBytes(t1.toPath()), StandardCharsets.UTF_8));
        assertEquals("a2", new String(Files.readAllBytes(t2.toPath()), StandardCharsets.UTF_8));
    }

    @Test
    public void testTargetPathGenerate() throws Exception {
        File dir = folder.newFolder("out");
        TestGenerator generator = new TestGenerator("csv");
        assertSame(generator, generator.targetPath(dir.getAbsolutePath(), "part"));
        // 路径生成模式下始终有下一个
        assertTrue(generator.callHasNext());
        generator.callNext();
        generator.current().write("p1".getBytes(StandardCharsets.UTF_8));
        generator.current().close();
        generator.callNext();
        generator.current().write("p2".getBytes(StandardCharsets.UTF_8));
        generator.current().close();
        generator.close();
        File part1 = new File(dir, "part1.csv");
        File part2 = new File(dir, "part2.csv");
        assertTrue(part1.exists());
        assertTrue(part2.exists());
        assertEquals("p1", new String(Files.readAllBytes(part1.toPath()), StandardCharsets.UTF_8));
        assertEquals("p2", new String(Files.readAllBytes(part2.toPath()), StandardCharsets.UTF_8));
    }

    @Test
    public void testTargetPathWithNameSuffix() throws Exception {
        File dir = folder.newFolder("out");
        TestGenerator generator = new TestGenerator("txt");
        generator.targetPath(dir.getAbsolutePath(), "data", "-");
        generator.callNext();
        generator.current().write("x".getBytes(StandardCharsets.UTF_8));
        generator.current().close();
        generator.close();
        File target = new File(dir, "data-1.txt");
        assertTrue(target.exists());
        assertEquals("x", new String(Files.readAllBytes(target.toPath()), StandardCharsets.UTF_8));
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyTargetThrows() {
        new TestGenerator("csv").target(new OutputStream[0]);
    }

    @Test(expected = RuntimeException.class)
    public void testNullTargetPathDirThrows() {
        new TestGenerator("csv").targetPath(null, "part");
    }

    @Test(expected = RuntimeException.class)
    public void testNullTargetBaseNameThrows() throws Exception {
        File dir = folder.newFolder("out");
        new TestGenerator("csv").targetPath(dir.getAbsolutePath(), null);
    }

}
