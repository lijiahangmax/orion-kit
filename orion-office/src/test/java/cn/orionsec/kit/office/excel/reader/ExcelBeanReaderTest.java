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
package cn.orionsec.kit.office.excel.reader;

import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.RoundTripUser;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.type.ExcelReadType;
import cn.orionsec.kit.office.excel.writer.ExcelBeanWriter;
import cn.orionsec.kit.office.excel.writer.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.Workbook;
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
 * ExcelBeanWriter 写入 ExcelBeanReader 读取 回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelBeanReaderTest {

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
        this.file = folder.newFile("bean-round-trip.xlsx");
        this.users = new ArrayList<>();
        users.add(createUser(1L, "张三", "123.456", "2023-08-15 12:30:45", "备注一"));
        users.add(createUser(2L, "李四", "0.01", "2023-09-01 08:00:00", null));
        users.add(createUser(3L, "王五五", "999999.99", "2024-01-31 23:59:59", "remark"));
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelBeanWriter<RoundTripUser> writer = build.createBeanWriter("bean", RoundTripUser.class);
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
    public void testBeanRoundTrip() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelBeanReader<RoundTripUser> reader = ExcelBeanReader.create(workbook, workbook.getSheetAt(0), RoundTripUser.class);
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
        }
        // 空备注写入后为空白单元格 读取为空串
        Assert.assertEquals("备注一", rows.get(0).getRemark());
        Assert.assertEquals("", rows.get(1).getRemark());
        Assert.assertEquals("remark", rows.get(2).getRemark());
        reader.close();
    }

    @Test
    public void testBeanReaderOption() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelBeanReader<RoundTripUser> reader = ExcelBeanReader.create(workbook, workbook.getSheetAt(0), RoundTripUser.class);
        // 手动配置覆盖注解 将名称列读取到备注字段
        reader.option(1, "remark", ExcelReadType.TEXT);
        reader.init().skip().read();
        List<RoundTripUser> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals("张三", rows.get(0).getRemark());
        Assert.assertEquals("李四", rows.get(1).getRemark());
        reader.close();
    }

    @Test
    public void testBeanReaderConsumer() {
        Workbook workbook = Excels.openWorkbook(file);
        List<Long> ids = new ArrayList<>();
        ExcelBeanReader<RoundTripUser> reader = ExcelBeanReader.create(workbook, workbook.getSheetAt(0),
                RoundTripUser.class, user -> ids.add(user.getId()));
        reader.init().skip().read();
        Assert.assertEquals(3, ids.size());
        Assert.assertEquals(Long.valueOf(1L), ids.get(0));
        Assert.assertEquals(Long.valueOf(3L), ids.get(2));
        reader.close();
    }

}
