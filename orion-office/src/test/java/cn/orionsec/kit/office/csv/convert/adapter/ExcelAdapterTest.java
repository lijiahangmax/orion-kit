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
package cn.orionsec.kit.office.csv.convert.adapter;

import cn.orionsec.kit.office.csv.CsvExt;
import cn.orionsec.kit.office.csv.reader.CsvArrayReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * {@link ExcelAdapter} csv 转 excel 适配器测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26
 */
public class ExcelAdapterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File createCsv(String name, String content) throws Exception {
        File file = folder.newFile(name);
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
        return file;
    }

    @Test
    public void testConvertToExcel() throws Exception {
        File source = createCsv("source.csv", "1,tom\n2,jerry\n");
        File target = folder.newFile("target.xlsx");
        ExcelAdapter adapter = new ExcelAdapter(new CsvExt(source));
        adapter.forNew().write(target);
        adapter.getReader().close();
        adapter.close();
        try (InputStream in = new FileInputStream(target);
             XSSFWorkbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(1, sheet.getLastRowNum());
            assertEquals("1", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("tom", sheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("2", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("jerry", sheet.getRow(1).getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testConvertWithHeaderAndSkip() throws Exception {
        File source = createCsv("source.csv", "id,name\n1,tom\n2,jerry\n");
        File target = folder.newFile("target.xlsx");
        ExcelAdapter adapter = new ExcelAdapter(new CsvExt(source));
        // 跳过源表头 写入自定义表头
        adapter.skip().header("编号", "名称").bufferLine(1).forNew().write(target);
        adapter.getReader().close();
        adapter.close();
        try (InputStream in = new FileInputStream(target);
             XSSFWorkbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(2, sheet.getLastRowNum());
            assertEquals("编号", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("名称", sheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("1", sheet.getRow(1).getCell(0).getStringCellValue());
            assertEquals("tom", sheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("2", sheet.getRow(2).getCell(0).getStringCellValue());
            assertEquals("jerry", sheet.getRow(2).getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testSkipMultiRows() throws Exception {
        File source = createCsv("source.csv", "h1,h2\nx1,x2\n1,tom\n");
        File target = folder.newFile("target.xlsx");
        ExcelAdapter adapter = new ExcelAdapter(new CsvExt(source));
        adapter.skip(2).forNew().write(target);
        adapter.getReader().close();
        adapter.close();
        try (InputStream in = new FileInputStream(target);
             XSSFWorkbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertEquals(0, sheet.getLastRowNum());
            assertEquals("1", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("tom", sheet.getRow(0).getCell(1).getStringCellValue());
        }
    }

    @Test
    public void testChainAndGetters() throws Exception {
        File source = createCsv("source.csv", "a,b\n");
        CsvArrayReader reader = new CsvExt(source).arrayReader();
        ExcelAdapter adapter = new ExcelAdapter(reader);
        assertSame(adapter, adapter.bufferLine(10));
        assertSame(adapter, adapter.skip());
        assertSame(adapter, adapter.skip(1));
        assertSame(adapter, adapter.header("h"));
        assertSame(adapter, adapter.header());
        assertNotNull(adapter.getSheet());
        assertSame(reader, adapter.getReader());
        reader.close();
        adapter.close();
    }

}
