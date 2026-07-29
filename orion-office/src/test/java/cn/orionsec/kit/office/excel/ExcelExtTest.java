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
package cn.orionsec.kit.office.excel;

import cn.orionsec.kit.lang.define.collect.MutableMap;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.excel.reader.ExcelArrayReader;
import cn.orionsec.kit.office.excel.reader.ExcelBeanReader;
import cn.orionsec.kit.office.excel.reader.ExcelLambdaReader;
import cn.orionsec.kit.office.excel.reader.ExcelMapReader;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import cn.orionsec.kit.office.excel.writer.ExcelBeanWriter;
import cn.orionsec.kit.office.excel.writer.ExcelWriterBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelExt 提取器测试 (普通读取 + 流式读取)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelExtTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File file;

    private List<RoundTripUser> users;

    private static RoundTripUser createUser(long id, String name, String balance, String date, String remark) {
        RoundTripUser user = new RoundTripUser();
        user.setId(id);
        user.setName(name);
        user.setBalance(new BigDecimal(balance));
        user.setDate(Dates.parse(date, "yyyy-MM-dd HH:mm:ss"));
        user.setRemark(remark);
        return user;
    }

    @Before
    public void prepareFile() throws Exception {
        this.file = folder.newFile("ext-read.xlsx");
        this.users = new ArrayList<>();
        users.add(createUser(1L, "张三", "123.456", "2023-08-15 12:30:45", "备注一"));
        users.add(createUser(2L, "李四", "0.01", "2023-09-01 08:00:00", "remark"));
        users.add(createUser(3L, "王五五", "999999.99", "2024-01-31 23:59:59", "备注三"));
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelBeanWriter<RoundTripUser> writer = build.createBeanWriter("users", RoundTripUser.class);
        writer.option(0, "id", ExcelFieldType.NUMBER)
                .option(1, "name", ExcelFieldType.TEXT)
                .option(2, "balance", ExcelFieldType.NUMBER)
                .option(3, "date", ExcelFieldType.DATE)
                .option(4, "remark", ExcelFieldType.TEXT)
                .headers("ID", "名称", "余额", "时间", "备注")
                .addRows(users);
        build.write(file);
        build.close();
    }

    @Test
    public void testExtArrayReader() {
        ExcelExt ext = new ExcelExt(file);
        Assert.assertFalse(ext.isStreaming());
        ExcelArrayReader reader = ext.arrayReader(0);
        reader.skip().read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals("1", rows.get(0)[0]);
        Assert.assertEquals("张三", rows.get(0)[1]);
        Assert.assertEquals("王五五", rows.get(2)[1]);
        ext.close();
    }

    @Test
    public void testExtStreamingBeanReader() {
        // 流式读取
        ExcelExt ext = new ExcelExt(file, true);
        Assert.assertTrue(ext.isStreaming());
        Assert.assertTrue(Excels.isStreamingWorkbook(ext.getWorkbook()));
        ExcelBeanReader<RoundTripUser> reader = ext.beanReader(0, RoundTripUser.class);
        reader.init().skip().read();
        List<RoundTripUser> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        for (int i = 0; i < 3; i++) {
            RoundTripUser expect = users.get(i);
            RoundTripUser actual = rows.get(i);
            Assert.assertEquals(expect.getId(), actual.getId());
            Assert.assertEquals(expect.getName(), actual.getName());
            Assert.assertEquals(0, expect.getBalance().compareTo(actual.getBalance()));
            Assert.assertEquals(Dates.format(expect.getDate()), Dates.format(actual.getDate()));
            Assert.assertEquals(expect.getRemark(), actual.getRemark());
        }
        ext.close();
    }

    @Test
    public void testExtMapReader() {
        ExcelExt ext = new ExcelExt(file.getAbsolutePath());
        ExcelMapReader<String, Object> reader = ext.mapReader(0);
        reader.option(0, "id", ExcelReadType.LONG)
                .option(1, "name", ExcelReadType.TEXT);
        reader.init().skip().read();
        List<MutableMap<String, Object>> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(1L, rows.get(0).get("id"));
        Assert.assertEquals("李四", rows.get(1).get("name"));
        ext.close();
    }

    @Test
    public void testExtLambdaReader() {
        // 按 sheet 名称获取流式 lambda 读取器
        ExcelExt ext = new ExcelExt(file, true);
        ExcelLambdaReader<RoundTripUser> reader = ext.lambdaReader("users", RoundTripUser::new);
        reader.option(0, ExcelReadType.LONG, RoundTripUser::setId)
                .option(1, ExcelReadType.TEXT, RoundTripUser::setName)
                .option(2, ExcelReadType.DECIMAL, RoundTripUser::setBalance);
        reader.init().skip().read();
        List<RoundTripUser> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(Long.valueOf(3L), rows.get(2).getId());
        Assert.assertEquals("王五五", rows.get(2).getName());
        Assert.assertEquals(0, new BigDecimal("999999.99").compareTo(rows.get(2).getBalance()));
        ext.close();
    }

}
