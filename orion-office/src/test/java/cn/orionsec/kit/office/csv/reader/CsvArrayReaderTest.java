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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * CsvArrayReader 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvArrayReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入测试文件
     */
    private File writeFile(String name, String[]... rows) throws Exception {
        File file = folder.newFile(name);
        CsvArrayWriter writer = new CsvArrayWriter(file);
        for (String[] row : rows) {
            writer.addRow(row);
        }
        writer.close();
        return file;
    }

    @Test
    public void testReadAllRoundTrip() throws Exception {
        File file = this.writeFile("read-all.csv",
                new String[]{"1", "tom", "developer"},
                new String[]{"2", "jerry", "tester"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "tom", "developer"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "jerry", "tester"}, rows.get(1));
    }

    @Test
    public void testReadColumnsRoundTrip() throws Exception {
        File file = this.writeFile("read-columns.csv",
                new String[]{"1", "tom", "developer"},
                new String[]{"2", "jerry", "tester"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        // 只读取第 2 0 列
        reader.columns(1, 0);
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"tom", "1"}, rows.get(0));
        assertArrayEquals(new String[]{"jerry", "2"}, rows.get(1));
    }

    @Test
    public void testReadCapacityRoundTrip() throws Exception {
        File file = this.writeFile("read-capacity.csv",
                new String[]{"1", "tom", "developer"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        // 容量截断
        reader.capacity(2);
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"1", "tom"}, rows.get(0));
    }

    @Test
    public void testColumnOfNullRoundTrip() throws Exception {
        File file = this.writeFile("read-null-column.csv",
                new String[]{"1", "tom"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        // 读取越界列使用默认值
        reader.columnOfNull("N/A");
        reader.columns(0, 1, 5);
        reader.read();
        reader.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(1, rows.size());
        assertArrayEquals(new String[]{"1", "tom", "N/A"}, rows.get(0));
    }

    @Test
    public void testConsumerRoundTrip() throws Exception {
        File file = this.writeFile("read-consumer.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"});
        List<String[]> collected = new ArrayList<>();
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file), collected::add);
        reader.read();
        reader.close();
        assertEquals(2, collected.size());
        assertArrayEquals(new String[]{"1", "tom"}, collected.get(0));
        assertArrayEquals(new String[]{"2", "jerry"}, collected.get(1));
    }

}
