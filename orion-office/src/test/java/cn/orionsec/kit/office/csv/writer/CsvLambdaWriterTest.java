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
import cn.orionsec.kit.office.csv.core.CsvReader;
import cn.orionsec.kit.office.csv.reader.CsvLambdaReader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * CsvLambdaWriter 读写回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class CsvLambdaWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    /**
     * 写入后读取
     */
    private List<RoundTripUser> writeThenRead(File file, List<RoundTripUser> rows) {
        CsvLambdaWriter<RoundTripUser> writer = new CsvLambdaWriter<>(file);
        writer.mapping(0, RoundTripUser::getId)
                .mapping(1, RoundTripUser::getName)
                .mapping(2, RoundTripUser::getRemark);
        writer.addRows(rows);
        writer.flush();
        writer.close();
        CsvLambdaReader<RoundTripUser> reader = new CsvLambdaReader<>(new CsvReader(file), RoundTripUser::new);
        reader.mapping(0, Integer::valueOf, RoundTripUser::setId)
                .mapping(1, RoundTripUser::setName)
                .mapping(2, RoundTripUser::setRemark);
        reader.read();
        reader.close();
        return new ArrayList<>(reader.getRows());
    }

    @Test
    public void testLambdaRoundTrip() throws Exception {
        File file = folder.newFile("lambda.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "tom", "developer"),
                new RoundTripUser(2, "jerry", "tester"),
                new RoundTripUser(3, "spike", "manager"));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getId(), readRows.get(i).getId());
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

    @Test
    public void testLambdaSpecialCharacterRoundTrip() throws Exception {
        File file = folder.newFile("lambda-special.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "a,b\"c\"", "line1\nline2"),
                new RoundTripUser(2, "中文，测试", "备注\"引号\""));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        for (int i = 0; i < rows.size(); i++) {
            assertEquals(rows.get(i).getId(), readRows.get(i).getId());
            assertEquals(rows.get(i).getName(), readRows.get(i).getName());
            assertEquals(rows.get(i).getRemark(), readRows.get(i).getRemark());
        }
    }

    @Test
    public void testLambdaEmptyValueRoundTrip() throws Exception {
        File file = folder.newFile("lambda-empty.csv");
        List<RoundTripUser> rows = Arrays.asList(
                new RoundTripUser(1, "", null),
                new RoundTripUser(2, "name", ""));
        List<RoundTripUser> readRows = this.writeThenRead(file, rows);
        assertEquals(rows.size(), readRows.size());
        // 空值与 null 均写出为空字符串
        assertEquals(Integer.valueOf(1), readRows.get(0).getId());
        assertEquals("", readRows.get(0).getName());
        assertEquals("", readRows.get(0).getRemark());
        assertEquals(Integer.valueOf(2), readRows.get(1).getId());
        assertEquals("name", readRows.get(1).getName());
        assertEquals("", readRows.get(1).getRemark());
    }

}
