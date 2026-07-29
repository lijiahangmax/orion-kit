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

import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvMapReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * CsvMapWriter 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvMapWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Map<String, String> buildRow(String id, String name, String remark) {
        Map<String, String> row = new LinkedHashMap<>();
        row.put("id", id);
        row.put("name", name);
        row.put("remark", remark);
        return row;
    }

    private List<MutableMap<String, String>> readRows(File file) {
        CsvMapReader<String, String> reader = new CsvMapReader<>(new CsvReader(file));
        reader.linked()
                .mapping(0, "id")
                .mapping(1, "name")
                .mapping(2, "remark");
        reader.read();
        reader.close();
        return new ArrayList<>(reader.getRows());
    }

    @Test
    public void testMapRoundTrip() throws Exception {
        File file = folder.newFile("map.csv");
        CsvMapWriter<String, String> writer = new CsvMapWriter<>(file);
        writer.mapping(0, "id")
                .mapping(1, "name")
                .mapping(2, "remark");
        writer.addRow(this.buildRow("1", "tom", "developer"));
        writer.addRow(this.buildRow("2", "jerry", "tester"));
        writer.flush();
        writer.close();
        List<MutableMap<String, String>> readRows = this.readRows(file);
        assertEquals(2, readRows.size());
        assertEquals("1", readRows.get(0).get("id"));
        assertEquals("tom", readRows.get(0).get("name"));
        assertEquals("developer", readRows.get(0).get("remark"));
        assertEquals("2", readRows.get(1).get("id"));
        assertEquals("jerry", readRows.get(1).get("name"));
        assertEquals("tester", readRows.get(1).get("remark"));
    }

    @Test
    public void testMapSpecialCharacterRoundTrip() throws Exception {
        File file = folder.newFile("map-special.csv");
        CsvMapWriter<String, String> writer = new CsvMapWriter<>(file);
        writer.mapping(0, "id")
                .mapping(1, "name")
                .mapping(2, "remark");
        writer.addRow(this.buildRow("1", "a,b\"c\"", "line1\nline2"));
        writer.addRow(this.buildRow("2", "中文，键值", "备注\"引号\""));
        writer.close();
        List<MutableMap<String, String>> readRows = this.readRows(file);
        assertEquals(2, readRows.size());
        assertEquals("a,b\"c\"", readRows.get(0).get("name"));
        assertEquals("line1\nline2", readRows.get(0).get("remark"));
        assertEquals("中文，键值", readRows.get(1).get("name"));
        assertEquals("备注\"引号\"", readRows.get(1).get("remark"));
    }

    @Test
    public void testMapDefaultValueRoundTrip() throws Exception {
        File file = folder.newFile("map-default.csv");
        CsvMapWriter<String, String> writer = new CsvMapWriter<>(file);
        writer.mapping(0, "id")
                .mapping(1, "name")
                .mapping(2, "remark", "无");
        Map<String, String> row = new LinkedHashMap<>();
        row.put("id", "1");
        row.put("name", "tom");
        // 缺失 remark 使用默认值
        writer.addRow(row);
        writer.close();
        List<MutableMap<String, String>> readRows = this.readRows(file);
        assertEquals(1, readRows.size());
        assertEquals("1", readRows.get(0).get("id"));
        assertEquals("tom", readRows.get(0).get("name"));
        assertEquals("无", readRows.get(0).get("remark"));
    }

    @Test
    public void testMapEmptyValueRoundTrip() throws Exception {
        File file = folder.newFile("map-empty.csv");
        CsvMapWriter<String, String> writer = new CsvMapWriter<>(file);
        writer.mapping(0, "id")
                .mapping(1, "name")
                .mapping(2, "remark");
        writer.addRow(this.buildRow("1", "", null));
        writer.close();
        List<MutableMap<String, String>> readRows = this.readRows(file);
        assertEquals(1, readRows.size());
        // 空值与 null 均写出为空字符串
        assertEquals("1", readRows.get(0).get("id"));
        assertEquals("", readRows.get(0).get("name"));
        assertEquals("", readRows.get(0).get("remark"));
    }

}
