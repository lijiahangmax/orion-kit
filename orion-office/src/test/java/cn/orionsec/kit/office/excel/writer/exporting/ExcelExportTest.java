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
package cn.orionsec.kit.office.excel.writer.exporting;

import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.RoundTripUser;
import cn.orionsec.kit.office.excel.reader.ExcelArrayReader;
import cn.orionsec.kit.office.excel.reader.ExcelBeanReader;
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
 * ExcelExport 注解导出 ExcelBeanReader 读取 回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelExportTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

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
    public void prepareData() {
        this.users = new ArrayList<>();
        users.add(createUser(1L, "张三", "123.456", "2023-08-15 12:30:45", "备注一"));
        users.add(createUser(2L, "李四", "0.01", "2023-09-01 08:00:00", null));
        users.add(createUser(3L, "王五五", "999999.99", "2024-01-31 23:59:59", "remark"));
    }

    @Test
    public void testExportRoundTrip() throws Exception {
        File file = folder.newFile("export-round-trip.xlsx");
        ExcelExport<RoundTripUser> export = ExcelExport.create(RoundTripUser.class);
        export.addRows(users);
        Assert.assertEquals(3, export.getRows());
        Assert.assertEquals(4, export.getColumnMaxIndex());
        export.write(file);
        export.close();

        Workbook workbook = Excels.openWorkbook(file);
        // 注解定义的 sheet 名称
        Assert.assertEquals("users", workbook.getSheetAt(0).getSheetName());
        ExcelBeanReader<RoundTripUser> reader = ExcelBeanReader.create(workbook, workbook.getSheetAt(0), RoundTripUser.class);
        // 跳过表头行
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
        Assert.assertEquals("备注一", rows.get(0).getRemark());
        Assert.assertEquals("remark", rows.get(2).getRemark());
        reader.close();
    }

    @Test
    public void testExportHeaderRow() throws Exception {
        File file = folder.newFile("export-header.xlsx");
        ExcelExport<RoundTripUser> export = ExcelExport.create(RoundTripUser.class);
        export.addRows(users);
        export.write(file);
        export.close();

        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.read(1);
        // 注解定义的表头
        Assert.assertArrayEquals(new String[]{"ID", "名称", "余额", "时间", "备注"}, reader.getRows().get(0));
        // 总行数 = 1 表头 + 3 数据
        Assert.assertEquals(4, reader.getLines());
        reader.close();
    }

    @Test
    public void testExportRenameSheet() throws Exception {
        File file = folder.newFile("export-rename.xlsx");
        // 重命名需要在 init 之前设置
        ExcelExport<RoundTripUser> export = new ExcelExport<>(RoundTripUser.class);
        export.sheet("重命名页");
        export.init();
        export.addRow(users.get(0));
        export.write(file);
        export.close();

        Workbook workbook = Excels.openWorkbook(file);
        Assert.assertEquals("重命名页", workbook.getSheetAt(0).getSheetName());
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip().read();
        Assert.assertEquals(1, reader.getRows().size());
        Assert.assertEquals("张三", reader.getRows().get(0)[1]);
        reader.close();
    }

}
