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

import cn.orionsec.kit.office.csv.RoundTripUser;
import cn.orionsec.kit.office.csv.annotation.ExportField;
import cn.orionsec.kit.office.csv.annotation.ImportField;
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvBeanReader;
import cn.orionsec.kit.office.csv.reader.CsvRawReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * CsvBeanWriter 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvBeanWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入后读取
     */
    private List<RoundTripUser> writeThenRead(File file, List<RoundTripUser> rows) {
        CsvBeanWriter<RoundTripUser> writer = new CsvBeanWriter<>(file, RoundTripUser.class);
        writer.addRows(rows);
        writer.flush();
        writer.close();
        CsvBeanReader<RoundTripUser> reader = new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class);
        reader.read();
        reader.close();
        return new ArrayList<>(reader.getRows());
    }

    @Test
    public void testBeanRoundTrip() throws Exception {
        File file = folder.newFile("bean.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester"));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getId(), readRows.get(i).getId());
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

    @Test
    public void testBeanSpecialCharacterRoundTrip() throws Exception {
        File file = folder.newFile("bean-special.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "a,b\"c\"", "line1\nline2"),
                new RoundTripUser(2, "中文，名字", "备注\"引号\""));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getId(), readRows.get(i).getId());
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

    @Test
    public void testBeanNullValueRoundTrip() throws Exception {
        File file = folder.newFile("bean-null.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", null),
                new RoundTripUser(2, null, "remark"));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        // null 写出为空字符串 读回为空字符串
        assertEquals(Integer.valueOf(1), readRows.get(0).getId());
        assertEquals("tom", readRows.get(0).getName());
        assertEquals("", readRows.get(0).getRemark());
        assertEquals(Integer.valueOf(2), readRows.get(1).getId());
        assertEquals("", readRows.get(1).getName());
        assertEquals("remark", readRows.get(1).getRemark());
    }

    @Test
    public void testBeanHeaderRoundTrip() throws Exception {
        File file = folder.newFile("bean-header.csv");
        CsvBeanWriter<HeaderUser> writer = new CsvBeanWriter<>(file, HeaderUser.class);
        writer.addRow(new HeaderUser(1, "orion"));
        writer.close();
        // 验证注解表头
        CsvRawReader rawReader = new CsvRawReader(new CsvReader(file));
        rawReader.read();
        rawReader.close();
        List<String> lines = new ArrayList<>(rawReader.getRows());
        assertEquals(2, lines.size());
        assertEquals("编号,名称", lines.get(0));
        // 跳过表头读取数据
        CsvBeanReader<HeaderUser> reader = new CsvBeanReader<>(new CsvReader(file), HeaderUser.class);
        reader.skip(1);
        reader.read();
        reader.close();
        List<HeaderUser> readRows = new ArrayList<>(reader.getRows());
        assertEquals(1, readRows.size());
        assertEquals(Integer.valueOf(1), readRows.get(0).getId());
        assertEquals("orion", readRows.get(0).getName());
    }

    /**
     * 带表头的测试 bean
     */
    public static class HeaderUser {

        @ExportField(value = 0, header = "编号")
        @ImportField(0)
        private Integer id;

        @ExportField(value = 1, header = "名称")
        @ImportField(1)
        private String name;

        public HeaderUser() {
        }

        public HeaderUser(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}
