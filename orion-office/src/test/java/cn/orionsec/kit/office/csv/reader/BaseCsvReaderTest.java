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
package cn.orionsec.kit.office.csv.reader;

import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * BaseCsvReader 功能回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class BaseCsvReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入 5 行测试文件
     */
    private File writeFile(String name) throws Exception {
        File file = folder.newFile(name);
        CsvArrayWriter writer = new CsvArrayWriter(file);
        for (int i = 1; i <= 5; i++) {
            writer.addRow(new String[]{String.valueOf(i), "name" + i});
        }
        writer.close();
        return file;
    }

    @Test
    public void testSkipThenRead() throws Exception {
        File file = this.writeFile("skip-read.csv");
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.skip(2);
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(3, rows.size());
        assertArrayEquals(new String[]{"3", "name3"}, rows.get(0));
        assertEquals(3, reader.getRowNum());
    }

    @Test
    public void testReadCount() throws Exception {
        File file = this.writeFile("read-count.csv");
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read(2);
        assertEquals(2, reader.getRows().size());
        assertEquals(2, reader.getRowNum());
        // 继续读取剩余行
        reader.read();
        reader.close();
        assertEquals(5, reader.getRows().size());
        assertEquals(5, reader.getRowNum());
    }

    @Test
    public void testConsumerRead() throws Exception {
        File file = this.writeFile("consumer-read.csv");
        List<String[]> collected = new ArrayList<>();
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file), collected::add);
        reader.read();
        reader.close();
        assertEquals(5, collected.size());
        assertArrayEquals(new String[]{"1", "name1"}, collected.get(0));
        assertArrayEquals(new String[]{"5", "name5"}, collected.get(4));
    }

    @Test
    public void testClearRows() throws Exception {
        File file = this.writeFile("clear-rows.csv");
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read(2);
        assertEquals(2, reader.getRows().size());
        reader.clear();
        assertEquals(0, reader.getRows().size());
        reader.read(1);
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(1, rows.size());
        // clear 后继续读取第三行
        assertArrayEquals(new String[]{"3", "name3"}, rows.get(0));
    }

    @Test
    public void testGetRaw() throws Exception {
        File file = this.writeFile("get-raw.csv");
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read(1);
        assertEquals("1,name1", reader.getRaw());
        reader.close();
    }

    @Test
    public void testSkipEmptyRows() throws Exception {
        File file = folder.newFile("skip-empty.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRow(new String[]{"1", "a"});
        writer.skip();
        writer.addRow(new String[]{"2", "b"});
        writer.close();
        // 默认跳过空行
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        assertTrue(reader.getOption().isSkipEmptyRows());
        reader.read();
        reader.close();
        assertEquals(2, reader.getRows().size());
    }

}
