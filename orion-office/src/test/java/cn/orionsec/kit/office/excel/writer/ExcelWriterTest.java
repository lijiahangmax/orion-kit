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
package cn.orionsec.kit.office.excel.writer;

import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.reader.ExcelArrayReader;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import org.apache.poi.ss.usermodel.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * excel writer 写入功能测试 (标题 合并 多sheet 默认值 样式)
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelWriterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testTitleAndMerge() throws Exception {
        File file = folder.newFile("title-merge.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelArrayWriter<Object> writer = build.createArrayWriter("title");
        // 需要先设置 option 再设置标题 否则合并区域只有一格
        writer.option(0, 0, ExcelFieldType.TEXT)
                .option(1, 1, ExcelFieldType.TEXT)
                .option(2, 2, ExcelFieldType.TEXT);
        writer.title("汇总标题");
        writer.headers("列一", "列二", "列三");
        writer.addRow(new Object[]{"a1", "中文值", "c1"})
                .addRow(new Object[]{"a2", "b2", "c2"});
        // 手动合并数据行下方一行
        writer.merge(4, 0, 2);
        build.write(file);
        build.close();

        Workbook workbook = Excels.openWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        // 标题合并区 + 手动合并区
        Assert.assertEquals(2, sheet.getNumMergedRegions());
        Assert.assertEquals("汇总标题", Excels.getCellValue(sheet.getRow(0).getCell(0)));
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, sheet);
        // 跳过标题行和表头行
        reader.skip(2).read(2);
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(2, rows.size());
        Assert.assertArrayEquals(new String[]{"a1", "中文值", "c1"}, rows.get(0));
        Assert.assertArrayEquals(new String[]{"a2", "b2", "c2"}, rows.get(1));
        reader.close();
    }

    @Test
    public void testMultiSheet() throws Exception {
        File file = folder.newFile("multi-sheet.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelArrayWriter<Object> writer1 = build.createArrayWriter("第一页");
        writer1.option(0, 0, ExcelFieldType.TEXT).addRow(new Object[]{"sheet1-value"});
        ExcelArrayWriter<Object> writer2 = build.createArrayWriter("第二页");
        writer2.option(0, 0, ExcelFieldType.TEXT).addRow(new Object[]{"sheet2-值"});
        build.write(file);
        build.close();

        Workbook workbook = Excels.openWorkbook(file);
        Assert.assertEquals(2, workbook.getNumberOfSheets());
        Assert.assertEquals("第一页", workbook.getSheetAt(0).getSheetName());
        Assert.assertEquals("第二页", workbook.getSheetAt(1).getSheetName());
        ExcelArrayReader reader1 = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader1.read();
        Assert.assertEquals("sheet1-value", reader1.getRows().get(0)[0]);
        ExcelArrayReader reader2 = ExcelArrayReader.create(workbook, workbook.getSheetAt(1));
        reader2.read();
        Assert.assertEquals("sheet2-值", reader2.getRows().get(0)[0]);
        // 关闭 workbook
        reader1.close();
    }

    @Test
    public void testDefaultValueAndSkipNullRows() throws Exception {
        File file = folder.newFile("default-value.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelMapWriter<String, Object> writer = build.createMapWriter("default");
        writer.option(0, "id", ExcelFieldType.NUMBER)
                .option(1, "name", ExcelFieldType.TEXT, (Object) "无名");
        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", 1);
        row1.put("name", "有名");
        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", 2);
        // name 缺失 命中默认值
        writer.addRow(row1)
                // null 行默认跳过
                .addRow(null)
                .addRow(row2);
        build.write(file);
        build.close();

        Workbook workbook = Excels.openWorkbook(file);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(2, rows.size());
        Assert.assertArrayEquals(new String[]{"1", "有名"}, rows.get(0));
        Assert.assertArrayEquals(new String[]{"2", "无名"}, rows.get(1));
        reader.close();
    }

    @Test
    public void testStyleAndIndex() throws Exception {
        File file = folder.newFile("style-index.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelArrayWriter<Object> writer = build.createArrayWriter("style");
        CellStyle style = writer.createCellStyle();
        Font font = writer.createFont();
        DataFormat format = writer.createFormat();
        Assert.assertNotNull(style);
        Assert.assertNotNull(font);
        Assert.assertNotNull(format);
        writer.option(0, 0, ExcelFieldType.TEXT)
                .option(1, 1, ExcelFieldType.TEXT)
                .option(2, 2, ExcelFieldType.TEXT);
        Assert.assertEquals(2, writer.getColumnMaxIndex());
        Assert.assertEquals(0, writer.getRowIndex());
        writer.headers("h1", "h2", "h3");
        writer.addRow(new Object[]{"a", "b", "c"});
        Assert.assertEquals(2, writer.getRowIndex());
        build.write(file);
        build.close();
        Assert.assertTrue(file.length() > 0);
    }

}
