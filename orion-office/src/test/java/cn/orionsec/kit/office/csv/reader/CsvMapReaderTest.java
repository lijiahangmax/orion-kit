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

import cn.orionsec.kit.lang.define.collect.MutableLinkedHashMap;
import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.writer.CsvMapWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * CsvMapReader 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvMapReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入测试文件 两列 id name
     */
    private File writeFile(String name) throws Exception {
        File file = folder.newFile(name);
        CsvMapWriter<String, String> writer = new CsvMapWriter<>(file);
        writer.mapping(0, "id").mapping(1, "name");
        Map<String, String> row1 = new LinkedHashMap<>();
        row1.put("id", "1");
        row1.put("name", "tom");
        Map<String, String> row2 = new LinkedHashMap<>();
        row2.put("id", "2");
        row2.put("name", "jerry");
        writer.addRow(row1);
        writer.addRow(row2);
        writer.close();
        return file;
    }

    @Test
    public void testMapRoundTrip() throws Exception {
        File file = this.writeFile("map-read.csv");
        CsvMapReader<String, String> reader = new CsvMapReader<>(new CsvReader(file));
        reader.mapping(0, "id").mapping(1, "name");
        reader.read();
        reader.close();
        List<MutableMap<String, String>> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertEquals("1", rows.get(0).get("id"));
        assertEquals("tom", rows.get(0).get("name"));
        assertEquals("2", rows.get(1).get("id"));
        assertEquals("jerry", rows.get(1).get("name"));
    }

    @Test
    public void testMapLinkedRoundTrip() throws Exception {
        File file = this.writeFile("map-linked.csv");
        CsvMapReader<String, String> reader = new CsvMapReader<>(new CsvReader(file));
        reader.linked().mapping(0, "id").mapping(1, "name");
        reader.read();
        reader.close();
        List<MutableMap<String, String>> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        // linked 保持插入顺序
        assertTrue(rows.get(0) instanceof MutableLinkedHashMap);
        List<String> keys = new ArrayList<>(rows.get(0).keySet());
        assertEquals("id", keys.get(0));
        assertEquals("name", keys.get(1));
    }

    @Test
    public void testMapDefaultValueRoundTrip() throws Exception {
        File file = this.writeFile("map-default-read.csv");
        CsvMapReader<String, String> reader = new CsvMapReader<>(new CsvReader(file));
        // 越界列使用默认值
        reader.mapping(0, "id").mapping(1, "name").mapping(5, "extra", "def");
        reader.read();
        reader.close();
        List<MutableMap<String, String>> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertEquals("def", rows.get(0).get("extra"));
        assertEquals("def", rows.get(1).get("extra"));
    }

    @Test
    public void testMapNullPutKeyRoundTrip() throws Exception {
        File file = this.writeFile("map-null-key.csv");
        CsvMapReader<String, String> reader = new CsvMapReader<>(new CsvReader(file));
        // 越界列无默认值 且不插入 key
        reader.nullPutKey(false);
        reader.mapping(0, "id").mapping(6, "none");
        reader.read();
        reader.close();
        List<MutableMap<String, String>> rows = new ArrayList<>(reader.getRows());
        assertEquals(2, rows.size());
        assertFalse(rows.get(0).containsKey("none"));
        // 插入 key
        CsvMapReader<String, String> reader2 = new CsvMapReader<>(new CsvReader(file));
        reader2.nullPutKey(true);
        reader2.mapping(0, "id").mapping(6, "none");
        reader2.read();
        reader2.close();
        List<MutableMap<String, String>> rows2 = new ArrayList<>(reader2.getRows());
        assertTrue(rows2.get(0).containsKey("none"));
        assertNull(rows2.get(0).get("none"));
    }

}
