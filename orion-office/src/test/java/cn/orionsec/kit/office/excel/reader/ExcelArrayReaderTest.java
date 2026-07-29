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

import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.writer.ExcelArrayWriter;
import cn.orionsec.kit.office.excel.writer.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * ExcelArrayWriter 写入 ExcelArrayReader 读取 回环测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelArrayReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File file;

    @Before
    public void prepareFile() throws Exception {
        this.file = folder.newFile("array-round-trip.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelArrayWriter<Object> writer = build.createArrayWriter("array");
        writer.option(0, 0, ExcelFieldType.NUMBER)
                .option(1, 1, ExcelFieldType.TEXT)
                .option(2, 2, ExcelFieldType.NUMBER)
                .option(3, 3, ExcelFieldType.BOOLEAN)
                .option(4, 4, ExcelFieldType.DATE_FORMAT, "yyyy-MM-dd HH:mm:ss")
                .headers("id", "名称", "余额", "启用", "时间");
        writer.addRow(new Object[]{1001, "张三", new BigDecimal("123.456"), true, "2023-08-15 12:30:45"})
                .addRow(new Object[]{1002, "李四", new BigDecimal("0.01"), false, "2023-08-16 00:00:00"})
                .addRow(new Object[]{1003, "王五五", new BigDecimal("999999.99"), true, "2023-08-17 23:59:59"});
        build.write(file);
        build.close();
    }

    @Test
    public void testArrayRoundTrip() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip().read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(3, reader.getRowNum());
        // 总行数 = 1 表头 + 3 数据
        Assert.assertEquals(4, reader.getLines());
        Assert.assertArrayEquals(new String[]{"1001", "张三", "123.456", "true", "2023-08-15 12:30:45"}, rows.get(0));
        Assert.assertArrayEquals(new String[]{"1002", "李四", "0.01", "false", "2023-08-16 00:00:00"}, rows.get(1));
        Assert.assertArrayEquals(new String[]{"1003", "王五五", "999999.99", "true", "2023-08-17 23:59:59"}, rows.get(2));
        reader.close();
    }

    @Test
    public void testReadHeaderRow() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.read(1);
        Assert.assertEquals(1, reader.getRows().size());
        Assert.assertArrayEquals(new String[]{"id", "名称", "余额", "启用", "时间"}, reader.getRows().get(0));
        reader.close();
    }

    @Test
    public void testReadSpecifiedColumns() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.columns(2, 0).skip().read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(2, reader.getColumnSize());
        Assert.assertArrayEquals(new String[]{"123.456", "1001"}, rows.get(0));
        Assert.assertArrayEquals(new String[]{"999999.99", "1003"}, rows.get(2));
        reader.close();
    }

    @Test
    public void testCapacityAndNullColumn() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        // 容量超过实际列数 缺失列返回空串
        reader.capacity(7).columnOfNullToEmpty().skip().read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(3, rows.size());
        Assert.assertEquals(7, rows.get(0).length);
        Assert.assertEquals("", rows.get(0)[5]);
        Assert.assertEquals("", rows.get(0)[6]);
        Assert.assertEquals("1001", rows.get(0)[0]);
        reader.close();
    }

    @Test
    public void testReaderIterator() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip();
        int count = 0;
        for (String[] row : reader) {
            Assert.assertEquals(5, row.length);
            count++;
        }
        Assert.assertEquals(3, count);
        reader.close();
    }

    @Test
    public void testPartialRead() {
        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip().read(2);
        Assert.assertEquals(2, reader.getRows().size());
        // 继续读取剩余行
        reader.read();
        Assert.assertEquals(3, reader.getRows().size());
        // 清空
        reader.clear();
        Assert.assertEquals(0, reader.getRows().size());
        reader.close();
    }

}
