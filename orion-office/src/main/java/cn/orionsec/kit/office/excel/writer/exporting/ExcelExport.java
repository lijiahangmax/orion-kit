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

import cn.orionsec.kit.lang.utils.Assert;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.writer.BaseExcelWriteable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;

/**
 * excel 导出器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/28 11:15
 */
public class ExcelExport<T> extends BaseExcelWriteable {

    private final Sheet sheet;

    /**
     * 列配置
     */
    private final SheetConfig<T> sheetConfig;

    /**
     * 初始化器
     */
    private final ExportInitializer<T> initializer;

    /**
     * 处理器
     */
    private final ExportProcessor<T> processor;

    /**
     * 写入行数
     */
    private int rows;

    public ExcelExport(Class<T> targetClass) {
        this(targetClass, new SXSSFWorkbook(), null);
    }

    public ExcelExport(Class<T> targetClass, Workbook workbook) {
        this(targetClass, workbook, null);
    }

    public ExcelExport(Class<T> targetClass, Sheet sheet) {
        this(targetClass, sheet.getWorkbook(), sheet);
    }

    public ExcelExport(Class<T> targetClass, Workbook workbook, Sheet sheet) {
        super(workbook);
        Assert.notNull(targetClass, "target class is null");
        this.sheetConfig = new SheetConfig<>();
        this.initializer = new ExportInitializer<>(workbook, sheet, targetClass, sheetConfig);
        this.sheetConfig.initializer = initializer;
        this.processor = new ExportProcessor<>(workbook, initializer.sheet, sheetConfig);
        this.sheet = initializer.sheet;
    }

    /**
     * 创建导出实例并初始化
     *
     * @param targetClass targetClass
     * @param <T>         T
     * @return exporter
     */
    public static <T> ExcelExport<T> create(Class<T> targetClass) {
        return new ExcelExport<>(targetClass).init();
    }

    public static <T> ExcelExport<T> create(Class<T> targetClass, Workbook workbook) {
        return new ExcelExport<>(targetClass, workbook).init();
    }

    public static <T> ExcelExport<T> create(Class<T> targetClass, Sheet sheet) {
        return new ExcelExport<>(targetClass, sheet).init();
    }

    public static <T> ExcelExport<T> create(Class<T> targetClass, Workbook workbook, Sheet sheet) {
        return new ExcelExport<>(targetClass, workbook, sheet).init();
    }

    /**
     * 初始化
     *
     * @return this
     */
    public ExcelExport<T> init() {
        initializer.init();
        return this;
    }

    /**
     * 跳过一多
     *
     * @return this
     */
    public ExcelExport<T> skip() {
        initializer.rowIndex += 1;
        return this;
    }

    /**
     * 跳过行多
     *
     * @param i 行
     * @return this
     */
    public ExcelExport<T> skip(int i) {
        initializer.rowIndex += i;
        return this;
    }

    /**
     * 跳过空行
     *
     * @param skipNullRows true跳过
     * @return this
     */
    public ExcelExport<T> skipNullRows(boolean skipNullRows) {
        sheetConfig.sheetOption.setSkipNullRows(skipNullRows);
        return this;
    }

    /**
     * 跳过title
     *
     * @return this
     */
    public ExcelExport<T> skipTitle() {
        sheetConfig.sheetOption.setSkipTitle(true);
        return this;
    }

    /**
     * 设置sheet的名称
     *
     * @param sheetName sheetName
     * @return this
     */
    public ExcelExport<T> sheet(String sheetName) {
        if (sheetName != null) {
            sheetConfig.sheetOption.setName(sheetName);
            sheetConfig.sheetOption.setNameReset(true);
        }
        return this;
    }

    /**
     * 设置title的名称
     *
     * @param title title
     * @return this
     */
    public ExcelExport<T> title(String title) {
        if (title != null) {
            sheetConfig.sheetOption.setTitle(title);
        }
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelExport<T> protect(String password) {
        sheet.protectSheet(password);
        return this;
    }

    public ExcelExport<T> merge(int row, int firstCol, int lastCol) {
        return this.merge(new CellRangeAddress(row, row, firstCol, lastCol), true);
    }

    public ExcelExport<T> merge(int row, int firstCol, int lastCol, boolean mergeBorder) {
        return this.merge(new CellRangeAddress(row, row, firstCol, lastCol), mergeBorder);
    }

    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCol, int lastCol) {
        return this.merge(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), true);
    }

    /**
     * 合并单元格
     *
     * @param firstRow    合并开始行索引
     * @param lastRow     合并结束行索引
     * @param firstCol    合并开始列索引
     * @param lastCol     合并结束列索引
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(int firstRow, int lastRow, int firstCol, int lastCol, boolean mergeBorder) {
        return this.merge(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol), mergeBorder);
    }

    /**
     * 合并单元格
     *
     * @param region      region
     * @param mergeBorder 是否合并边框
     * @return this
     */
    public ExcelExport<T> merge(CellRangeAddress region, boolean mergeBorder) {
        initializer.checkInit();
        Excels.mergeCell(sheet, region);
        // 合并边框
        if (mergeBorder) {
            Optional.ofNullable(Excels.getCell(sheet, region.getFirstRow(), 0))
                    .map(Cell::getCellStyle)
                    .ifPresent(s -> {
                        Excels.mergeCellBorder(sheet, s.getBorderTop().getCode(), s.getTopBorderColor(), region);
                    });
        }
        return this;
    }

    /**
     * 设置表头
     *
     * @param headers 头
     * @return this
     */
    public ExcelExport<T> headers(String... headers) {
        initializer.checkInit();
        initializer.headers(false, headers);
        return this;
    }

    /**
     * 添加数据
     *
     * @param row row
     * @return this
     */
    public ExcelExport<T> addRow(T row) {
        return this.addRows(Lists.singleton(row));
    }

    /**
     * 添加数据
     *
     * @param rows rows
     * @return this
     */
    public ExcelExport<T> addRows(Collection<T> rows) {
        initializer.checkInit();
        Integer rowHeight = sheetConfig.sheetOption.getRowHeight();
        for (T row : rows) {
            this.rows++;
            if (row == null && sheetConfig.sheetOption.isSkipNullRows()) {
                continue;
            }
            // 空行会有样式
            Row dataRow = sheet.createRow(initializer.rowIndex);
            // 行高
            if (rowHeight != null) {
                dataRow.setHeightInPoints(rowHeight);
            }
            // 设置值
            sheetConfig.fieldOptions.forEach((col, option) -> {
                processor.setCellValue(dataRow.createCell(col), initializer.rowIndex, col, row, option);
            });
            initializer.rowIndex++;
        }
        return this;
    }

    @Override
    protected BaseExcelWriteable write(OutputStream out, String password, boolean close) {
        initializer.checkInit();
        initializer.finished();
        return super.write(out, password, close);
    }

    /**
     * 获取一个单元格样式 用于样式修改
     *
     * @return 单元格样式
     */
    public CellStyle createCellStyle() {
        return workbook.createCellStyle();
    }

    /**
     * 复制列样式 用于样式修改
     *
     * @param column column
     * @return 单元格样式
     */
    public CellStyle cloneCellStyle(int column) {
        CellStyle style = workbook.createCellStyle();
        // 获取列样式并且复制
        CellStyle columnStyle = sheetConfig.getColumnStyles().get(column);
        if (columnStyle != null) {
            style.cloneStyleFrom(columnStyle);
        }
        return style;
    }

    /**
     * 获取一个字体 用于样式修改
     *
     * @return 字体
     */
    public Font createFont() {
        return workbook.createFont();
    }

    /**
     * 获取一个格式 用于样式修改
     *
     * @return 格式
     */
    public DataFormat createFormat() {
        return workbook.createDataFormat();
    }

    public Sheet getSheet() {
        return sheet;
    }

    public SheetConfig<T> getSheetConfig() {
        return sheetConfig;
    }

    /**
     * 获取总行数
     *
     * @return 总行数
     */
    public int getLines() {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 获取最大列索引
     *
     * @return 最大列索引
     */
    public int getColumnMaxIndex() {
        return sheetConfig.sheetOption.getColumnMaxIndex();
    }

    /**
     * @return 当前行数
     */
    public int getRowIndex() {
        return initializer.rowIndex;
    }

    /**
     * @return 写入的数据行数
     */
    public int getRows() {
        return rows;
    }

}
