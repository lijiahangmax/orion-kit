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

import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import cn.orionsec.kit.office.excel.writer.ExcelArrayWriter;
import cn.orionsec.kit.office.excel.writer.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * 流式读取测试
 * <p>
 * 普通 writer 写入大数据量后使用流式 workbook 读取
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelStreamingReaderTest {

    private static final int ROWS = 1000;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File file;

    @Before
    public void prepareFile() throws Exception {
        this.file = folder.newFile("streaming-read.xlsx");
        ExcelWriterBuilder build = new ExcelWriterBuilder();
        ExcelArrayWriter<Object> writer = build.createArrayWriter("stream");
        writer.option(0, 0, ExcelFieldType.NUMBER)
                .option(1, 1, ExcelFieldType.TEXT)
                .option(2, 2, ExcelFieldType.NUMBER)
                .option(3, 3, ExcelFieldType.TEXT)
                .headers("id", "名称", "余额", "时间");
        for (int i = 1; i <= ROWS; i++) {
            writer.addRow(new Object[]{i, "用户" + i, new BigDecimal(i + ".25"), "2023-08-15 12:30:45"});
        }
        build.write(file);
        build.close();
    }

    @Test
    public void testStreamingWorkbookCheck() {
        Workbook workbook = Excels.openStreamingWorkbook(Files1.openInputStreamSafe(file), 100, 4096);
        Assert.assertTrue(Excels.isStreamingWorkbook(workbook));
        Assert.assertTrue(Excels.isStreamingWorkbook(workbook.getClass()));
        Sheet sheet = workbook.getSheetAt(0);
        Assert.assertTrue(Excels.isStreamingSheet(sheet));
        Assert.assertTrue(Excels.isStreamingSheet(sheet.getClass()));
        Assert.assertEquals("stream", sheet.getSheetName());
        Streams.close(workbook);
        // 普通 workbook 不是流式
        Workbook xssf = new XSSFWorkbook();
        Assert.assertFalse(Excels.isStreamingWorkbook(xssf));
        Assert.assertFalse(Excels.isStreamingWorkbook(XSSFWorkbook.class));
        Assert.assertFalse(Excels.isStreamingSheet(xssf.createSheet().getClass()));
        Streams.close(xssf);
    }

    @Test
    public void testStreamingArrayRead() {
        Workbook workbook = Excels.openStreamingWorkbook(Files1.openInputStreamSafe(file), 100, 4096);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip().read();
        List<String[]> rows = reader.getRows();
        Assert.assertEquals(ROWS, rows.size());
        Assert.assertEquals(ROWS, reader.getRowNum());
        Assert.assertArrayEquals(new String[]{"1", "用户1", "1.25", "2023-08-15 12:30:45"}, rows.get(0));
        Assert.assertArrayEquals(new String[]{"500", "用户500", "500.25", "2023-08-15 12:30:45"}, rows.get(499));
        Assert.assertArrayEquals(new String[]{"1000", "用户1000", "1000.25", "2023-08-15 12:30:45"}, rows.get(ROWS - 1));
        reader.close();
    }

    @Test
    public void testStreamingHeaderRow() {
        Workbook workbook = Excels.openStreamingWorkbook(file, 100);
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.read(1);
        Assert.assertEquals(1, reader.getRows().size());
        Assert.assertArrayEquals(new String[]{"id", "名称", "余额", "时间"}, reader.getRows().get(0));
        reader.close();
    }

    @Test
    public void testStreamingConsumerRead() {
        Workbook workbook = Excels.openStreamingWorkbook(file);
        List<String> names = new ArrayList<>();
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0), row -> names.add(row[1]));
        reader.skip().read();
        Assert.assertEquals(ROWS, names.size());
        Assert.assertEquals("用户1", names.get(0));
        Assert.assertEquals("用户1000", names.get(ROWS - 1));
        reader.close();
    }

    @Test
    public void testStreamingPartialRead() {
        Workbook workbook = Excels.openStreamingWorkbook(file.getAbsolutePath());
        ExcelArrayReader reader = ExcelArrayReader.create(workbook, workbook.getSheetAt(0));
        reader.skip().read(10);
        Assert.assertEquals(10, reader.getRows().size());
        Assert.assertArrayEquals(new String[]{"10", "用户10", "10.25", "2023-08-15 12:30:45"}, reader.getRows().get(9));
        // 继续读取剩余行
        reader.read();
        Assert.assertEquals(ROWS, reader.getRows().size());
        reader.close();
    }

}
