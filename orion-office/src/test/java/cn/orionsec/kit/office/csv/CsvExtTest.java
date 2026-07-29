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
package cn.orionsec.kit.office.csv;

import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.office.csv.reader.*;
import cn.orionsec.kit.office.csv.writer.CsvArrayWriter;
import cn.orionsec.kit.office.csv.writer.CsvBeanWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * CsvExt 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvExtTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入 array 测试文件
     */
    private File writeArrayFile(String name, String[]... rows) throws Exception {
        File file = folder.newFile(name);
        CsvArrayWriter writer = new CsvArrayWriter(file);
        for (String[] row : rows) {
            writer.addRow(row);
        }
        writer.close();
        return file;
    }

    @Test
    public void testCreateFromFileArrayRoundTrip() throws Exception {
        File file = this.writeArrayFile("ext-file.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"});
        CsvExt ext = new CsvExt(file);
        CsvArrayReader reader = ext.arrayReader();
        reader.read();
        ext.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "tom"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "jerry"}, rows.get(1));
    }

    @Test
    public void testCreateFromStreamRoundTrip() throws Exception {
        File file = this.writeArrayFile("ext-stream.csv",
                new String[]{"1", "中文"},
                new String[]{"2", "b,c"});
        try (FileInputStream in = new FileInputStream(file)) {
            CsvExt ext = new CsvExt(in);
            CsvArrayReader reader = ext.arrayReader();
            reader.read();
            ext.close();
            List<String[]> rows = new ArrayList<>(reader.getRows());
            assertEquals(2, rows.size());
            assertArrayEquals(new String[]{"1", "中文"}, rows.get(0));
            assertArrayEquals(new String[]{"2", "b,c"}, rows.get(1));
        }
    }

    @Test
    public void testParseStringRoundTrip() {
        CsvExt ext = CsvExt.parse("1,tom\n2,jerry");
        CsvArrayReader reader = ext.arrayReader();
        reader.read();
        ext.close();
        List<String[]> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "tom"}, rows.get(0));
        assertArrayEquals(new String[]{"2", "jerry"}, rows.get(1));
    }

    @Test
    public void testMapReaderRoundTrip() throws Exception {
        File file = this.writeArrayFile("ext-map.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"});
        CsvExt ext = new CsvExt(file);
        CsvMapReader<String, String> reader = ext.mapReader();
        reader.mapping(0, "id").mapping(1, "name");
        reader.read();
        ext.close();
        List<MutableMap<String, String>> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertEquals("1", rows.get(0).get("id"));
        assertEquals("tom", rows.get(0).get("name"));
        assertEquals("2", rows.get(1).get("id"));
        assertEquals("jerry", rows.get(1).get("name"));
    }

    @Test
    public void testBeanReaderRoundTrip() throws Exception {
        File file = folder.newFile("ext-bean.csv");
        CsvBeanWriter<RoundTripUser> writer = new CsvBeanWriter<>(file, RoundTripUser.class);
        writer.addRows(Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester")));
        writer.close();
        CsvExt ext = new CsvExt(file);
        CsvBeanReader<RoundTripUser> reader = ext.beanReader(RoundTripUser.class);
        reader.read();
        ext.close();
        List<RoundTripUser> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertEquals(Integer.valueOf(1), rows.get(0).getId());
        assertEquals("tom", rows.get(0).getName());
        assertEquals("developer", rows.get(0).getRemark());
        assertEquals(Integer.valueOf(2), rows.get(1).getId());
    }

    @Test
    public void testLambdaReaderRoundTrip() throws Exception {
        File file = this.writeArrayFile("ext-lambda.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"});
        CsvExt ext = new CsvExt(file);
        CsvLambdaReader<RoundTripUser> reader = ext.lambdaReader(RoundTripUser::new);
        reader.mapping(0, Integer::valueOf, RoundTripUser::setId)
                .mapping(1, RoundTripUser::setName);
        reader.read();
        ext.close();
        List<RoundTripUser> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertEquals(Integer.valueOf(1), rows.get(0).getId());
        assertEquals("tom", rows.get(0).getName());
        assertEquals(Integer.valueOf(2), rows.get(1).getId());
        assertEquals("jerry", rows.get(1).getName());
    }

    @Test
    public void testRawReaderRoundTrip() throws Exception {
        File file = this.writeArrayFile("ext-raw.csv",
                new String[]{"1", "tom"},
                new String[]{"2", "jerry"});
        CsvExt ext = new CsvExt(file);
        CsvRawReader reader = ext.rawReader();
        reader.read();
        ext.close();
        List<String> lines = new ArrayList<>(reader.getRows());
        assertEquals(2, lines.size());
        assertEquals("1,tom", lines.get(0));
        assertEquals("2,jerry", lines.get(1));
    }

}
