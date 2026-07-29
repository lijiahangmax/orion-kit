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

import static org.junit.Assert.assertEquals;

/**
 * CsvRawReader 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvRawReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testRawRoundTrip() throws Exception {
        File file = folder.newFile("raw.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRow(new String[]{"1", "tom", "developer"});
        writer.addRow(new String[]{"2", "jerry", "tester"});
        writer.close();
        CsvRawReader reader = new CsvRawReader(new CsvReader(file));
        reader.read();
        reader.close();
        List<String> lines = new ArrayList<>(reader.getRows());
        assertEquals(2, lines.size());
        assertEquals("1,tom,developer", lines.get(0));
        assertEquals("2,jerry,tester", lines.get(1));
    }

    @Test
    public void testRawQualifierRoundTrip() throws Exception {
        File file = folder.newFile("raw-qualifier.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        // 包含逗号的列写出后带限定符
        writer.addRow(new String[]{"a,b", "c"});
        writer.close();
        CsvRawReader reader = new CsvRawReader(new CsvReader(file));
        reader.read();
        reader.close();
        List<String> lines = new ArrayList<>(reader.getRows());
        assertEquals(1, lines.size());
        // 原始行保留转义
        assertEquals("\"a,b\",c", lines.get(0));
    }

    @Test
    public void testRawChineseRoundTrip() throws Exception {
        File file = folder.newFile("raw-chinese.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRow(new String[]{"中文", "测试"});
        writer.close();
        CsvRawReader reader = new CsvRawReader(new CsvReader(file));
        reader.read();
        reader.close();
        List<String> lines = new ArrayList<>(reader.getRows());
        assertEquals(1, lines.size());
        assertEquals("中文,测试", lines.get(0));
    }

    @Test
    public void testRawConsumerRoundTrip() throws Exception {
        File file = folder.newFile("raw-consumer.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRow(new String[]{"1", "a"});
        writer.addRow(new String[]{"2", "b"});
        writer.close();
        List<String> collected = new ArrayList<>();
        CsvRawReader reader = new CsvRawReader(new CsvReader(file), collected::add);
        reader.read();
        reader.close();
        assertEquals(2, collected.size());
        assertEquals("1,a", collected.get(0));
        assertEquals("2,b", collected.get(1));
    }

}
