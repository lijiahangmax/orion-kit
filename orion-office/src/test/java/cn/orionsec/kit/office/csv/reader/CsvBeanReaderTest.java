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
import cn.orionsec.kit.office.csv.writer.CsvBeanWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * CsvBeanReader 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvBeanReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入测试文件
     */
    private File writeFile(String name, List<RoundTripUser> rows) throws Exception {
        File file = folder.newFile(name);
        CsvBeanWriter<RoundTripUser> writer = new CsvBeanWriter<>(file, RoundTripUser.class);
        writer.addRows(rows);
        writer.close();
        return file;
    }

    @Test
    public void testBeanRoundTrip() throws Exception {
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester"));
        File file = this.writeFile("bean-read.csv", rows);
        CsvBeanReader<RoundTripUser> reader = new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class);
        reader.read();
        reader.close();
        List<RoundTripUser> readRows = new ArrayList<>(reader.getRows());
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getId(), readRows.get(i).getId());
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

    @Test
    public void testBeanSkipRoundTrip() throws Exception {
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester"),
                new RoundTripUser(3, "spike", "manager"));
        File file = this.writeFile("bean-skip.csv", rows);
        CsvBeanReader<RoundTripUser> reader = new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class);
        reader.skip(2);
        reader.read();
        reader.close();
        List<RoundTripUser> readRows = new ArrayList<>(reader.getRows());
        assertEquals(1, readRows.size());
        assertEquals(Integer.valueOf(3), readRows.get(0).getId());
        assertEquals("spike", readRows.get(0).getName());
    }

    @Test
    public void testBeanConsumerRoundTrip() throws Exception {
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester"));
        File file = this.writeFile("bean-consumer.csv", rows);
        List<RoundTripUser> collected = new ArrayList<>();
        CsvBeanReader<RoundTripUser> reader = new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class, collected::add);
        reader.read();
        reader.close();
        assertEquals(2, collected.size());
        assertEquals(Integer.valueOf(1), collected.get(0).getId());
        assertEquals("jerry", collected.get(1).getName());
    }

    @Test
    public void testBeanSpecialCharacterRoundTrip() throws Exception {
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "a,b\"c\"", "line1\nline2"),
                new RoundTripUser(2, "中文，名字", "备注\"引号\""));
        File file = this.writeFile("bean-special-read.csv", rows);
        CsvBeanReader<RoundTripUser> reader = new CsvBeanReader<>(new CsvReader(file), RoundTripUser.class);
        reader.read();
        reader.close();
        List<RoundTripUser> readRows = new ArrayList<>(reader.getRows());
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

}
