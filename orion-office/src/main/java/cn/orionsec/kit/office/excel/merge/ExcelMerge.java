/*
 * Copyright (c) 2019 - present Jiahang Li (kit.orionsec.cn ljh1553488six@139.com).
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
package cn.orionsec.kit.office.excel.merge;

import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.office.excel.Excels;
import cn.orionsec.kit.office.excel.writer.BaseExcelWriteable;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * excel 行合并器 不支持复杂操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/9/9 1:07
 */
public class ExcelMerge extends BaseExcelWriteable {

    /**
     * source workbook
     */
    private final Workbook sourceWorkbook;

    /**
     * source sheet
     */
    private final Sheet sourceSheet;

    private int rowIndex;

    /**
     * 跳过合并sheet的行
     */
    private int skipRows;

    /**
     * 行高
     */
    private int height;

    public ExcelMerge() {
        this(new SXSSFWorkbook(), null);
    }

    public ExcelMerge(Workbook sourceWorkbook, Sheet sourceSheet) {
        super(sourceWorkbook);
        Valid.isFalse(Excels.isStreamingSheet(sourceSheet), "origin is not be streaming");
        this.sourceWorkbook = sourceWorkbook;
        if (sourceSheet == null) {
            sourceSheet = sourceWorkbook.createSheet();
        }
        this.sourceSheet = sourceSheet;
        this.rowIndex = sourceSheet.getLastRowNum() + 1;
    }

    /**
     * 跳过行
     *
     * @return this
     */
    public ExcelMerge skip() {
        rowIndex += 1;
        return this;
    }

    /**
     * 跳过多行
     *
     * @param skip skip
     * @return this
     */
    public ExcelMerge skip(int skip) {
        rowIndex += skip;
        return this;
    }

    /**
     * 跳过一行合并sheet
     *
     * @return this
     */
    public ExcelMerge skipRows() {
        skipRows += 1;
        return this;
    }

    /**
     * 跳过多行合并sheet
     *
     * @param skip skip
     * @return this
     */
    public ExcelMerge skipRows(int skip) {
        skipRows += skip;
        return this;
    }

    /**
     * 设置行宽
     *
     * @param column 列索引
     * @param width  行宽
     * @return this
     */
    public ExcelMerge width(int column, int width) {
        sourceSheet.setColumnWidth(column, Excels.getWidth(width));
        return this;
    }

    /**
     * 设置默认行宽
     *
     * @param width 行宽
     * @return this
     */
    public ExcelMerge width(int width) {
        sourceSheet.setDefaultColumnWidth(width);
        return this;
    }

    /**
     * 行高
     *
     * @param height 行高
     * @return this
     */
    public ExcelMerge height(int height) {
        this.height = height;
        sourceSheet.setDefaultRowHeightInPoints(height);
        return this;
    }

    /**
     * 执行合并
     *
     * @param sheet 合并sheet
     * @return this
     */
    public ExcelMerge merge(Sheet sheet) {
        boolean streaming = Excels.isStreamingSheet(sheet);
        int i = 0;
        for (Row sheetRow : sheet) {
            if (i++ < skipRows) {
                continue;
            }
            Row originRow = sourceSheet.createRow(rowIndex++);
            if (!streaming && height != 0) {
                originRow.setHeightInPoints(height);
            }
            int cellIndex = 0;
            for (Cell cell : sheetRow) {
                Cell originCell = originRow.createCell(cellIndex++);
                if (!streaming) {
                    CellStyle originStyle = sourceWorkbook.createCellStyle();
                    originStyle.cloneStyleFrom(cell.getCellStyle());
                    originCell.setCellStyle(originStyle);
                }
                Excels.copyCellValue(cell, originCell);
            }
        }
        this.skipRows = 0;
        return this;
    }

    /**
     * 保护表格
     *
     * @param password password
     * @return this
     */
    public ExcelMerge protect(String password) {
        sourceSheet.protectSheet(password);
        return this;
    }

    public Workbook getSourceWorkbook() {
        return sourceWorkbook;
    }

    public Sheet getSourceSheet() {
        return sourceSheet;
    }

}
