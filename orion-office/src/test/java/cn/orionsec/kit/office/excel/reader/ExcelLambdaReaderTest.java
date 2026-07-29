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
import cn.orionsec.kit.office.excel.writer.ExcelLambdaWriter;
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
 * ExcelLambdaWriter 写入 ExcelLambdaReader 读取 回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelLambdaReaderTest {

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
        this.file = folder.newFile("lambda-round-trip.xlsx");
        this.users = new ArrayList<>();
        users.add(createUser(1L, "张三", "123.456", "2023-08-15 12:30:45", "备注一"));
        users.add(createUser(2L, "李四", "0.01", "2023-09-01 08:00:00", "remark"));
        users.add(createUser(3L, "王五五", "999999.99", "2024-01-31 23:59:59", "备注三"));
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelLambdaWriter<RoundTripUser> writer = build.createLambdaWriter("lambda");
        writer.option(0, RoundTripUser::getId, ExcelFieldType.NUMBER)
                .option(1, RoundTripUser::getName, ExcelFieldType.TEXT)
                .option(2, RoundTripUser::getBalance, ExcelFieldType.NUMBER)
                .option(3, RoundTripUser::getDate, ExcelFieldType.DATE)
                .option(4, RoundTripUser::getRemark, ExcelFieldType.TEXT)
                .headers("ID", "名称", "余额", "时间", "备注")
                .addRows(users);
        build.write(file);
        build.close();
    }

    @Test
    public void testLambdaRoundTrip() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelLambdaReader<RoundTripUser> reader = ExcelLambdaReader.create(workbook, workbook.getSheetAt(0), RoundTripUser::new);
        reader.option(0, ExcelReadType.LONG, RoundTripUser::setId)
                .option(1, ExcelReadType.TEXT, RoundTripUser::setName)
                .option(2, ExcelReadType.DECIMAL, RoundTripUser::setBalance)
                .option(3, ExcelReadType.DATE, RoundTripUser::setDate)
                .option(4, ExcelReadType.TEXT, RoundTripUser::setRemark);
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
        reader.close();
    }

    @Test
    public void testLambdaConvert() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelLambdaReader<RoundTripUser> reader = ExcelLambdaReader.create(workbook, workbook.getSheetAt(0), RoundTripUser::new);
        // 使用 convert 转换读取值
        reader.option(1, ExcelReadType.TEXT, (String name) -> "N-" + name, RoundTripUser::setName)
                .option(0, ExcelReadType.LONG, RoundTripUser::setId);
        reader.init().skip().read();
        List<RoundTripUser> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals("N-张三", rows.get(0).getName());
        Assert.assertEquals("N-王五五", rows.get(2).getName());
        Assert.assertEquals(Long.valueOf(2L), rows.get(1).getId());
        reader.close();
    }

    @Test
    public void testLambdaConsumer() {
        Workbook workbook = Excels.openWorkbook(file);
        List<String> names = new ArrayList<>();
        ExcelLambdaReader<RoundTripUser> reader = ExcelLambdaReader.create(workbook, workbook.getSheetAt(0),
                user -> names.add(user.getName()), RoundTripUser::new);
        reader.option(1, ExcelReadType.TEXT, RoundTripUser::setName);
        reader.init().skip().read();
        Assert.assertEquals(3, names.size());
        Assert.assertEquals("张三", names.get(0));
        Assert.assertEquals("王五五", names.get(2));
        reader.close();
    }

}
