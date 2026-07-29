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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * CsvArrayWriter 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvArrayWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入后读取
     */
    private List<String[]> writeThenRead(File file, List<String[]> rows) {
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.addRows(rows);
        writer.flush();
        writer.close();
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read();
        reader.close();
        return new ArrayList<>(reader.getRows());
    }

    @Test
    public void testArrayRoundTrip() throws Exception {
        File file = folder.newFile("array.csv");
        List<String[]> rows = Arrays.asList(
                new String[]{"1", "tom", "developer"},
                new String[]{"2", "jerry", "tester"},
                new String[]{"3", "spike", "manager"});
        List<String[]> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertArrayEquals(rows.get(i), readRows.get(i));
        }
    }

    @Test
    public void testSpecialCharacterRoundTrip() throws Exception {
        File file = folder.newFile("special.csv");
        List<String[]> rows = Arrays.asList(
                new String[]{"a,b", "he said \"hello\"", "line1\nline2"},
                new String[]{",start", "quote\"middle", "end,"},
                new String[]{"\"full\"", "multi\nline\ncell", "normal"});
        List<String[]> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertArrayEquals(rows.get(i), readRows.get(i));
        }
    }

    @Test
    public void testChineseRoundTrip() throws Exception {
        File file = folder.newFile("chinese.csv");
        List<String[]> rows = Arrays.asList(
                new String[]{"中文", "逗号，测试", "引号\"测试\""},
                new String[]{"你好世界", "换行\n测试", "混合, \"内容\""});
        List<String[]> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertArrayEquals(rows.get(i), readRows.get(i));
        }
    }

    @Test
    public void testEmptyValueRoundTrip() throws Exception {
        File file = folder.newFile("empty.csv");
        List<String[]> rows = Arrays.asList(
                new String[]{"", "middle", ""},
                new String[]{"1", "", "3"},
                new String[]{"1", null, "3"});
        List<String[]> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        // 空字符串回环
        assertArrayEquals(new String[]{"", "middle", ""}, readRows.get(0));
        assertArrayEquals(new String[]{"1", "", "3"}, readRows.get(1));
        // null 写出为空字符串
        assertArrayEquals(new String[]{"1", "", "3"}, readRows.get(2));
    }

    @Test
    public void testHeadersRoundTrip() throws Exception {
        File file = folder.newFile("headers.csv");
        CsvArrayWriter writer = new CsvArrayWriter(file);
        writer.headers("id", "name");
        writer.addRow(new String[]{"1", "tom"});
        writer.close();
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        reader.read();
        reader.close();
        List<String[]> readRows = new ArrayList<>(reader.getRows());
        assertEquals(2, readRows.size());
        assertArrayEquals(new String[]{"id", "name"}, readRows.get(0));
        assertArrayEquals(new String[]{"1", "tom"}, readRows.get(1));
    }

}
