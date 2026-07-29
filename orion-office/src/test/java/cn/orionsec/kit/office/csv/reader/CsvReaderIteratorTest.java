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

import cn.orionsec.kit.office.csv.RoundTripUser;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * CsvReaderIterator 迭代读取回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvReaderIteratorTest {

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
    public void testIteratorRoundTrip() throws Exception {
        File file = this.writeFile("iterator.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"},
                new String[]{"3", "spike"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        CsvReaderIterator<String[]> iterator = reader.iterator();
        List<String[]> rows = new ArrayList<>();
        while (iterator.hasNext()) {
            rows.add(iterator.next());
        }
        iterator.close();
        assertEquals(3, rows.size());
        assertArrayEquals(new String[]{"1", "tom"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "jerry"}, rows.get(1));
        assertArrayEquals(new String[]{"3", "spike"}, rows.get(2));
    }

    @Test
    public void testIteratorForEachRoundTrip() throws Exception {
        File file = this.writeFile("iterator-foreach.csv",
                new String[]{"1", "a"},
                new String[]{"2", "b"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        List<String[]> rows = new ArrayList<>();
        for (String[] row : reader.iterator()) {
            rows.add(row);
        }
        reader.close();
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "a"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "b"}, rows.get(1));
    }

    @Test
    public void testIteratorNoSuchElement() throws Exception {
        File file = this.writeFile("iterator-end.csv",
                new String[]{"1", "a"});
        CsvArrayReader reader = new CsvArrayReader(new CsvReader(file));
        CsvReaderIterator<String[]> iterator = reader.iterator();
        assertTrue(iterator.hasNext());
        assertArrayEquals(new String[]{"1", "a"}, iterator.next());
        assertFalse(iterator.hasNext());
        // 读取结束后抛出异常
        try {
            iterator.next();
            throw new AssertionError("expect NoSuchElementException");
        } catch (NoSuchElementException e) {
            // ignore
        } finally {
            iterator.close();
        }
    }

    @Test
    public void testIteratorBeanRoundTrip() throws Exception {
        File file = this.writeFile("iterator-bean.csv",
                new String[]{"1", "tom", "developer"},
                new String[]{"2", "jerry", "tester"});
        CsvBeanReader<RoundTripUser> reader =
                new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class);
        CsvReaderIterator<RoundTripUser> iterator = reader.iterator();
        List<RoundTripUser> rows = new ArrayList<>();
        while (iterator.hasNext()) {
            rows.add(iterator.next());
        }
        iterator.close();
        assertEquals(2, rows.size());
        assertEquals(Integer.valueOf(1), rows.get(0).getId());
        assertEquals("tom", rows.get(0).getName());
        assertEquals(Integer.valueOf(2), rows.get(1).getId());
        assertEquals("jerry", rows.get(1).getName());
    }

}
