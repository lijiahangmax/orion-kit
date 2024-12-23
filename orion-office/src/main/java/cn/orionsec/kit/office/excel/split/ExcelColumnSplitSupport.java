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
package cn.orionsec.kit.office.excel.split;

import cn.orionsec.kit.office.excel.Excels;
import org.apache.poi.ss.usermodel.*;

/**
 * excel 列拆分
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/1/20 18:30
 */
class ExcelColumnSplitSupport {

    /**
     * 拆分
     *
     * @param sourceSheet    原sheet
     * @param targetWorkbook 目标workbook
     * @param targetSheet    目标sheet
     * @param columns        列
     * @param headers        表头
     * @param skip           跳过的行
     * @param streaming      是否为流式读取
     */
    protected static void split(Sheet sourceSheet, Workbook targetWorkbook, Sheet targetSheet,
                                int[] columns, String[] headers, int skip, boolean streaming) {
        int index = 0, rowIndex = 0;
        if (!streaming) {
            targetSheet.setDefaultColumnWidth(sourceSheet.getDefaultColumnWidth());
            targetSheet.setDefaultRowHeightInPoints(sourceSheet.getDefaultRowHeightInPoints());
            for (int column : columns) {
                targetSheet.setColumnWidth(column, sourceSheet.getColumnWidth(column));
                targetSheet.setDefaultColumnStyle(column, sourceSheet.getColumnStyle(column));
            }
        }
        // 表头
        if (headers != null) {
            Row headerRow = targetSheet.createRow(rowIndex++);
            for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
                Cell headerCell = headerRow.createCell(headerIndex);
                headerCell.setCellValue(headers[headerIndex]);
            }
        }
        for (Row sourceRow : sourceSheet) {
            // skip
            if (index++ < skip) {
                continue;
            }
            int cellIndex = 0;
            Row targetRow = targetSheet.createRow(rowIndex++);
            for (int column : columns) {
                Cell targetCell = targetRow.createCell(cellIndex++);
                Cell sourceCell = sourceRow.getCell(column);
                Excels.copyCellValue(sourceCell, targetCell);
                if (!streaming) {
                    CellStyle targetStyle = targetWorkbook.createCellStyle();
                    targetStyle.cloneStyleFrom(sourceCell.getCellStyle());
                    targetCell.setCellStyle(targetStyle);
                }
            }
        }
    }

}
