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
package cn.orionsec.kit.office.csv.writer;

import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * BaseCsvWriter 功能回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class BaseCsvWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private List<String[]> readAll(File file) {
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read();
        reader.close();
        return new ArrayList<>(reader.getRows());
    }

    @Test
    public void testMappingRoundTrip() throws Exception {
        File file = folder.newFile("mapping.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        // 交换 0 1 两列
        writer.mapping(0, 1).mapping(1, 0);
        writer.addRow(new String[]{"a", "b"});
        writer.close();
        List<String[]> rows = this.readAll(file);
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"b", "a"}, rows.get(0));
    }

    @Test
    public void testCapacityRoundTrip() throws Exception {
        File file = folder.newFile("capacity.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.capacity(2);
        writer.addRow(new String[]{"1", "2", "3", "4"});
        writer.close();
        List<String[]> rows = this.readAll(file);
        assertEquals(1, rows.size());
        // 只写入容量内的列
        assertArrayEquals(new String[]{"1", "2"}, rows.get(0));
    }

    @Test
    public void testDefaultValueRoundTrip() throws Exception {
        File file = folder.newFile("default.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.defaultValue(1, "def");
        writer.addRow(new String[]{"a", null});
        writer.close();
        List<String[]> rows = this.readAll(file);
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"a", "def"}, rows.get(0));
    }

    @Test
    public void testSkipRows() throws Exception {
        File file = folder.newFile("skip.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRow(new String[]{"1", "a"});
        writer.skip(2);
        writer.addRow(new String[]{"2", "b"});
        writer.close();
        // 文件包含空行
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertEquals(4, lines.size());
        assertEquals("", lines.get(1));
        assertEquals("", lines.get(2));
        // 默认跳过空行读取
        List<String[]> rows = this.readAll(file);
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "a"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "b"}, rows.get(1));
    }

    @Test
    public void testSkipNullRows() throws Exception {
        File file = folder.newFile("skip-null.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        // 跳过 null 行 不产生空行
        writer.skipNullRows(true);
        writer.addRow(new String[]{"1", "a"});
        writer.addRow(null);
        writer.addRow(new String[]{"2", "b"});
        writer.close();
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        // 不跳过 null 行 写出空行
        File file2 = folder.newFile("keep-null.csv");
        CsvArrayWriter writer2 = new CsvArrayWriter(file2);
        writer2.skipNullRows(false);
        writer2.addRow(new String[]{"1", "a"});
        writer2.addRow(null);
        writer2.addRow(new String[]{"2", "b"});
        writer2.close();
        List<String> lines2 = Files.readAllLines(file2.toPath(), StandardCharsets.UTF_8);
        assertEquals(3, lines2.size());
        assertEquals("", lines2.get(1));
    }

    @Test
    public void testAddCommentRoundTrip() throws Exception {
        File file = folder.newFile("comment.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addComment("this is comment");
        writer.addRow(new String[]{"1", "a"});
        writer.close();
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        assertTrue(lines.get(0).startsWith("#"));
        // 开启注释跳过后只读取数据行
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.getOption().setUseComments(true);
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"1", "a"}, rows.get(0));
    }

}
