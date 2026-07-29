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

import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.office.excel.option.CellOption;
import cn.orionsec.kit.office.excel.type.ExcelFieldType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Excels 工具类测试
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/7/26 10:00
 */
public class ExcelsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final Workbook workbook = new XSSFWorkbook();

    private final Sheet sheet = workbook.createSheet("tests");

    @After
    public void closeWorkbook() {
        Excels.close(workbook);
    }

    private Cell createCell(int row, int column) {
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        return r.createCell(column);
    }

    @Test
    public void testColumnConvert() {
        Assert.assertEquals(1, Excels.getColumnNumber("A"));
        Assert.assertEquals(26, Excels.getColumnNumber("Z"));
        Assert.assertEquals(27, Excels.getColumnNumber("AA"));
        Assert.assertEquals("A", Excels.getColumnSymbol(1));
        Assert.assertEquals("Z", Excels.getColumnSymbol(26));
        Assert.assertEquals("AA", Excels.getColumnSymbol(27));
        Assert.assertTrue(Excels.getWidth(10) > 0);
    }

    @Test
    public void testCellText() {
        Cell cell = this.createCell(0, 0);
        Excels.setCellValue(cell, "你好 excel", ExcelFieldType.TEXT);
        Assert.assertEquals("你好 excel", Excels.getCellValue(cell));
        // 无类型默认 toString
        Cell cell1 = this.createCell(0, 1);
        Excels.setCellValue(cell1, "plain");
        Assert.assertEquals("plain", Excels.getCellValue(cell1));
        // 空单元格
        Assert.assertEquals("", Excels.getCellValue((Cell) null));
        Cell blank = this.createCell(0, 2);
        Assert.assertEquals("", Excels.getCellValue(blank));
    }

    @Test
    public void testCellNumber() {
        Cell cell = this.createCell(1, 0);
        Excels.setCellValue(cell, 1001, ExcelFieldType.NUMBER);
        Assert.assertEquals("1001", Excels.getCellValue(cell));
        Assert.assertEquals(Integer.valueOf(1001), Excels.getCellInteger(cell));
        Assert.assertEquals(Long.valueOf(1001L), Excels.getCellLong(cell));
        // 小数精度
        Cell decimalCell = this.createCell(1, 1);
        Excels.setCellValue(decimalCell, new BigDecimal("123.456"), ExcelFieldType.NUMBER);
        Assert.assertEquals(0, new BigDecimal("123.456").compareTo(Excels.getCellDecimal(decimalCell)));
        Assert.assertEquals("123.456", Excels.getCellValue(decimalCell));
        // 手机号
        Cell phoneCell = this.createCell(1, 2);
        Excels.setCellValue(phoneCell, 13800138000L, ExcelFieldType.NUMBER);
        Assert.assertEquals("13800138000", Excels.getCellPhone(phoneCell));
        // 空单元格
        Assert.assertNull(Excels.getCellDecimal(null));
    }

    @Test
    public void testCellDate() {
        Date date = Dates.parse("2023-08-15 12:30:45", "yyyy-MM-dd HH:mm:ss");
        // 无样式日期
        Cell cell = this.createCell(2, 0);
        Excels.setCellValue(cell, date, ExcelFieldType.DATE);
        Assert.assertEquals("2023-08-15 12:30:45", Dates.format(Excels.getCellDate(cell), "yyyy-MM-dd HH:mm:ss"));
        // 有样式日期 TEXT 读取为格式化时间
        Cell styleCell = this.createCell(2, 1);
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
        styleCell.setCellStyle(style);
        Excels.setCellValue(styleCell, date, ExcelFieldType.DATE);
        Assert.assertEquals(Dates.format(date), Excels.getCellValue(styleCell));
        // 格式化文本日期
        Cell formatCell = this.createCell(2, 2);
        Excels.setCellValue(formatCell, date, ExcelFieldType.DATE_FORMAT, new CellOption("yyyy-MM-dd"));
        Assert.assertEquals("2023-08-15", Excels.getCellValue(formatCell));
        Assert.assertEquals("2023-08-15", Dates.format(Excels.getCellDate(formatCell, new CellOption("yyyy-MM-dd")), "yyyy-MM-dd"));
    }

    @Test
    public void testCellBooleanAndFormula() {
        Cell boolCell = this.createCell(3, 0);
        Excels.setCellValue(boolCell, true, ExcelFieldType.BOOLEAN);
        Assert.assertEquals("true", Excels.getCellValue(boolCell));
        // 公式
        Excels.setCellValue(this.createCell(4, 0), 1.5, ExcelFieldType.NUMBER);
        Excels.setCellValue(this.createCell(4, 1), 2.5, ExcelFieldType.NUMBER);
        Cell formulaCell = this.createCell(4, 2);
        Excels.setCellValue(formulaCell, "A5+B5", ExcelFieldType.FORMULA);
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        Assert.assertEquals("4", Excels.getCellValue(formulaCell));
        Assert.assertEquals(0, new BigDecimal("4").compareTo(Excels.getCellDecimal(formulaCell)));
    }

    @Test
    public void testDecimalFormat() {
        Cell cell = this.createCell(5, 0);
        Excels.setCellValue(cell, new BigDecimal("1234567.891"), ExcelFieldType.DECIMAL_FORMAT, new CellOption("#,##0.00"));
        Assert.assertEquals("1,234,567.89", Excels.getCellValue(cell));
        // 格式化解析
        BigDecimal parsed = Excels.getCellDecimal(cell, new CellOption("#,##0.00"));
        Assert.assertEquals(0, new BigDecimal("1234567.89").compareTo(parsed));
    }

    @Test
    public void testMergeCell() {
        Excels.setCellValue(this.createCell(6, 0), "合并单元格", ExcelFieldType.TEXT);
        Excels.mergeCell(sheet, 6, 0, 2);
        Assert.assertEquals(1, sheet.getNumMergedRegions());
        // 合并后获取
        Cell merged = Excels.getCellMerge(sheet, 6, 2);
        Assert.assertNotNull(merged);
        Assert.assertEquals("合并单元格", Excels.getCellValue(merged));
    }

    @Test
    public void testGetRowAndCell() {
        Excels.setCellValue(this.createCell(7, 1), "cell", ExcelFieldType.TEXT);
        Assert.assertNotNull(Excels.getRow(sheet, 7));
        Assert.assertNull(Excels.getRow(sheet, 99));
        Assert.assertNotNull(Excels.getCell(sheet, 7, 1));
        Assert.assertNull(Excels.getCell(sheet, 7, 9));
        Assert.assertEquals("cell", Excels.getCellValue(Excels.getCell(sheet, 7, 1)));
    }

    @Test
    public void testWriteAndOpenWorkbook() throws Exception {
        Excels.setCellValue(this.createCell(0, 0), "回环写入", ExcelFieldType.TEXT);
        // 冻结 筛选
        Excels.freezeRow(sheet, 1);
        Excels.filterRow(sheet, 0, 2);
        File file = folder.newFile("excels-write.xlsx");
        Excels.write(workbook, file);
        // 读回
        Workbook read = Excels.openWorkbook(file);
        Assert.assertEquals("回环写入", Excels.getCellValue(Excels.getCell(read.getSheet("tests"), 0, 0)));
        Excels.close(read);
    }

    @Test
    public void testStreamingCheck() {
        Assert.assertFalse(Excels.isStreamingWorkbook(workbook));
        Assert.assertFalse(Excels.isStreamingWorkbook(workbook.getClass()));
        Assert.assertFalse(Excels.isStreamingSheet(sheet));
        Assert.assertFalse(Excels.isStreamingSheet(sheet.getClass()));
    }

}
